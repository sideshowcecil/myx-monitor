package at.ac.tuwien.dsg.myx.monitor.aggregator.evaluation;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
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
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLRuntimeEvent;
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
     * Represents a list of events for each brick, that can be associated with
     * the bricks creation and destruction.
     */
    private Map<String, List<Event>> brickEvents = new HashMap<>();

    // files used to save statistics data

    private String brickCountStatisticsFile;
    private String externalConnectionCountStatisticsFile;
    private String watchedBricksStatisticsFile;
    private Set<String> watchedBricks;
    private String brickEventTimeDiffFile;

    private ScheduledExecutorService persistenceExecutor = Executors.newScheduledThreadPool(1);

    public StatisticsSubscriber(String brickCountStatisticsFile, String externalConnectionCountStatisticsFile,
            String watchedBricksStatisticsFile, Set<String> watchedBricks, String brickEventTimeDiffFile) {
        this.brickCountStatisticsFile = brickCountStatisticsFile;
        this.externalConnectionCountStatisticsFile = externalConnectionCountStatisticsFile;
        this.watchedBricksStatisticsFile = watchedBricksStatisticsFile;
        this.watchedBricks = watchedBricks;
        this.brickEventTimeDiffFile = brickEventTimeDiffFile;

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
            synchronized (brickEvents) {
                if (!brickEvents.containsKey(e.getXadlRuntimeId()))
                    brickEvents.put(e.getXadlRuntimeId(), new ArrayList<Event>());
                brickEvents.get(e.getXadlRuntimeId()).add(e);
            }
        } else if (event instanceof XADLRuntimeEvent) {
            XADLRuntimeEvent e = (XADLRuntimeEvent) event;

            synchronized (brickEvents) {
                if (!brickEvents.containsKey(e.getXadlRuntimeId()))
                    brickEvents.put(e.getXadlRuntimeId(), new ArrayList<Event>());
                brickEvents.get(e.getXadlRuntimeId()).add(e);
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
        // TODO: what other data can we extract that is interesting
    }

    /**
     * Persist/Print the statistics.
     */
    public void persist() {
        SortedMap<Long, Long> brickCountStatistics = new TreeMap<>();
        SortedMap<Long, Long> externalConnectionCountStatistics = new TreeMap<>();
        SortedMap<Long, Long> watchedBrickCountStatistics = new TreeMap<>();
        double[] brickEventTimeDifferenceStatistics = new double[3];

        // compute statistics
        long now = System.currentTimeMillis() / 1000;
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
        synchronized (brickEvents) {
            List<Tuple<Double, Integer>> brickEventTimeDifferences = Arrays.asList(new Tuple<Double, Integer>(0.0, 0),
                    new Tuple<Double, Integer>(0.0, 0), new Tuple<Double, Integer>(0.0, 0));

            for (Entry<String, List<Event>> entry : brickEvents.entrySet()) {
                List<Event> events = entry.getValue();
                for (int i = 0; i + 1 < events.size(); i++) {
                    Tuple<Double, Integer> diff = brickEventTimeDifferences.get(i);

                    diff.setFst(diff.getFst() - events.get(i).getTimestamp() + events.get(i + 1).getTimestamp());
                    diff.setSnd(diff.getSnd() + 1);

                    brickEventTimeDifferences.set(i, diff);
                }
            }
            
            for (int i = 0; i < brickEventTimeDifferences.size(); i++) {
                Tuple<Double, Integer> diff = brickEventTimeDifferences.get(i);
                if (diff.getSnd() > 0)
                    brickEventTimeDifferenceStatistics[i] = diff.getFst() / diff.getSnd() / 1000;
                else
                    brickEventTimeDifferenceStatistics[i] = 0;
            }
        }

        // compute the starting timestamp
        long minimumTimestamp = Long.MAX_VALUE;
        if (brickCountStatistics.size() > 0) {
            minimumTimestamp = brickCountStatistics.firstKey();
        }
        if (externalConnectionCountStatistics.size() > 0
                && minimumTimestamp > externalConnectionCountStatistics.firstKey()) {
            minimumTimestamp = externalConnectionCountStatistics.firstKey();
        }

        // print statistics
        if (brickCountStatistics.size() > 0 && brickCountStatisticsFile != null) {
            PrintStream ps = null;
            try {
                ps = new PrintStream(brickCountStatisticsFile);
                ps.println("time,amount");
                for (long i = minimumTimestamp; i < now; i++) {
                    if (brickCountStatistics.containsKey(i)) {
                        ps.print(i - minimumTimestamp);
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
        if (watchedBrickCountStatistics.size() > 0 && watchedBricksStatisticsFile != null) {
            PrintStream ps = null;
            try {
                ps = new PrintStream(watchedBricksStatisticsFile);
                ps.println("time,amount");
                for (long i = minimumTimestamp; i < now; i++) {
                    if (watchedBrickCountStatistics.containsKey(i)) {
                        ps.print(i - minimumTimestamp);
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
        if (externalConnectionCountStatistics.size() > 0 && externalConnectionCountStatisticsFile != null) {
            PrintStream ps = null;
            try {
                ps = new PrintStream(externalConnectionCountStatisticsFile);
                ps.println("time,amount");
                long current = 0;
                minimumTimestamp = externalConnectionCountStatistics.firstKey() < minimumTimestamp ? externalConnectionCountStatistics
                        .firstKey() : minimumTimestamp;
                for (long i = minimumTimestamp; i < now; i++) {
                    if (externalConnectionCountStatistics.containsKey(i)) {
                        current += externalConnectionCountStatistics.get(i);
                    }
                    ps.print(i - minimumTimestamp);
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
        if (brickEventTimeDiffFile != null) {
            PrintStream ps = null;
            try {
                ps = new PrintStream(brickEventTimeDiffFile);
                ps.println("events,differnces");
                ps.print("create-running,");
                ps.println(brickEventTimeDifferenceStatistics[0]);
                ps.print("running-shutdown,");
                ps.println(brickEventTimeDifferenceStatistics[1]);
                ps.print("shutdown-destroy,");
                ps.println(brickEventTimeDifferenceStatistics[2]);
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
