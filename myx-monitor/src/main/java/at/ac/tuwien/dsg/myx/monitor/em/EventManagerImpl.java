package at.ac.tuwien.dsg.myx.monitor.em;

import at.ac.tuwien.dsg.myx.monitor.em.events.Event;

public class EventManagerImpl implements EventManager {

    @Override
    public void handle(Event event) {
        // TODO Auto-generated method stub
        System.out.println(event);
    }

}
