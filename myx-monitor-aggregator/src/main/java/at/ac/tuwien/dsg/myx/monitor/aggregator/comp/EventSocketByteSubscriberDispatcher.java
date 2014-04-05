package at.ac.tuwien.dsg.myx.monitor.aggregator.comp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import at.ac.tuwien.dsg.myx.monitor.aggregator.network.EventSocketByteMessageProtocol;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.middleware.comp.SubscriberDispatcher;
import at.ac.tuwien.dsg.pubsub.network.Endpoint;

/**
 * Implementation of SubscriberListener based on the SocketByteMessageProtocol.
 * 
 * @author bernd.rathmanner
 * 
 */
public class EventSocketByteSubscriberDispatcher extends SubscriberDispatcher<Event> {

    private int port;
    private ServerSocket server;

    @Override
    public void init() {
        try {
            port = Integer.parseInt(MyxMonitoringUtils.getInitProperties(this).getProperty("port", "6667"));
        } catch (NumberFormatException e) {
            // use default value
            port = 6667;
        }
        super.init();
    }

    @Override
    public Endpoint<Event> waitForNextEndpoint() {
        if (server == null) {
            // open socket
            try {
                server = new ServerSocket(port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (server != null) {
            try {
                // wait for a new connection
                Socket socket = server.accept();
                // create the handler
                return new EventSocketByteMessageProtocol(socket);
            } catch (IOException e) {
                // ignore
            }
        }
        return null;
    }

    @Override
    protected String getSubscriberEndpointClass() {
        return EventSocketByteSubscriberEndpoint.class.getName();
    }

}
