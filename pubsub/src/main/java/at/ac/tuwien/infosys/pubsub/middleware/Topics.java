package at.ac.tuwien.infosys.pubsub.middleware;

import java.util.concurrent.ConcurrentHashMap;

/**
 * This static class is used to handle the registration of topics.
 * 
 * @author bernd.rathmanner
 * 
 * @param <E>
 *            resembles the message data.
 */
public final class Topics<E> {

    private static ConcurrentHashMap<String, PubSubHandler<?>> topics = new ConcurrentHashMap<>();

    /**
     * Add a publisher with the given topic name to the registrations. If we
     * topic is already registered an exception is thrown.
     * 
     * @param topicName
     * @param handler
     */
    public static <E> void add(String topicName, PubSubHandler<?> handler) {
        synchronized (topics) {
            if (!topics.containsKey(topicName)) {
                topics.put(topicName, handler);
            } else {
                throw new IllegalArgumentException("The topic '" + topicName
                        + "' is already used!");
            }
        }
    }

    /**
     * Get the corresponding PubSubHandler of a topic name.
     * 
     * @param topicName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E> PubSubHandler<E> get(String topicName) {
        PubSubHandler<?> handler = topics.get(topicName);
        if (handler != null) {
            try {
                return (PubSubHandler<E>) handler;
            } catch (ClassCastException e) {
            }
        }
        return null;
    }

    /**
     * Remove a topic from the registrations.
     * 
     * @param topicName
     */
    public static <E> void remove(String topicName) {
        topics.remove(topicName);
    }
}
