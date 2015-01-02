package at.ac.tuwien.dsg.pubsub.em;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostEvent;
import at.ac.tuwien.dsg.myx.util.EventUtils;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.network.Endpoint;
import at.ac.tuwien.dsg.pubsub.network.socket.EventSocketByteMessageProtocol;

public class EventManagerImpl implements EventManager, Runnable {

    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    private final String architectureRuntimeId;
    private final String hostId;
    private final String connectionString;

    private BlockingDeque<Event> deque = new LinkedBlockingDeque<>();

    private Endpoint<Event> endpoint;

    public EventManagerImpl(String architectureRuntimeId, String hostId, String connectionString) {
        this.architectureRuntimeId = architectureRuntimeId;
        this.hostId = hostId;
        this.connectionString = connectionString;
        executor.execute(this);
    }

    @Override
    public synchronized void handle(Event event) {
        // set the architecture runtime id
        event.setArchitectureRuntimeId(architectureRuntimeId);
        if (event instanceof XADLHostEvent) {
            // set the host id
            ((XADLHostEvent) event).setHostId(hostId);
        }
        deque.addLast(event);
    }

    @Override
    public void run() {
        if (connectionString != null && !connectionString.isEmpty()) {
            URI uri;
            try {
                uri = new URI(connectionString);
                Socket s = new Socket(uri.getHost(), uri.getPort());
                endpoint = new EventSocketByteMessageProtocol(s);
            } catch (URISyntaxException | IOException e) {
            }
        }
        try {
            int failCount = 0;
            while (true) {
                Event event = deque.takeFirst();
                if (endpoint != null) {
                    Message<Event> msg = new Message<Event>(EventUtils.getTopic(event), event);
                    try {
                        endpoint.send(msg);
                        failCount = 0;
                    } catch (IOException e) {
                        failCount++;
                        if (failCount > 5) {
                            // close the connection after 5 failed transmissions
                            endpoint.close();
                            endpoint = null;
                        } else {
                            // add the event again so we can try to deliver it once more
                            deque.addFirst(event);
                            Thread.sleep(25);
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
        }
    }

}
