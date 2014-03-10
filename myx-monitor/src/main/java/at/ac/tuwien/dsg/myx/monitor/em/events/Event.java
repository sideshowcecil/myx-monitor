package at.ac.tuwien.dsg.myx.monitor.em.events;

import java.util.UUID;

public abstract class Event {

    private final String id;
    private final String archInstanceId;
    private final long timestamp;

    private String eventSourceId;

    public Event(String archInstanceId) {
        id = UUID.randomUUID().toString();
        this.archInstanceId = archInstanceId;
        timestamp = System.currentTimeMillis();
    }

    public Event(Event copyFrom) {
        id = UUID.randomUUID().toString();
        archInstanceId = copyFrom.getArchInstanceId();
        timestamp = copyFrom.getTimestamp();
        eventSourceId = copyFrom.getEventSourceId();
    }

    public String getId() {
        return id;
    }

    public String getArchInstanceId() {
        return archInstanceId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getEventSourceId() {
        return eventSourceId;
    }

    public void setEventSourceId(String eventSourceId) {
        this.eventSourceId = eventSourceId;
    }

    @Override
    public String toString() {
        return "Event [id=" + getId() + ", archInstanceId=" + getArchInstanceId() + ", timestamp=" + getTimestamp()
                + ", eventSourceId=" + getEventSourceId() + "]";
    }
}
