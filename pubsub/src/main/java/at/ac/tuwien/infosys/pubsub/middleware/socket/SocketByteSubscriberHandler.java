package at.ac.tuwien.infosys.pubsub.middleware.socket;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.message.Message.Type;
import at.ac.tuwien.infosys.pubsub.middleware.SubscriberHandler;
import at.ac.tuwien.infosys.pubsub.network.socket.SocketByteMessageReceiver;
import at.ac.tuwien.infosys.pubsub.network.socket.SocketByteMessageSender;

public class SocketByteSubscriberHandler extends SubscriberHandler<byte[]> {

    private SocketByteMessageReceiver receiver;
    private SocketByteMessageSender sender;

    public SocketByteSubscriberHandler(SocketByteMessageReceiver receiver,
            SocketByteMessageSender sender) {
        this.receiver = receiver;
        this.sender = sender;
    }

    @Override
    public String waitForTopicName() {
        Message<byte[]> msg = receiver.receive();
        if (msg.getType() == Type.TOPIC) {
            return new String(msg.getData());
        }
        return null;
    }

    @Override
    public void sendErrorForNonExistingTopic() {
        Message<byte[]> msg = new Message<byte[]>(
                "The given topic does not exist!".getBytes(), Type.ERROR);
        sendMessage(msg);
    }

    @Override
    public void sendMessage(Message<byte[]> msg) {
        sender.send(msg);
    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub
    }

}
