package at.ac.tuwien.infosys.pubsub.network;

import at.ac.tuwien.infosys.pubsub.message.Message;

public interface MessageSender<E> {
    public void send(Message<E> msg);
}
