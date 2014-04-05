package at.ac.tuwien.dsg.pubsub.middleware.myx;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostEvent;

public class EventManagerImpl implements EventManager, Runnable {

    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    private final String architectureRuntimeId;
    private final String hostId;
    private final String connectionString;

    private BlockingQueue<Event> queue = new LinkedBlockingQueue<>();

    public EventManagerImpl(String architectureRuntimeId, String hostId, String connectionString) {
        this.architectureRuntimeId = architectureRuntimeId;
        this.hostId = hostId;
        this.connectionString = connectionString;
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
        if (connectionString != null) {
            // TODO connect
        }
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
