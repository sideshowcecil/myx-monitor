package at.ac.tuwien.dsg.myx.monitor.aggregator.hosted;

import java.util.ArrayList;
import java.util.List;

import edu.uci.isr.xarch.hostproperty.IHostedArchStructure;
import at.ac.tuwien.dsg.myx.monitor.aggregator.model.ModelRoot;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostInstanceEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostPropertyEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostingEvent;
import at.ac.tuwien.dsg.myx.util.EventUtils;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Topic;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.ISubscriber;

public class HostedRuntimeManager implements ISubscriber<Event> {

    private final ModelRoot modelRoot;
    private final List<Topic> topics;

    public HostedRuntimeManager(ModelRoot modelRoot) {
        this.modelRoot = modelRoot;
        topics = new ArrayList<>();
        topics.add(new Topic(EventUtils.getTopicPattern(XADLHostEvent.class)));
    }

    @Override
    public void consume(Message<Event> message) {
        if (matches(message.getTopic())) {
            Event event = message.getData();
            if (event instanceof XADLHostingEvent) {
                process((XADLHostingEvent)event);
            } else if (event instanceof XADLHostInstanceEvent) {
                process((XADLHostInstanceEvent)event);
            } else if (event instanceof XADLHostPropertyEvent) {
                process((XADLHostPropertyEvent)event);
            }
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

    /**
     * Process a {@link XADLHostingEvent}.
     * 
     * @param event
     */
    private void process(XADLHostingEvent event) {

    }

    /**
     * Process a {@link XADLHostInstanceEvent}.
     * 
     * @param event
     */
    private void process(XADLHostInstanceEvent event) {

    }

    /**
     * Process a {@link XADLHostPropertyEvent}.
     * 
     * @param event
     */
    private void process(XADLHostPropertyEvent event) {

    }

}
