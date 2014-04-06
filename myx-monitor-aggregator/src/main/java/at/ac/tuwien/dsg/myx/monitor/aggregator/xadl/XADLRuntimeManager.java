package at.ac.tuwien.dsg.myx.monitor.aggregator.xadl;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.dsg.myx.monitor.aggregator.model.ModelRoot;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLExternalLinkEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLLinkEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLRuntimeEvent;
import at.ac.tuwien.dsg.myx.util.EventUtils;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Topic;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.ISubscriber;

public class XADLRuntimeManager implements ISubscriber<Event> {
    
    private final ModelRoot modelRoot;
    private final List<Topic> topics;
    
    public XADLRuntimeManager(ModelRoot modelRoot) {
        this.modelRoot = modelRoot;
        topics = new ArrayList<>();
        topics.add(new Topic(EventUtils.getTopic(XADLEvent.class)));
        topics.add(new Topic(EventUtils.getTopic(XADLExternalLinkEvent.class)));
        topics.add(new Topic(EventUtils.getTopic(XADLLinkEvent.class)));
        topics.add(new Topic(EventUtils.getTopic(XADLRuntimeEvent.class)));
    }

    @Override
    public void consume(Message<Event> message) {
        if (matches(message.getTopic())) {
            System.err.println(getClass().getName() + ": " + message.getData());
        }
    }

    /**
     * Return if the given topic matches any of the subscribed topics.
     * 
     * @param topic
     * @return
     */
    private boolean matches(String topic) {
        if (topics != null) {
            for (Topic t : topics) {
                if (t.matches(topic)) {
                    return true;
                }
            }
        }
        return false;
    }

}
