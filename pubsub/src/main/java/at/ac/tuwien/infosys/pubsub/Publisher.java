package at.ac.tuwien.infosys.pubsub;

import at.ac.tuwien.infosys.pubsub.message.Message;

/**
 * TODO publisher interface
 * 
 * @author bernd.rathmanner
 * 
 */
public interface Publisher {
    public void send(Message<?> msg);
}
