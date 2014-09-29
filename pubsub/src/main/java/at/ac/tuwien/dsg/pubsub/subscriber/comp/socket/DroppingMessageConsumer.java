package at.ac.tuwien.dsg.pubsub.subscriber.comp.socket;

import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.subscriber.comp.MessageConsumer;

public class DroppingMessageConsumer extends MessageConsumer<byte[]> {

    @Override
    public void consume(Message<byte[]> msg) {
        // do something, but nothing productive
        switch (msg.getType()) {
        default:
            break;
        }
    }

}
