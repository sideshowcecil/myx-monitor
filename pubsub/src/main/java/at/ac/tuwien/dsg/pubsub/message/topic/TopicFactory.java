package at.ac.tuwien.dsg.pubsub.message.topic;

/**
 * Factory class to create {@link Topic} instances.
 * 
 * @author bernd.rathmanner
 * 
 */
public class TopicFactory {

    /**
     * Convenience method to create a {@link Topic} based on a glob pattern.
     * 
     * @param type
     * @param pattern
     * @return
     */
    public Topic create(String pattern) {
        return create(Type.GLOB, pattern);
    }

    /**
     * Convenience method to create a {@link Topic} based on the given
     * {@link Type}.
     * 
     * @param type
     * @param pattern
     * @return
     */
    public Topic create(Type type, String pattern) {
        switch (type) {
        case STRING:
            return new StringTopic(pattern);
        case GLOB:
            return new GlobTopic(pattern);
        case REGEX:
        default:
            return new RegexTopic(pattern);
        }
    }

    public static enum Type {
        STRING, REGEX, GLOB
    }
}
