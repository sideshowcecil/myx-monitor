package at.ac.tuwien.dsg.myx.monitor.em;

import at.ac.tuwien.dsg.myx.monitor.em.events.Event;

public interface EventManager {
    /**
     * Handle the given event.
     * 
     * @param event
     */
    public void handle(Event event);
}
