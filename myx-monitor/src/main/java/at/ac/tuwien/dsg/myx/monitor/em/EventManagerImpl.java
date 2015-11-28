package at.ac.tuwien.dsg.myx.monitor.em;

import at.ac.tuwien.dsg.myx.monitor.em.events.Event;

public class EventManagerImpl implements EventManager {

    public EventManagerImpl() {
    }

    @Override
    public void handle(Event event) {
        // default behavior is to drop the events
    }

}
