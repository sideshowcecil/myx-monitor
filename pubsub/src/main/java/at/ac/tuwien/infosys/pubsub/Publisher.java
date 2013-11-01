package at.ac.tuwien.infosys.pubsub;

import at.ac.tuwien.infosys.pubsub.message.Message;

public interface Publisher {
    public Message<?> receive();
}
