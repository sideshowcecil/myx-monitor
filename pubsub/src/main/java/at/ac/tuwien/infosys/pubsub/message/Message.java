package at.ac.tuwien.infosys.pubsub.message;

import java.io.Serializable;

/**
 * This class contains the data for messages sent over the PubSubMiddleware.
 * 
 * @author bernd.rathmanner
 * 
 * @param <E> resembles the message data.
 */
public class Message<E> implements Serializable {

    private static final long serialVersionUID = 1L;

    private E data;
    private Type type;

    /**
     * Basic constructor with predefined message type <code>DATA</code>.
     * 
     * @param data
     */
    public Message(E data) {
        this(data, Type.DATA);
    }

    /**
     * Constructor for both message data and message type.
     * 
     * @param data
     * @param type
     */
    public Message(E data, Type type) {
        this.data = data;
        this.type = type;
    }

    /**
     * Get the message data.
     * 
     * @return
     */
    public E getData() {
        return data;
    }

    /**
     * Get the message type.
     * 
     * @return
     */
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[" + type + "] " + data;
    }

    /**
     * All message types avaliable.
     * 
     * @author bernd.rathmanner
     * 
     */
    public enum Type {
        TOPIC, INIT, DATA, CLOSE, ERROR
    }
}
