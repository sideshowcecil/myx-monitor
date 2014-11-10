package at.ac.tuwien.dsg.myx.monitor.aggregator.evaluation;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

    // files used to save statistics data

    private String brickCountStatisticsFile;
    private String externalConnectionCountStatisticsFile;
    private String watchedBricksStatisticsFile;
    private Set<String> watchedBricks;
    private String eventStatisticsFile;

    private ScheduledExecutorService persistenceExecutor = Executors.newScheduledThreadPool(1);
    private long startingTimestamp = System.currentTimeMillis() / 1000;

    public StatisticsSubscriber(String brickCountStatisticsFile, String externalConnectionCountStatisticsFile,
            String watchedBricksStatisticsFile, Set<String> watchedBricks, String eventStatisticsFile) {
        this.brickCountStatisticsFile = brickCountStatisticsFile;
        this.externalConnectionCountStatisticsFile = externalConnectionCountStatisticsFile;
        this.watchedBricksStatisticsFile = watchedBricksStatisticsFile;
        this.watchedBricks = watchedBricks;
        this.eventStatisticsFile = eventStatisticsFile;

        persistenceExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                persist();
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
        }
        synchronized (eventTimes) {
            if (!eventTimes.containsKey(event.getClass())) {
                eventTimes.put(event.getClass(), new ArrayList<Long>());
            }
            eventTimes.get(event.getClass()).add(timestamp);
        }
        // TODO: what other data can we extract that is interesting
        // TODO: häufigkeiten der einzelnen events, wann treten events auf,
        // speichern von timestamps aller events und anzeige dieser in einer art
        // histogram für jede art von event
        // dadurch kann dann gezeigt werden, dass es sich nicht lohnt auf
        // runtime events zu subscriben.
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
            for (Tuple<Long, Long> brick : brickLifetimes.values()) {
                long max = brick.getSnd() != null ? brick.getSnd() : now;
                for (long i = brick.getFst(); i <= max; i++) {
                    if (!brickCountStatistics.containsKey(i)) {
                        brickCountStatistics.put(new Long(i), 1L);
                    } else {
                        brickCountStatistics.put(new Long(i), brickCountStatistics.get(i) + 1);
                    }
                }
            }
            for (Entry<String, Tuple<Long, Long>> entry : brickLifetimes.entrySet()) {
                // is the brick watched
                if (watchedBricks.contains(runtime2blueprint.get(entry.getKey()))) {
                    long max = entry.getValue().getSnd() != null ? entry.getValue().getSnd() : now;
                    for (long i = entry.getValue().getFst(); i <= max; i++) {
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

        // print statistics
        if (brickCountStatisticsFile != null && brickCountStatistics.size() > 0) {
            PrintStream ps = null;
            try {
                ps = new PrintStream(brickCountStatisticsFile);
                ps.println("time,amount");
                for (long i = startingTimestamp; i < now; i++) {
                    if (brickCountStatistics.containsKey(i)) {
                        ps.print(i - startingTimestamp);
                        ps.print(",");
                        ps.print(brickCountStatistics.get(i));
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
        if (watchedBricksStatisticsFile != null && watchedBrickCountStatistics.size() > 0) {
            PrintStream ps = null;
            try {
                ps = new PrintStream(watchedBricksStatisticsFile);
                ps.println("time,amount");
                for (long i = startingTimestamp; i < now; i++) {
                    if (watchedBrickCountStatistics.containsKey(i)) {
                        ps.print(i - startingTimestamp);
                        ps.print(",");
                        ps.print(watchedBrickCountStatistics.get(i));
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
        if (externalConnectionCountStatisticsFile != null && externalConnectionCountStatistics.size() > 0) {
            PrintStream ps = null;
            try {
                ps = new PrintStream(externalConnectionCountStatisticsFile);
                ps.println("time,amount");
                long current = 0;
                startingTimestamp = externalConnectionCountStatistics.firstKey() < startingTimestamp ? externalConnectionCountStatistics
                        .firstKey() : startingTimestamp;
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
                ps.println();
                // print data
                for (long i = startingTimestamp; i < now; i++) {
                    ps.print(i - startingTimestamp);
                    if (eventTimeStatistics.containsKey(i)) {
                        for (Long count : eventTimeStatistics.get(i)) {
                            ps.print(",");
                            ps.print(count);
                        }
                    } else {
                        for (int j = 0; j < classMapping.size(); j++) {
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
    }
}
