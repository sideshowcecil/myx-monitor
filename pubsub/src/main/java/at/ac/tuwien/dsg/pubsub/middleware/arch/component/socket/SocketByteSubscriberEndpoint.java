package at.ac.tuwien.dsg.pubsub.middleware.arch.component.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Topic;
import at.ac.tuwien.dsg.pubsub.middleware.arch.component.SubscriberEndpoint;
import at.ac.tuwien.dsg.pubsub.middleware.arch.network.socket.SocketByteMessageProtocol;

/**
 * Implementation of {@link SubscriberEndpoint} based on the
 * {@link SocketByteMessageProtocol}.
 * 
 * @author bernd.rathmanner
 * 
 */
public class SocketByteSubscriberEndpoint extends SubscriberEndpoint<byte[]> {

    @Override
    public List<Topic> getTopics() {
        try {
            Message<byte[]> msg = endpoint.receive();
            if (msg.getType() == Message.Type.TOPIC) {
                Topic.Type type = Topic.Type.valueOf(msg.getTopic());
                List<Topic> topics = new ArrayList<>();
                for (String topic : new String(msg.getData())
                        .split(String.valueOf(SocketByteMessageProtocol.SEPARATOR))) {
                    topics.add(new Topic(topic, type));
                }
                return topics;
            }
        } catch (IOException | IllegalArgumentException e) {
        }
        return null;
    }
}
