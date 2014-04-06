package at.ac.tuwien.dsg.pubsub.middleware.interfaces;

import at.ac.tuwien.dsg.pubsub.message.Message;

public interface ISubscriber<E> {
    /**
     * Consume a received message.
     * 
     * @param message
     */
    public void consume(Message<E> message);
}
