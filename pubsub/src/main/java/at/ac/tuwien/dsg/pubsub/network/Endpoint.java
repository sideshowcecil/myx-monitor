package at.ac.tuwien.dsg.pubsub.network;

import java.io.Closeable;
import java.io.IOException;

import at.ac.tuwien.dsg.pubsub.message.Message;

/**
 * Interface that specifies the methods of a real network endpoint.
 * 
 * @param <E>
 */
public interface Endpoint<E> extends Closeable {
    /**
     * Receive a message.
     * 
     * @return
     */
    public Message<E> receive() throws IOException;

    /**
     * Send a message.
     * 
     * @param msg
     */
    public void send(Message<E> msg) throws IOException;
}
