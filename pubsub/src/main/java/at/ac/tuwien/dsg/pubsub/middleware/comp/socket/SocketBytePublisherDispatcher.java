package at.ac.tuwien.dsg.pubsub.middleware.comp.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.middleware.comp.PublisherDispatcher;
import at.ac.tuwien.dsg.pubsub.middleware.network.Endpoint;
import at.ac.tuwien.dsg.pubsub.middleware.network.socket.SocketByteMessageProtocol;

/**
 * Implementation of {@link PublisherDispatcher} based on the
 * {@link SocketByteMessageProtocol}.
 * 
 * @author bernd.rathmanner
 * 
 */
public class SocketBytePublisherDispatcher extends PublisherDispatcher<byte[]> {

    private int port;
    private ServerSocket server;

    @Override
    public void init() {
        try {
            port = Integer.parseInt(MyxMonitoringUtils.getInitProperties(this).getProperty("port", "6666"));
        } catch (NumberFormatException e) {
            // use default value
            port = 6666;
        }
        super.init();
    }

    @Override
    public Endpoint<byte[]> waitForNextEndpoint() {
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
                return new SocketByteMessageProtocol(socket);
            } catch (IOException e) {
                // ignore
            }
        }
        return null;
    }

    @Override
    protected String getPublisherEndpointClass() {
        return SocketBytePublisherEndpoint.class.getName();
    }

}
