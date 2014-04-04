package at.ac.tuwien.dsg.myx.monitor.em;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostEvent;

public class EventManagerImpl implements EventManager, Runnable {

    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    private String architectureRuntimeId;
    private String hostId;
    
    private BlockingQueue<Event> queue = new LinkedBlockingQueue<>();

    public EventManagerImpl(String architectureRuntimeId, String hostId) {
        this.architectureRuntimeId = architectureRuntimeId;
        this.hostId = hostId;
        executor.execute(this);
    }

    @Override
    public void handle(Event event) {
        // set the architecture runtime id 
        event.setArchitectureRuntimeId(architectureRuntimeId);
        if (event instanceof XADLHostEvent) {
            // set the host id
            ((XADLHostEvent) event).setHostId(hostId);
        }
        queue.add(event);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Event event = queue.take();
                // TODO: integrate the event dispatching to the real monitor
                System.err.println(event);
            }
        } catch (InterruptedException e) {
        }
    }

}
