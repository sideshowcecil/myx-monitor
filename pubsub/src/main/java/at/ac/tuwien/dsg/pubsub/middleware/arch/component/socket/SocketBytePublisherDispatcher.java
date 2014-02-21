package at.ac.tuwien.dsg.pubsub.middleware.arch.component.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import at.ac.tuwien.dsg.pubsub.middleware.arch.component.PublisherDispatcher;
import at.ac.tuwien.dsg.pubsub.middleware.arch.myx.MyxRuntime;
import at.ac.tuwien.dsg.pubsub.middleware.arch.network.Endpoint;
import at.ac.tuwien.dsg.pubsub.middleware.arch.network.socket.SocketByteMessageProtocol;
import edu.uci.isr.myx.fw.MyxUtils;

/**
 * Implementation of {@link PublisherDispatcher} based on the
 * {@link SocketByteMessageProtocol}.
 * 
 * @author bernd.rathmanner
 * 
 */
public class SocketBytePublisherDispatcher extends PublisherDispatcher<byte[]> {

    private int _port;
    private ServerSocket _server;

    @Override
    public void init() {
        try {
            _port = Integer.parseInt(MyxUtils.getInitProperties(this).getProperty("port", "6666"));
        } catch (NumberFormatException e) {
            // use default value
            _port = 6666;
        }
        super.init();
    }

    @Override
    public Endpoint<byte[]> waitForNextEndpoint() {
        if (_server == null) {
            // open socket
            try {
                _server = new ServerSocket(_port);
            } catch (IOException e) {
                // shutdown the dispatcher
                MyxRuntime.getInstance().shutdownDispatcher(this);
            }
        }
        if (_server != null) {
            try {
                // wait for a new connection
                Socket socket = _server.accept();
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
