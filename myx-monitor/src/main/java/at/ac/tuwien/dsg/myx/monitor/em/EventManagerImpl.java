package at.ac.tuwien.dsg.myx.monitor.em;

import at.ac.tuwien.dsg.myx.monitor.em.events.Event;

public class EventManagerImpl implements EventManager {

    public EventManagerImpl(String architectureRuntimeId, String hostId, String connectionString) {
    }

    @Override
    public void handle(Event event) {
        // default bahaviour is to drop the events
    }

}
