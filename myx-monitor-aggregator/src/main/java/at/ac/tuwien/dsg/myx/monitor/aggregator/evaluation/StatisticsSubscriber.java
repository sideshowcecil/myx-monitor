package at.ac.tuwien.dsg.myx.monitor.aggregator.evaluation;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

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

    // files used to save statistics data

    private String brickCountStatisticsFile;
    private String externalConnectionCountStatisticsFile;
    private String watchedBricksStatisticsFile;
    private Set<String> watchedBricks;

    public StatisticsSubscriber(String brickCountStatisticsFile, String externalConnectionCountStatisticsFile,
            String watchedBricksStatisticsFile, Set<String> watchedBricks) {
        this.brickCountStatisticsFile = brickCountStatisticsFile;
        this.externalConnectionCountStatisticsFile = externalConnectionCountStatisticsFile;
        this.watchedBricksStatisticsFile = watchedBricksStatisticsFile;
        this.watchedBricks = watchedBricks;
    }

    @Override
    public void consume(Message<Event> message) {
        if (message.getData() instanceof XADLEvent) {
            XADLEvent e = (XADLEvent) message.getData();

            // brick lifetime
            if (!brickLifetimes.containsKey(e.getXadlRuntimeId())) {
                brickLifetimes.put(e.getXadlRuntimeId(), new Tuple<Long, Long>());
                runtime2blueprint.put(e.getXadlRuntimeId(), e.getXadlBlueprintId());
            }
            switch (e.getXadlEventType()) {
            case ADD:
                // use seconds
                brickLifetimes.get(e.getXadlRuntimeId()).setFst(e.getTimestamp() / 1000);
                break;
            case REMOVE:
                // use seconds
                brickLifetimes.get(e.getXadlRuntimeId()).setSnd(e.getTimestamp() / 1000);
                break;
            default:
                break;
            }
        } else if (message.getData() instanceof XADLExternalLinkEvent) {
            XADLExternalLinkEvent e = (XADLExternalLinkEvent) message.getData();

            Tuple<String, XADLEventType> key = new Tuple<>(e.getXadlExternalConnectionIdentifier(),
                    e.getXadlEventType());
            long timestamp = e.getTimestamp() / 1000; // use seconds
            if (!externalConnectionCounts.containsKey(key)) {
                externalConnectionCounts.put(key, timestamp);
            } else if (e.getXadlEventType() == XADLEventType.ADD && externalConnectionCounts.get(key) > timestamp) {
                externalConnectionCounts.put(key, timestamp);
            } else if (e.getXadlEventType() == XADLEventType.REMOVE && externalConnectionCounts.get(key) < timestamp) {
                externalConnectionCounts.put(key, timestamp);
            }
        } else {
            return;
        }
        // TODO: what other data can we extract that is interesting
        persist();
    }

    /**
     * Persist/Print the statistics.
     */
    public void persist() {
        SortedMap<Long, Long> brickCountStatistics = new TreeMap<>();
        SortedMap<Long, Long> externalConnectionCountStatistics = new TreeMap<>();
        SortedMap<Long, Long> watchedBrickCountStatistics = new TreeMap<>();

        // compute statistics
        long now = System.currentTimeMillis() / 1000;
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
    }
}
