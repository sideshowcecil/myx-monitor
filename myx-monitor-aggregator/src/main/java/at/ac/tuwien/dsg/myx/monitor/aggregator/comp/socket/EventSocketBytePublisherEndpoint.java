package at.ac.tuwien.dsg.myx.monitor.aggregator.comp.socket;

import java.net.Socket;

import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.myx.util.IpResolver;
import at.ac.tuwien.dsg.pubsub.middleware.comp.PublisherEndpoint;
import at.ac.tuwien.dsg.pubsub.network.socket.EventSocketByteMessageProtocol;

/**
 * Implementation of {@link PublisherEndpoint} based on the
 * SocketByteMessageProtocol.
 * 
 * @author bernd.rathmanner
 * 
 */
public class EventSocketBytePublisherEndpoint extends PublisherEndpoint<Event> {

    @Override
    protected String getExternalConnectionIdentifier() {
        if (endpoint instanceof EventSocketByteMessageProtocol) {
            Socket s = ((EventSocketByteMessageProtocol) endpoint).getSocket();
            // from,to
            return IdGenerator.generateConnectionIdentifier(s.getInetAddress().getHostAddress() + ":" + s.getPort()
                    + "," + IpResolver.getLocalIp(s) + ":" + s.getLocalPort());
        }
        return null;
    }

}
