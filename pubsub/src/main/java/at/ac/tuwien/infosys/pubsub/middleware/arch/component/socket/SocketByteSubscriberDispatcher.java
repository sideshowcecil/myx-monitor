package at.ac.tuwien.infosys.pubsub.middleware.arch.component.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import edu.uci.isr.myx.fw.MyxUtils;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.SubscriberDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.network.Endpoint;
import at.ac.tuwien.infosys.pubsub.middleware.arch.network.socket.SocketByteMessageProtocol;

/**
 * Implementation of SubscriberListener based on the SocketByteMessageProtocol.
 * 
 * @author bernd.rathmanner
 * 
 */
public class SocketByteSubscriberDispatcher extends SubscriberDispatcher<byte[]> {

    private int _port;
    private ServerSocket _server;

    public SocketByteSubscriberDispatcher(int port) {
        this._port = port;
        _server = null;
    }

	@Override
	public void init() {
		try {
			_port = Integer.parseInt(MyxUtils.getInitProperties(this)
					.getProperty("port", "6667"));
		} catch (NumberFormatException e) {
			// use default value
			_port = 6667;
		}
		super.init();
	}

    @Override
    public Endpoint<byte[]> waitForNewEndpoint() {
        if (_server == null) {
            // open socket
            try {
                _server = new ServerSocket(_port);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (_server != null) {
            try {
                // wait for a new connection
                Socket socket = _server.accept();
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
