package at.ac.tuwien.dsg.myx.monitor.aggregator.evaluation;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEventType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLExternalLinkEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostInstanceEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostProperties;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostPropertyEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostingEvent;
import at.ac.tuwien.dsg.myx.util.Tuple;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.ISubscriber;

public class StatisticsSubscriber implements ISubscriber<Event> {

    /**
     * Represents a time-line with the brick count present.
     */
    private Map<String, Tuple<Long, Long>> brickLifetimes = new HashMap<>();
    /**
     * Represents a mapping of runtime- to blueprint-ids.
     */
    private Map<String, String> runtime2blueprint = new HashMap<>();
    /**
     * Represents the time where an external connection was established or shut
     * down.
     */
    private Map<Tuple<String, XADLEventType>, Long> externalConnectionCounts = new HashMap<>();
    /**
     * Represents a mapping of runtime ids to their external connection
     * identifier.
     */
    private Map<String, Set<String>> runtime2externalConnections = new HashMap<>();
    /**
     * Represents the creation times of different kind of events.
     */
    private Map<Class<? extends Event>, List<Long>> eventTimes = new HashMap<>();
    /**
     * Represents the time a brick is added to a host or removed.
     */
    private Map<String, SortedMap<Long, List<XADLHostingEvent>>> hostingEvents = new HashMap<>();
    /**
     * Represents the time a host is added or removed.
     */
    private SortedMap<Long, List<XADLHostInstanceEvent>> hostInstanceEvents = new TreeMap<>();
    /**
     * Represents the cpu utilization of each host.
     */
    private Map<String, SortedMap<Long, Short>> cpuUtilization = new HashMap<>();
    /**
     * Represents the memory utilization of each host in kilobytes.
     */
    private Map<String, SortedMap<Long, Long>> memoryUtilization = new HashMap<>();

    // files used to save statistics data

    private String brickCountStatisticsFile;
    private String externalConnectionCountStatisticsFile;
    private String watchedBricksStatisticsFile;
    private Set<String> watchedBricks;
    private String eventStatisticsFile;
    private String hostStatisticsFile;
    private String hostCPUStatisticsFile;
    private String hostMemoryStatisticsFile;

    private ScheduledExecutorService persistenceExecutor = Executors.newScheduledThreadPool(1);
    private long startingTimestamp = System.currentTimeMillis() / 1000;

    public StatisticsSubscriber(String brickCountStatisticsFile, String externalConnectionCountStatisticsFile,
            String watchedBricksStatisticsFile, Set<String> watchedBricks, String hostStatisticsFile,
            String eventStatisticsFile, String hostCPUStatisticsFile, String hostMemoryStatisticsFile) {
        this.brickCountStatisticsFile = brickCountStatisticsFile;
        this.externalConnectionCountStatisticsFile = externalConnectionCountStatisticsFile;
        this.watchedBricksStatisticsFile = watchedBricksStatisticsFile;
        this.watchedBricks = watchedBricks;
        this.hostStatisticsFile = hostStatisticsFile;
        this.eventStatisticsFile = eventStatisticsFile;
        this.hostCPUStatisticsFile = hostCPUStatisticsFile;
        this.hostMemoryStatisticsFile = hostMemoryStatisticsFile;

        persistenceExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    persist();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    public void consume(Message<Event> message) {
        Event event = message.getData();

        // get the timestamp of the event in seconds
        final long timestamp = event.getTimestamp() / 1000;

        if (event instanceof XADLEvent) {
            XADLEvent e = (XADLEvent) event;

            // brick lifetime
            synchronized (brickLifetimes) {
                if (!brickLifetimes.containsKey(e.getXadlRuntimeId())) {
                    brickLifetimes.put(e.getXadlRuntimeId(), new Tuple<Long, Long>());
                    runtime2blueprint.put(e.getXadlRuntimeId(), e.getXadlBlueprintId());
                }
                switch (e.getXadlEventType()) {
                case ADD:
                    // use seconds
                    brickLifetimes.get(e.getXadlRuntimeId()).setFst(timestamp);
                    break;
                case REMOVE:
                    // use seconds
                    brickLifetimes.get(e.getXadlRuntimeId()).setSnd(timestamp);
                    break;
                default:
                    break;
                }
            }
            synchronized (externalConnectionCounts) {
                if (e.getXadlEventType() == XADLEventType.REMOVE
                        && runtime2externalConnections.containsKey(e.getXadlRuntimeId())) {
                    // for each saved external connection we save the timestamp
                    // for their shutdown, if no event was received
                    Set<String> externalConnectionIds = runtime2externalConnections.get(e.getXadlRuntimeId());
                    for (String extId : externalConnectionIds) {
                        Tuple<String, XADLEventType> key = new Tuple<>(extId, e.getXadlEventType());
                        if (!externalConnectionCounts.containsKey(key)) {
                            externalConnectionCounts.put(key, timestamp);
                            synchronized (eventTimes) {
                                if (!eventTimes.containsKey(XADLExternalLinkEvent.class)) {
                                    eventTimes.put(XADLExternalLinkEvent.class, new ArrayList<Long>());
                                }
                                eventTimes.get(XADLExternalLinkEvent.class).add(timestamp);
                            }
                        }
                    }
                }
            }
        } else if (event instanceof XADLExternalLinkEvent) {
            XADLExternalLinkEvent e = (XADLExternalLinkEvent) event;

            Tuple<String, XADLEventType> key = new Tuple<>(e.getXadlExternalConnectionIdentifier(),
                    e.getXadlEventType());
            synchronized (externalConnectionCounts) {
                if (!externalConnectionCounts.containsKey(key)) {
                    externalConnectionCounts.put(key, timestamp);
                } else if (e.getXadlEventType() == XADLEventType.ADD && externalConnectionCounts.get(key) > timestamp) {
                    externalConnectionCounts.put(key, timestamp);
                } else if (e.getXadlEventType() == XADLEventType.REMOVE
                        && externalConnectionCounts.get(key) < timestamp) {
                    externalConnectionCounts.put(key, timestamp);
                }
                // save the mapping
                if (!runtime2externalConnections.containsKey(e.getXadlRuntimeId())) {
                    runtime2externalConnections.put(e.getXadlRuntimeId(), new HashSet<String>());
                }
                runtime2externalConnections.get(e.getXadlRuntimeId()).add(e.getXadlExternalConnectionIdentifier());
            }
        } else if (event instanceof XADLHostInstanceEvent) {
            XADLHostInstanceEvent e = (XADLHostInstanceEvent) event;
            synchronized (hostInstanceEvents) {
                if (!hostInstanceEvents.containsKey(timestamp)) {
                    hostInstanceEvents.put(timestamp, new ArrayList<XADLHostInstanceEvent>());
                }
                hostInstanceEvents.get(timestamp).add(e);
            }
        } else if (event instanceof XADLHostingEvent) {
            XADLHostingEvent e = (XADLHostingEvent) event;
            synchronized (hostingEvents) {
                if (!hostingEvents.containsKey(e.getHostId())) {
                    hostingEvents.put(e.getHostId(), new TreeMap<Long, List<XADLHostingEvent>>());
                }
                if (!hostingEvents.get(e.getHostId()).containsKey(timestamp)) {
                    hostingEvents.get(e.getHostId()).put(timestamp, new ArrayList<XADLHostingEvent>());
                }
                hostingEvents.get(e.getHostId()).get(timestamp).add(e);
            }
        } else if (event instanceof XADLHostPropertyEvent) {
            XADLHostPropertyEvent e = (XADLHostPropertyEvent) event;
            Properties hostProperties = e.getHostProperties();

            // cpu
            if (hostProperties.containsKey(XADLHostProperties.CPU_SYSTEM_LOAD)) {
                synchronized (cpuUtilization) {
                    if (!cpuUtilization.containsKey(e.getHostId())) {
                        cpuUtilization.put(e.getHostId(), new TreeMap<Long, Short>());
                    }
                    Double utilization = (Double) hostProperties.get(XADLHostProperties.CPU_SYSTEM_LOAD) * 100;
                    cpuUtilization.get(e.getHostId()).put(timestamp, utilization.shortValue());
                }
            }
            // memory
            if (hostProperties.containsKey(XADLHostProperties.MEMORY_HEAP_USED)
                    || hostProperties.containsKey(XADLHostProperties.MEMORY_NON_HEAP_USED)) {
                long usedMemory = 0;
                if (hostProperties.containsKey(XADLHostProperties.MEMORY_HEAP_USED)) {
                    usedMemory += (long) hostProperties.get(XADLHostProperties.MEMORY_HEAP_USED) / 1024 / 1024;
                }
                if (hostProperties.containsKey(XADLHostProperties.MEMORY_NON_HEAP_USED)) {
                    usedMemory += (long) hostProperties.get(XADLHostProperties.MEMORY_NON_HEAP_USED) / 1024 / 1024;
                }
                synchronized (memoryUtilization) {
                    if (!memoryUtilization.containsKey(e.getHostId())) {
                        memoryUtilization.put(e.getHostId(), new TreeMap<Long, Long>());
                    }
                    memoryUtilization.get(e.getHostId()).put(timestamp, usedMemory);
                }
            }
        }
        synchronized (eventTimes) {
            if (!eventTimes.containsKey(event.getClass())) {
                eventTimes.put(event.getClass(), new ArrayList<Long>());
            }
            eventTimes.get(event.getClass()).add(timestamp);
        }
    }

    /**
     * Persist/Print the statistics.
     */
    public void persist() {
        // compute statistics
        long now = System.currentTimeMillis() / 1000;
        SortedMap<Long, Long> brickCountStatistics = new TreeMap<>();
        SortedMap<Long, Long> watchedBrickCountStatistics = new TreeMap<>();
        synchronized (brickLifetimes) {
            for (Entry<String, Tuple<Long, Long>> entry : brickLifetimes.entrySet()) {
                Tuple<Long, Long> brick = entry.getValue();
                long max = brick.getSnd() != null ? brick.getSnd() : now;

                for (long i = brick.getFst(); i <= max; i++) {
                    if (!brickCountStatistics.containsKey(i)) {
                        brickCountStatistics.put(new Long(i), 1L);
                    } else {
                        brickCountStatistics.put(new Long(i), brickCountStatistics.get(i) + 1);
                    }
                }
                if (watchedBricks.contains(runtime2blueprint.get(entry.getKey()))) {
                    for (long i = brick.getFst(); i <= max; i++) {
                        if (!watchedBrickCountStatistics.containsKey(i)) {
                            watchedBrickCountStatistics.put(new Long(i), 1L);
                        } else {
                            watchedBrickCountStatistics.put(new Long(i), watchedBrickCountStatistics.get(i) + 1);
                        }
                    }
                }
            }
        }
        SortedMap<Long, Long> externalConnectionCountStatistics = new TreeMap<>();
        synchronized (externalConnectionCounts) {
            for (Map.Entry<Tuple<String, XADLEventType>, Long> entry : externalConnectionCounts.entrySet()) {
                if (!externalConnectionCountStatistics.containsKey(entry.getValue())) {
                    externalConnectionCountStatistics.put(entry.getValue(), 0L);
                }
                switch (entry.getKey().getSnd()) {
                case ADD:
                    externalConnectionCountStatistics.put(entry.getValue(),
                            externalConnectionCountStatistics.get(entry.getValue()) + 1);
                    break;
                case REMOVE:
                    externalConnectionCountStatistics.put(entry.getValue(),
                            externalConnectionCountStatistics.get(entry.getValue()) - 1);
                    break;
                default:
                    break;
                }
            }
        }
        // create a mapping of class to index
        Map<Class<? extends Event>, Integer> classMapping = new HashMap<>();
        for (Class<? extends Event> clazz : eventTimes.keySet()) {
            classMapping.put(clazz, classMapping.size());
        }
        SortedMap<Long, Long[]> eventTimeStatistics = new TreeMap<>();
        synchronized (eventTimes) {
            for (Entry<Class<? extends Event>, List<Long>> entry : eventTimes.entrySet()) {
                for (long timestamp : entry.getValue()) {
                    if (!eventTimeStatistics.containsKey(timestamp)) {
                        Long[] stats = new Long[classMapping.size()];
                        for (int i = 0; i < classMapping.size(); i++) {
                            stats[i] = 0L;
                        }
                        eventTimeStatistics.put(timestamp, stats);
                    }
                    Long[] stats = eventTimeStatistics.get(timestamp);
                    stats[classMapping.get(entry.getKey())]++;
                    eventTimeStatistics.put(timestamp, stats);
                }
            }
        }
        // create a mapping of the host and its brick count over time
        final Map<String, Map<Long, Long>> hostBrickCount = new HashMap<>();
        long minimumHostsTimestamp = -1;
        synchronized (hostingEvents) {
            for (Entry<String, SortedMap<Long, List<XADLHostingEvent>>> entry : hostingEvents.entrySet()) {
                if (minimumHostsTimestamp == -1 || entry.getValue().firstKey() < minimumHostsTimestamp) {
                    minimumHostsTimestamp = entry.getValue().firstKey();
                }
                if (!hostBrickCount.containsKey(entry.getKey())) {
                    hostBrickCount.put(entry.getKey(), new HashMap<Long, Long>());
                }
                long current = 0;
                for (long timestamp = entry.getValue().firstKey(); timestamp <= now; timestamp++) {
                    if (entry.getValue().containsKey(timestamp)) {
                        for (final XADLHostingEvent e : entry.getValue().get(timestamp)) {
                            if (e.getXadlEventType() == XADLEventType.ADD) {
                                current += e.getHostedComponentIds().size();
                                current += e.getHostedConnectorIds().size();
                            } else if (e.getXadlEventType() == XADLEventType.REMOVE) {
                                current -= e.getHostedComponentIds().size();
                                current -= e.getHostedConnectorIds().size();
                            }
                        }
                    }
                    hostBrickCount.get(entry.getKey()).put(timestamp, current);
                }
            }
        }
        // compute statistics
        SortedMap<Long, Long> hostsCountStatistics = new TreeMap<>();
        synchronized (hostInstanceEvents) {
            if (hostInstanceEvents.size() > 0) {
                if (hostInstanceEvents.firstKey() < minimumHostsTimestamp) {
                    minimumHostsTimestamp = hostInstanceEvents.firstKey();
                }
            }
            Set<String> currentHosts = new HashSet<>();
            for (long timestamp = minimumHostsTimestamp; timestamp <= now; timestamp++) {
                Set<String> processedHosts = new HashSet<>();
                // process host instances
                if (hostInstanceEvents.containsKey(timestamp)) {
                    for (XADLHostInstanceEvent e : hostInstanceEvents.get(timestamp)) {
                        if (e.getXadlEventType() == XADLEventType.ADD
                                || (e.getXadlEventType() == XADLEventType.REMOVE && (!hostBrickCount.containsKey(e
                                        .getHostId()) || !hostBrickCount.get(e.getHostId()).containsKey(timestamp) || hostBrickCount
                                        .get(e.getHostId()).get(timestamp) == 0))) {
                            currentHosts.add(e.getHostId());
                            processedHosts.add(e.getHostId());
                        }
                    }
                }
                // process host components and connectors
                for (Entry<String, Map<Long, Long>> entry : hostBrickCount.entrySet()) {
                    if (!processedHosts.contains(entry.getKey()) && entry.getValue().containsKey(timestamp)) {
                        if (entry.getValue().get(timestamp) > 0) {
                            currentHosts.add(entry.getKey());
                        } else {
                            currentHosts.remove(entry.getKey());
                        }
                    }
                }
                hostsCountStatistics.put(timestamp, new Long(currentHosts.size()));
            }
        }

        // print statistics
        if (brickCountStatisticsFile != null && brickCountStatistics.size() > 0) {
            PrintStream ps = null;
            try {
                ps = new PrintStream(brickCountStatisticsFile);
                ps.println("time,amount");
                for (long i = startingTimestamp; i < now; i++) {
                    ps.print(i - startingTimestamp);
                    ps.print(",");
                    ps.print(brickCountStatistics.containsKey(i) ? brickCountStatistics.get(i) : 0);
                    ps.println();
                }
            } catch (FileNotFoundException e) {
                // ignore
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        }
        if (watchedBricksStatisticsFile != null && watchedBrickCountStatistics.size() > 0) {
            PrintStream ps = null;
            try {
                ps = new PrintStream(watchedBricksStatisticsFile);
                ps.println("time,amount");
                for (long i = startingTimestamp; i < now; i++) {
                    ps.print(i - startingTimestamp);
                    ps.print(",");
                    ps.print(watchedBrickCountStatistics.containsKey(i) ? watchedBrickCountStatistics.get(i) : 0);
                    ps.println();
                }
            } catch (FileNotFoundException e) {
                // ignore
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        }
        if (externalConnectionCountStatisticsFile != null && externalConnectionCountStatistics.size() > 0) {
            PrintStream ps = null;
            try {
                ps = new PrintStream(externalConnectionCountStatisticsFile);
                ps.println("time,amount");
                long current = 0;
                for (long i = startingTimestamp; i < now; i++) {
                    if (externalConnectionCountStatistics.containsKey(i)) {
                        current += externalConnectionCountStatistics.get(i);
                    }
                    ps.print(i - startingTimestamp);
                    ps.print(",");
                    ps.print(current);
                    ps.println();
                }
            } catch (FileNotFoundException e) {
                // ignore
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        }
        if (hostStatisticsFile != null && eventTimeStatistics.size() > 0) {
            PrintStream ps = null;
            try {
                ps = new PrintStream(hostStatisticsFile);
                // print header
                ps.println("time,amount");
                // print data
                for (long i = startingTimestamp; i < now; i++) {
                    ps.print(i - startingTimestamp);
                    ps.print(",");
                    ps.print(hostsCountStatistics.containsKey(i) ? hostsCountStatistics.get(i) : 0);
                    ps.println();
                }
            } catch (FileNotFoundException e) {
                // ignore
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        }
        if (eventStatisticsFile != null && eventTimeStatistics.size() > 0) {
            PrintStream ps = null;
            try {
                ps = new PrintStream(eventStatisticsFile);
                // print header
                ps.print("time");
                for (Class<? extends Event> clazz : classMapping.keySet()) {
                    ps.print(",");
                    ps.print(clazz.getSimpleName());
                }
                ps.print(",total");
                ps.println();
                // print data
                for (long i = startingTimestamp; i < now; i++) {
                    ps.print(i - startingTimestamp);
                    if (eventTimeStatistics.containsKey(i)) {
                        long total = 0;
                        for (Long count : eventTimeStatistics.get(i)) {
                            ps.print(",");
                            ps.print(count);
                            total += count;
                        }
                        ps.print(",");
                        ps.print(total);
                    } else {
                        for (int j = 0; j < classMapping.size() + 1; j++) {
                            ps.print(",0");
                        }
                    }
                    ps.println();
                }
            } catch (FileNotFoundException e) {
                // ignore
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        }
        if (hostCPUStatisticsFile != null && cpuUtilization.size() > 0) {
            PrintStream ps = null;
            try {
                synchronized (cpuUtilization) {
                    Set<String> hosts = cpuUtilization.keySet();
                    Map<String, Short> lastUtil = new HashMap<>();

                    ps = new PrintStream(hostCPUStatisticsFile);
                    // print header
                    ps.print("time");
                    for (String hostId : hosts) {
                        ps.print(",");
                        ps.print(hostId);
                    }
                    ps.println();
                    // print data
                    for (long i = startingTimestamp; i < now; i++) {
                        ps.print(i - startingTimestamp);
                        for (String hostId : hosts) {
                            ps.print(",");
                            if (cpuUtilization.get(hostId).containsKey(i)) {
                                ps.print(cpuUtilization.get(hostId).get(i));
                                lastUtil.put(hostId, cpuUtilization.get(hostId).get(i));
                            } else if (lastUtil.containsKey(hostId)) {
                                ps.print(lastUtil.get(hostId));
                            } else {
                                ps.print(0);
                            }
                        }
                        ps.println();
                    }
                }
            } catch (FileNotFoundException e) {
                // ignore
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        }
        if (hostMemoryStatisticsFile != null && memoryUtilization.size() > 0) {
            PrintStream ps = null;
            try {
                synchronized (memoryUtilization) {
                    Set<String> hosts = memoryUtilization.keySet();
                    Map<String, Long> lastUtil = new HashMap<>();

                    ps = new PrintStream(hostMemoryStatisticsFile);
                    // print header
                    ps.print("time");
                    for (String hostId : hosts) {
                        ps.print(",");
                        ps.print(hostId);
                    }
                    ps.println();
                    // print data
                    for (long i = startingTimestamp; i < now; i++) {
                        ps.print(i - startingTimestamp);
                        for (String hostId : hosts) {
                            ps.print(",");
                            if (memoryUtilization.get(hostId).containsKey(i)) {
                                ps.print(memoryUtilization.get(hostId).get(i));
                                lastUtil.put(hostId, memoryUtilization.get(hostId).get(i));
                            } else if (lastUtil.containsKey(hostId)) {
                                ps.print(lastUtil.get(hostId));
                            } else {
                                ps.print(0);
                            }
                        }
                        ps.println();
                    }
                }
            } catch (FileNotFoundException e) {
                // ignore
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        }
    }
}
