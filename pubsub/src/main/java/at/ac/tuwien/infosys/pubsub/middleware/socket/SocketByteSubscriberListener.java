package at.ac.tuwien.infosys.pubsub.middleware.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import at.ac.tuwien.infosys.pubsub.middleware.SubscriberHandler;
import at.ac.tuwien.infosys.pubsub.middleware.SubscriberListener;
import at.ac.tuwien.infosys.pubsub.network.socket.SocketByteMessageProtocol;

/**
 * Implementation of SubscriberListener based on the SocketByteMessageProtocol.
 * 
 * @author bernd.rathmanner
 * 
 */
public class SocketByteSubscriberListener extends SubscriberListener<byte[]> {

    private int port;

    private ServerSocket server;

    public SocketByteSubscriberListener(int port) {
        this.port = port;
        server = null;
    }

    @Override
    public SubscriberHandler<byte[]> waitForNextHandler() {
        if (server == null) {
            // open socket
            try {
                server = new ServerSocket(port);
                server.setSoTimeout(2500);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (server != null) {
            try {
                // wait for a new connection
                Socket socket = server.accept();
                socket.setSoTimeout(2500);

                // create the handler
                SubscriberHandler<byte[]> handler = new SocketByteSubscriberHandler(new SocketByteMessageProtocol(
                        socket));
                return handler;
            } catch (SocketTimeoutException e) {
                // ignore
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void shutdown() {
        if (server != null) {
            try {
                server.close();
            } catch (IOException e) {
            }
        }
    }

}
