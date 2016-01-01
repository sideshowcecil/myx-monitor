package at.ac.tuwien.dsg.pubsub.middleware.comp.socket;

import java.net.Socket;

import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.myx.util.IpResolver;
import at.ac.tuwien.dsg.pubsub.middleware.comp.PublisherEndpoint;
import at.ac.tuwien.dsg.pubsub.network.socket.SocketByteMessageProtocol;

/**
 * Implementation of {@link PublisherEndpoint} based on the
 * SocketByteMessageProtocol.
 * 
 * @author bernd.rathmanner
 * 
 */
public class SocketBytePublisherEndpoint extends PublisherEndpoint<byte[]> {

    @Override
    protected String getExternalConnectionIdentifier() {
        if (endpoint instanceof SocketByteMessageProtocol) {
            Socket s = ((SocketByteMessageProtocol) endpoint).getSocket();
            // from,to
            return IdGenerator.generateConnectionIdentifier(s.getInetAddress().getHostAddress() + ":" + s.getPort()
                    + "," + IpResolver.getLocalIp(s) + ":" + s.getLocalPort());
        }
        return null;
    }

}
