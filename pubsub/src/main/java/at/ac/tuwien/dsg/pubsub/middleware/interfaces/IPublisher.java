package at.ac.tuwien.dsg.pubsub.middleware.interfaces;

import at.ac.tuwien.dsg.pubsub.message.Message;

public interface IPublisher<E> {
    /**
     * Publish a message..
     * 
     * @param message
     */
    public void publish(Message<E> message);
}
