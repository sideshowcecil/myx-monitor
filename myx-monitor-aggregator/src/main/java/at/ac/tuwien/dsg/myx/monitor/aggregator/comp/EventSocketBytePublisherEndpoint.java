package at.ac.tuwien.dsg.myx.monitor.aggregator.comp;

import java.net.Socket;

import at.ac.tuwien.dsg.myx.monitor.aggregator.network.EventSocketByteMessageProtocol;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.pubsub.middleware.comp.PublisherEndpoint;

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
                    + "," + s.getLocalAddress().getHostAddress() + ":" + s.getLocalPort());
        }
        return null;
    }

}
