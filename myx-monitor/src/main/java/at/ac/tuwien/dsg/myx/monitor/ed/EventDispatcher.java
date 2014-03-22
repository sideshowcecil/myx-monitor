package at.ac.tuwien.dsg.myx.monitor.ed;

import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEventType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostPropertyEvent;

public abstract class EventDispatcher implements Runnable {

    private final String architectureRuntimeId;
    private final String hostId;
    private final EventManager eventManager;

    public EventDispatcher(String architectureRuntimeId, String hostId, EventManager eventManager) {
        this.architectureRuntimeId = architectureRuntimeId;
        this.hostId = hostId;
        this.eventManager = eventManager;
    }

    protected String getArchitectureRuntimeId() {
        return architectureRuntimeId;
    }

    protected String getHostId() {
        return hostId;
    }

    /**
     * Dispatch a {@link Event} to the {@link EventManager}.
     * 
     * @param event
     */
    protected void dispatch(Event event) {
        if (eventManager != null) {
            event.setEventSourceId(this.getClass().getName());
            eventManager.handle(event);
        }
    }

    /**
     * Initialize a {@link XADLHostPropertyEvent}.
     * 
     * @return
     */
    protected XADLHostPropertyEvent initHostPropertyEvent() {
        return new XADLHostPropertyEvent(getArchitectureRuntimeId(), getHostId(), XADLEventType.UPDATE);
    }

}
