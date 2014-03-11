package at.ac.tuwien.dsg.myx.monitor.em.events;

import java.util.UUID;

public abstract class Event {

    private final String id;
    private final String architectureRuntimeId;
    private final long timestamp;

    private String eventSourceId;

    public Event(String archInstanceId) {
        id = UUID.randomUUID().toString();
        this.architectureRuntimeId = archInstanceId;
        timestamp = System.currentTimeMillis();
    }

    public Event(Event copyFrom) {
        id = UUID.randomUUID().toString();
        architectureRuntimeId = copyFrom.getArchitectureRuntimeId();
        timestamp = copyFrom.getTimestamp();
        eventSourceId = copyFrom.getEventSourceId();
    }

    public String getId() {
        return id;
    }

    public String getArchitectureRuntimeId() {
        return architectureRuntimeId;
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
        return "Event [id=" + getId() + ", architectureRuntimeId=" + getArchitectureRuntimeId() + ", timestamp="
                + getTimestamp() + ", eventSourceId=" + getEventSourceId() + "]";
    }
}
