package at.ac.tuwien.dsg.pubsub.publisher.comp.socket;

import java.io.IOException;
import java.net.Socket;

import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.myx.util.IpResolver;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.network.Endpoint;
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
    protected Endpoint<byte[]> connect() {
        try {
            Socket s = new Socket(host, port);
            return new SocketByteMessageProtocol(s);
        } catch (IOException e) {
        }
        return null;
    }

    @Override
    protected String getExternalConnectionIdentifier() {
        if (endpoint instanceof SocketByteMessageProtocol) {
            Socket s = ((SocketByteMessageProtocol) endpoint).getSocket();
            // from,to
            return IdGenerator.generateConnectionIdentifier(IpResolver.getLocalIp(s) + ":" + s.getLocalPort() + ","
                    + s.getInetAddress().getHostAddress() + ":" + s.getPort());
        }
        return null;
    }

}
