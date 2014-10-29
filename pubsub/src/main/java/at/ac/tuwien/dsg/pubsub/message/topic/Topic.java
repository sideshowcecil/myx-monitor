package at.ac.tuwien.dsg.pubsub.message.topic;

/**
 * Interface providing the methods used to match a topic pattern.
 */
public interface Topic {
    /**
     * Return if the given topic matches the pattern.
     * 
     * @param topic
     * @return
     */
    public abstract boolean matches(String topic);
}
