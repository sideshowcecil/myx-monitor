package at.ac.tuwien.dsg.pubsub.message.topic;

public class StringTopic implements Topic {

    protected final String topic;

    /**
     * Constructor.
     * 
     * @param pattern
     */
    public StringTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public boolean matches(String topic) {
        return this.topic.equals(topic);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null && !(obj instanceof RegexTopic)) {
            return false;
        }
        return topic.equals(obj);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() + topic.hashCode();
    }

    @Override
    public String toString() {
        return "[" + getClass().getName() + "] " + topic;
    }
}
