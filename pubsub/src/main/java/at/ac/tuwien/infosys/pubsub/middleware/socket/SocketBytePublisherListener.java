package at.ac.tuwien.infosys.pubsub.middleware.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import at.ac.tuwien.infosys.pubsub.middleware.PubSubHandler;
import at.ac.tuwien.infosys.pubsub.middleware.PublisherListener;
import at.ac.tuwien.infosys.pubsub.network.socket.SocketByteMessageReceiver;
import at.ac.tuwien.infosys.pubsub.network.socket.SocketByteMessageSender;

public class SocketBytePublisherListener extends PublisherListener<byte[]> {

    private int port;

    private ServerSocket server;

    public SocketBytePublisherListener(int port) {
        this.port = port;
        server = null;
    }

    @Override
    public PubSubHandler<byte[]> waitForNextHandler() {
        if (server == null) {
            // open socket
            try {
                server = new ServerSocket(port);
                server.setSoTimeout(1000);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (server != null) {
            try {
                // wait for a new connection
                Socket socket = server.accept();
                socket.setSoTimeout(1000);

                // create the handler
                PubSubHandler<byte[]> handler = new SocketBytePubSubHandler(
                        new SocketByteMessageReceiver(socket),
                        new SocketByteMessageSender(socket));
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
