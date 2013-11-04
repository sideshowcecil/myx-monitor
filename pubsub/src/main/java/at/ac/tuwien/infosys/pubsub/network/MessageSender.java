package at.ac.tuwien.infosys.pubsub.network;

import at.ac.tuwien.infosys.pubsub.message.Message;

/**
 * Interface to specify message sender methods.
 * 
 * @author bernd.rathmanner
 * 
 * @param <E>
 */
public interface MessageSender<E> {
    /**
     * Send a message.
     * 
     * @param msg
     */
    public void send(Message<E> msg);
}
