package at.ac.tuwien.infosys.pubsub.middleware.socket;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.message.Message.Type;
import at.ac.tuwien.infosys.pubsub.middleware.SubscriberHandler;
import at.ac.tuwien.infosys.pubsub.network.socket.SocketByteMessageProtocol;

/**
 * Implementation of SubscriberHandler based on the SocketByteMessageProtocol.
 * 
 * @author bernd.rathmanner
 * 
 */
public class SocketByteSubscriberHandler extends SubscriberHandler<byte[]> {

    private SocketByteMessageProtocol protocol;

    public SocketByteSubscriberHandler(SocketByteMessageProtocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public String waitForTopicName() {
        Message<byte[]> msg = protocol.receive();
        if (msg.getType() == Type.TOPIC) {
            return new String(msg.getData());
        }
        return null;
    }

    @Override
    public void sendErrorForNonExistingTopic() {
        Message<byte[]> msg = new Message<byte[]>("The given topic does not exist!".getBytes(), Type.ERROR);
        sendMessage(msg);
    }

    @Override
    public void sendMessage(Message<byte[]> msg) {
        protocol.send(msg);
    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub
    }

}
