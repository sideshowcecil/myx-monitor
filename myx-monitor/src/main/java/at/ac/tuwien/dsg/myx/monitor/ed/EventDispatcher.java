package at.ac.tuwien.dsg.myx.monitor.ed;

import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEventType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostPropertyEvent;

public abstract class EventDispatcher implements Runnable {

    private final EventManager eventManager;

    public EventDispatcher(EventManager eventManager) {
        this.eventManager = eventManager;
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
        return new XADLHostPropertyEvent(XADLEventType.UPDATE);
    }

}
