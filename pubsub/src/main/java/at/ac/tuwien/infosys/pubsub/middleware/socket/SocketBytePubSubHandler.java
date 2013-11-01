package at.ac.tuwien.infosys.pubsub.middleware.socket;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.message.Message.Type;
import at.ac.tuwien.infosys.pubsub.middleware.PubSubHandler;
import at.ac.tuwien.infosys.pubsub.network.MessageReceiver;
import at.ac.tuwien.infosys.pubsub.network.MessageSender;

public class SocketBytePubSubHandler extends PubSubHandler<byte[]> {

    public SocketBytePubSubHandler(MessageReceiver<byte[]> receiver, MessageSender<byte[]> sender) {
        super(receiver, sender);
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
    public void sendErrorForExistingTopic() {
        Message<byte[]> msg = new Message<byte[]>(
                "The given topic already exists!".getBytes(), Type.ERROR);
        sender.send(msg);
    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub
    }

}
