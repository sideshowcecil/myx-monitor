package at.ac.tuwien.dsg.pubsub.message.topic;

import java.util.regex.Pattern;

/**
 * Implements {@link Topic} based on regular expressions.
 * 
 * @author bernd.rathmanner
 * 
 */
public class RegexTopic implements Topic {

    protected Pattern pattern;

    /**
     * Constructor.
     * 
     * @param pattern
     */
    public RegexTopic(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public boolean matches(String topic) {
        return pattern.matcher(topic).matches();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null && !(obj instanceof RegexTopic)) {
            return false;
        }
        return pattern.toString().equals(((RegexTopic) obj).pattern.toString());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() + pattern.hashCode();
    }

    @Override
    public String toString() {
        return "[" + getClass().getName() + "] " + pattern;
    }

}
