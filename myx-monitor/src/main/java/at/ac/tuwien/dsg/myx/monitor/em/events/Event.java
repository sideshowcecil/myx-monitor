package at.ac.tuwien.dsg.myx.monitor.em.events;

import java.io.Serializable;
import java.util.UUID;

public abstract class Event implements Serializable {

    private static final long serialVersionUID = -7750911233567472330L;
    
    private final String id;
    private final long timestamp;

    private String architectureRuntimeId;
    private String eventSourceId;

    public Event() {
        id = UUID.randomUUID().toString();
        timestamp = System.currentTimeMillis();
    }

    public Event(Event copyFrom) {
        id = UUID.randomUUID().toString();
        architectureRuntimeId = copyFrom.getArchitectureRuntimeId();
        timestamp = System.currentTimeMillis();
        eventSourceId = copyFrom.getEventSourceId();
    }

    public String getId() {
        return id;
    }

    public String getArchitectureRuntimeId() {
        return architectureRuntimeId;
    }

    public void setArchitectureRuntimeId(String architectureRuntimeId) {
        this.architectureRuntimeId = architectureRuntimeId;
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
