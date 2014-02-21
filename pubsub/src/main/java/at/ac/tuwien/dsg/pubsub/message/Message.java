package at.ac.tuwien.dsg.pubsub.message;

import java.io.Serializable;

/**
 * This class contains the data for messages sent over the PubSubMiddleware.
 * 
 * @author bernd.rathmanner
 * 
 * @param <E>
 *            resembles the message data.
 */
public class Message<E> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Type type;
    private String topic;
    private E data;

    /**
     * Basic constructor with predefined message type <code>DATA</code>.
     * 
     * @param data
     */
    public Message(String topic, E data) {
        this(Type.DATA, topic, data);
    }

    /**
     * Constructor for both message data and message type.
     * 
     * @param data
     * @param type
     */
    public Message(Type type, String topic, E data) {
        this.topic = topic;
        this.type = type;
        this.data = data;
    }

    /**
     * Get the message type.
     * 
     * @return
     */
    public Type getType() {
        return type;
    }

    /**
     * Get the message topic.
     * 
     * @return
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Get the message data.
     * 
     * @return
     */
    public E getData() {
        return data;
    }

    @Override
    public String toString() {
        return "[" + getType() + "] [" + getTopic() + "] " + getData();
    }

    /**
     * All message types available.
     * 
     * @author bernd.rathmanner
     * 
     */
    public enum Type {
        TOPIC, INIT, DATA, CLOSE, ERROR
    }
}
