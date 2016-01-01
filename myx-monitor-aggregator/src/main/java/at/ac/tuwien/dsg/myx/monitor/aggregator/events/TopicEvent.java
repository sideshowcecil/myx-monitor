package at.ac.tuwien.dsg.myx.monitor.aggregator.events;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.pubsub.message.topic.TopicFactory.Type;

public class TopicEvent extends Event {

    private static final long serialVersionUID = -6876793802543500140L;

    private final Type type;
    private final List<String> topics;

    public TopicEvent() {
        topics = new ArrayList<>();
        type = Type.GLOB;
    }

    public TopicEvent(Type type, List<String> topics) {
        this.type = type;
        this.topics = topics;
    }

    public List<String> getTopics() {
        return topics;
    }

    public Type getType() {
        return type;
    }

}
