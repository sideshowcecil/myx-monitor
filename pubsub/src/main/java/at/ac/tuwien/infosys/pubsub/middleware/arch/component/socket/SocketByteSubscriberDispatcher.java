package at.ac.tuwien.infosys.pubsub.middleware.arch.component.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import at.ac.tuwien.infosys.pubsub.middleware.arch.component.SubscriberDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.myx.MyxRuntime;
import at.ac.tuwien.infosys.pubsub.middleware.arch.network.Endpoint;
import at.ac.tuwien.infosys.pubsub.middleware.arch.network.socket.SocketByteMessageProtocol;
import edu.uci.isr.myx.fw.MyxUtils;

/**
 * Implementation of SubscriberListener based on the SocketByteMessageProtocol.
 * 
 * @author bernd.rathmanner
 * 
 */
public class SocketByteSubscriberDispatcher extends SubscriberDispatcher<byte[]> {

    private int _port;
    private ServerSocket _server;

    @Override
    public void init() {
        try {
            _port = Integer.parseInt(MyxUtils.getInitProperties(this).getProperty("port", "6667"));
        } catch (NumberFormatException e) {
            // use default value
            _port = 6667;
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
    protected String getSubscriberEndpointClass() {
        return SocketByteSubscriberEndpoint.class.getName();
    }

}
