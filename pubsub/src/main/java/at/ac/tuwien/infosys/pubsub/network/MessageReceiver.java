package at.ac.tuwien.infosys.pubsub.network;

import at.ac.tuwien.infosys.pubsub.message.Message;

public interface MessageReceiver<E> {
    public Message<E> receive();
}
