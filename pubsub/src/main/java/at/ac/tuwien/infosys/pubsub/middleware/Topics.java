package at.ac.tuwien.infosys.pubsub.middleware;

import java.util.concurrent.ConcurrentHashMap;

public final class Topics<E> {

    private static ConcurrentHashMap<String, PubSubHandler<?>> topics = new ConcurrentHashMap<>();

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

    public static <E> void remove(String topicName) {
        topics.remove(topicName);
    }
}
