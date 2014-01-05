package at.ac.tuwien.infosys.pubsub.middleware.arch.component.socket;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.message.Message.Type;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.SubscriberEndpoint;
import at.ac.tuwien.infosys.pubsub.middleware.arch.network.socket.SocketByteMessageProtocol;

/**
 * Implementation of {@link SubscriberEndpoint} based on the
 * {@link SocketByteMessageProtocol}.
 * 
 * @author bernd.rathmanner
 * 
 */
public class SocketByteSubscriberEndpoint extends SubscriberEndpoint<byte[]> {

    @Override
    public String waitForTopicName() {
        Message<byte[]> msg = _endpoint.receive();
        if (msg.getType() == Type.TOPIC) {
            return new String(msg.getData());
        }
        return null;
    }

    @Override
    public void sendErrorForNonExistingTopic() {
        Message<byte[]> msg = new Message<byte[]>("The given topic does not exist!".getBytes(), Type.ERROR);
        _endpoint.send(msg);
    }

}
