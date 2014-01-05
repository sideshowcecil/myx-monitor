package at.ac.tuwien.infosys.pubsub.middleware.arch.component.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import edu.uci.isr.myx.fw.MyxUtils;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.PublisherDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.network.Endpoint;
import at.ac.tuwien.infosys.pubsub.middleware.arch.network.socket.SocketByteMessageProtocol;

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

	public SocketBytePublisherDispatcher(int port) {
		this._port = port;
		_server = null;
	}

	@Override
	public void init() {
		try {
			_port = Integer.parseInt(MyxUtils.getInitProperties(this)
					.getProperty("port", "6666"));
		} catch (NumberFormatException e) {
			// use default value
			_port = 6666;
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
