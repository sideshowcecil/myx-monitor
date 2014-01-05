package at.ac.tuwien.infosys.pubsub.middleware.arch.network;

import at.ac.tuwien.infosys.pubsub.message.Message;

/**
 * Interface that specifies the methods of a real network endpoint.
 * 
 * @author bernd.rathmanner
 * 
 * @param <E>
 */
public interface Endpoint<E> {
    /**
     * Receive a message.
     * 
     * @return
     */
    public Message<E> receive();

    /**
     * Send a message.
     * 
     * @param msg
     */
    public void send(Message<E> msg);
}
