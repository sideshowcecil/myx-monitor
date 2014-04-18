package at.ac.tuwien.dsg.myx.monitor.aggregator.events;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.pubsub.message.Topic;
import at.ac.tuwien.dsg.pubsub.message.Topic.Type;

public class TopicEvent extends Event {

    private static final long serialVersionUID = -6876793802543500140L;

    private final Topic.Type type;
    private final List<String> topics;

    public TopicEvent() {
        topics = new ArrayList<>();
        type = Type.GLOB;
    }

    public TopicEvent(Topic.Type type, List<String> topics) {
        this.type = type;
        this.topics = topics;
    }

    public List<String> getTopics() {
        return topics;
    }

    public Topic.Type getType() {
        return type;
    }

}
