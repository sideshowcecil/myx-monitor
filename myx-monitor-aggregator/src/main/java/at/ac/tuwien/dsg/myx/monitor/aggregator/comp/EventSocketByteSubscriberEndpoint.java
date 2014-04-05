package at.ac.tuwien.dsg.myx.monitor.aggregator.comp;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.dsg.myx.monitor.aggregator.network.EventSocketByteMessageProtocol;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Topic;
import at.ac.tuwien.dsg.pubsub.middleware.comp.SubscriberEndpoint;
import at.ac.tuwien.dsg.pubsub.network.socket.SocketByteMessageProtocol;

/**
 * Implementation of {@link SubscriberEndpoint} based on the
 * {@link SocketByteMessageProtocol}.
 * 
 * @author bernd.rathmanner
 * 
 */
public class EventSocketByteSubscriberEndpoint extends SubscriberEndpoint<Event> {

    @Override
    public List<Topic> getTopics() {
        try {
            Message<Event> msg = endpoint.receive();
            if (msg.getType() == Message.Type.TOPIC) {
                Topic.Type type = Topic.Type.valueOf(msg.getTopic());
                List<Topic> topics = new ArrayList<>();
                for (String topic : msg.getTopic()
                        .split(String.valueOf(EventSocketByteMessageProtocol.TOPIC_SEPERATOR))) {
                    topics.add(new Topic(topic, type));
                }
                return topics;
            }
        } catch (IOException | IllegalArgumentException e) {
        }
        return null;
    }

    @Override
    protected String getExternalConnectionIdentifier() {
        if (endpoint instanceof EventSocketByteMessageProtocol) {
            Socket s = ((EventSocketByteMessageProtocol) endpoint).getSocket();
            // from,to
            return IdGenerator.generateConnectionIdentifier(s.getLocalAddress().getHostAddress() + ":"
                    + s.getLocalPort() + "," + s.getInetAddress().getHostAddress() + ":" + s.getPort());
        }
        return null;
    }
}
