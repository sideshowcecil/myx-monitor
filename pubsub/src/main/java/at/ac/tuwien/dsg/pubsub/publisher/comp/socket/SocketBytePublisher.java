package at.ac.tuwien.dsg.pubsub.publisher.comp.socket;

import java.io.IOException;
import java.net.Socket;

import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.network.socket.SocketByteMessageProtocol;
import at.ac.tuwien.dsg.pubsub.publisher.comp.Publisher;

public class SocketBytePublisher extends Publisher<byte[]> {

    private String host;
    private int port;

    @Override
    public void init() {
        host = MyxMonitoringUtils.getInitProperties(this).getProperty("hostname", "localhost");
        try {
            port = Integer.parseInt(MyxMonitoringUtils.getInitProperties(this).getProperty("port", "6666"));
        } catch (NumberFormatException e) {
            // use default value
            port = 6666;
        }
    }

    @Override
    protected void connect() {
        try {
            Socket s = new Socket(host, port);
            endpoint = new SocketByteMessageProtocol(s);
        } catch (IOException e) {
        }
    }

    @Override
    protected String getExternalConnectionIdentifier() {
        if (endpoint instanceof SocketByteMessageProtocol) {
            Socket s = ((SocketByteMessageProtocol) endpoint).getSocket();
            // from,to
            return IdGenerator.generateConnectionIdentifier(s.getInetAddress().getHostAddress() + ":" + s.getPort()
                    + "," + s.getLocalAddress().getHostAddress() + ":" + s.getLocalPort());
        }
        return null;
    }

}
