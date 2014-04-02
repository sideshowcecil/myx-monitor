package at.ac.tuwien.dsg.pubsub.middleware.interfaces;

import at.ac.tuwien.dsg.pubsub.message.Message;

public interface ISubscriber<E> {
    /**
     * Send a message to the real subscriber.
     * 
     * @param message
     */
    public void send(Message<E> message);
}
