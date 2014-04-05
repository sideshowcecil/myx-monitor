package at.ac.tuwien.dsg.pubsub.subscriber.interfaces;

import at.ac.tuwien.dsg.pubsub.message.Message;

public interface ISubscriber<E> {
    /**
     * Consume a received message.
     */
    public void consume(Message<E> msg);
}
