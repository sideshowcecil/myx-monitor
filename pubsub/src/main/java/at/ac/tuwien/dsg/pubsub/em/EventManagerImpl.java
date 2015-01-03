package at.ac.tuwien.dsg.pubsub.em;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostEvent;
import at.ac.tuwien.dsg.myx.util.EventUtils;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.network.Endpoint;
import at.ac.tuwien.dsg.pubsub.network.socket.EventSocketByteMessageProtocol;

public class EventManagerImpl implements EventManager {

    private static Logger logger = LoggerFactory.getLogger(EventManagerImpl.class);

    private final String architectureRuntimeId;
    private final String hostId;
    private final String connectionString;

    private Endpoint<Event> endpoint = null;

    public EventManagerImpl(String architectureRuntimeId, String hostId, String connectionString) {
        this.architectureRuntimeId = architectureRuntimeId;
        this.hostId = hostId;
        this.connectionString = connectionString;
        // connect to the specified endpoint
        connect();
    }

    @Override
    public synchronized void handle(Event event) {
        if (endpoint == null) {
            return;
        }
        setEventProperties(event);
        sendEvent(event);
    }

    /**
     * Connect the to the specified address.
     */
    private void connect() {
        if (connectionString == null || connectionString.isEmpty()) {
            return;
        }
        try {
            URI uri = new URI(connectionString);
            endpoint = new EventSocketByteMessageProtocol(new Socket(uri.getHost(), uri.getPort()));
        } catch (URISyntaxException | IOException e) {
            logger.error("Could not connect to the specified address '" + connectionString + "'", e);
        }
    }

    /**
     * Set some basic properties of the event.
     * 
     * @param event
     */
    protected void setEventProperties(Event event) {
        // set architecture runtime id
        event.setArchitectureRuntimeId(architectureRuntimeId);
        if (event instanceof XADLHostEvent) {
            // set the host id
            ((XADLHostEvent) event).setHostId(hostId);
        }
    }

    /**
     * Send the event to the specified endpoint.
     * 
     * @param event
     */
    private void sendEvent(Event event) {
        // pack the event into a message
        Message<Event> msg = new Message<Event>(EventUtils.getTopic(event), event);
        // send it
        IOException last = null;
        for (int i = 0; i < 3; i++) {
            try {
                endpoint.send(msg);
                return;
            } catch (IOException e) {
                // ignore the exception and try it once more
                last = e;
            }
        }
        // if we could not send the message we invalidate the connection
        logger.error("Could not send message", last);
        endpoint.close();
        endpoint = null;
    }
}
