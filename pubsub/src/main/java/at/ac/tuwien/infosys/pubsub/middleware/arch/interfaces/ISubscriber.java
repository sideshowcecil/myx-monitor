package at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces;

import at.ac.tuwien.infosys.pubsub.message.Message;

public interface ISubscriber<E> {
    /**
     * Send a message to the real subscriber.
     * 
     * @param message
     */
    public void send(Message<E> message);
}
