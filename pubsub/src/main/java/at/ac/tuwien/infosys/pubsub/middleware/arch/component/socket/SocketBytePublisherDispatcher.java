package at.ac.tuwien.infosys.pubsub.middleware.arch.component.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import at.ac.tuwien.infosys.pubsub.middleware.arch.component.PublisherDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.network.Endpoint;
import at.ac.tuwien.infosys.pubsub.middleware.arch.network.socket.SocketByteMessageProtocol;

/**
 * Implementation of {@link PublisherDispatcher} based on the {@link SocketByteMessageProtocol}.
 * 
 * @author bernd.rathmanner
 * 
 */
public class SocketBytePublisherDispatcher extends PublisherDispatcher<byte[]> {

    private int port;

    private ServerSocket server;

    public SocketBytePublisherDispatcher(int port) {
        this.port = port;
        server = null;
    }

    @Override
    public Endpoint<byte[]> waitForNewEndpoint() {
        if (server == null) {
            // open socket
            try {
                server = new ServerSocket(port);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (server != null) {
            try {
                // wait for a new connection
                Socket socket = server.accept();
                // create the handler
                return new SocketByteMessageProtocol(socket);
            } catch (SocketTimeoutException e) {
                // ignore
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

}
