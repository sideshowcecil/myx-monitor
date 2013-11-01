package at.ac.tuwien.infosys.pubsub;

import at.ac.tuwien.infosys.pubsub.message.Message;

public interface Subscriber {
    public void send(Message<?> msg);
}
