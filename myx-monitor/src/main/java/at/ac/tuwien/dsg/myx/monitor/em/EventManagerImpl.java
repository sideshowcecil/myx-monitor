package at.ac.tuwien.dsg.myx.monitor.em;

import at.ac.tuwien.dsg.myx.monitor.em.events.Event;

public class EventManagerImpl implements EventManager {

    @Override
    public void handle(Event event) {
        // TODO on application instantiation should we issue those as init messages, so each connected subscriber gets the basic architecture
        // TODO but we also need to take care of dynamically created bricks, thus we have to cache everything???
        // TODO Auto-generated method stub
        System.out.println(event);
    }

}
