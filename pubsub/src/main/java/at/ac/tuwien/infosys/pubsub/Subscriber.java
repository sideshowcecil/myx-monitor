package at.ac.tuwien.infosys.pubsub;

import at.ac.tuwien.infosys.pubsub.message.Message;

/**
 * TODO subscriber interface
 * 
 * @author bernd.rathmanner
 * 
 */
public interface Subscriber {
    public Message<?> receive();
}
