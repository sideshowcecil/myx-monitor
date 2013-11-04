package at.ac.tuwien.infosys.pubsub.network;

import at.ac.tuwien.infosys.pubsub.message.Message;

/**
 * Interface to specify the message receiver methods.
 * 
 * @author bernd.rathmanner
 * 
 * @param <E>
 */
public interface MessageReceiver<E> {
    /**
     * Receive a message.
     * 
     * @return
     */
    public Message<E> receive();
}
