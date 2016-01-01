package at.ac.tuwien.dsg.myx.monitor.aggregator.hosted;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.dsg.myx.monitor.aggregator.model.ModelRoot;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostInstanceEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostPropertyEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostingEvent;
import at.ac.tuwien.dsg.myx.util.DBLUtils;
import at.ac.tuwien.dsg.myx.util.EventUtils;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.topic.Topic;
import at.ac.tuwien.dsg.pubsub.message.topic.TopicFactory;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.ISubscriber;
import edu.uci.isr.xarch.hostproperty.IElementRef;
import edu.uci.isr.xarch.hostproperty.IHost;
import edu.uci.isr.xarch.hostproperty.IHostedArchInstance;
import edu.uci.isr.xarch.hostproperty.IProperty;

public class HostedInstanceRuntimeManager implements ISubscriber<Event> {

    private static final Logger logger = LoggerFactory.getLogger(HostedInstanceRuntimeManager.class);

    private final Map<String, IElementRef> elementRefIndex = new HashMap<>();

    private final ModelRoot modelRoot;
    private final Set<Topic> topics = new LinkedHashSet<>();

    public HostedInstanceRuntimeManager(ModelRoot modelRoot) {
        this.modelRoot = modelRoot;
        topics.add(new TopicFactory().create(EventUtils.getTopicPattern(XADLHostEvent.class)));
    }

    @Override
    public void consume(Message<Event> message) {
        if (matches(message.getTopic())) {
            logger.info("Consuming event of type " + message.getData().getClass());
            synchronized (modelRoot) {
                try {
                    Event event = message.getData();
                    if (event instanceof XADLHostingEvent) {
                        process((XADLHostingEvent) event);
                    } else if (event instanceof XADLHostInstanceEvent) {
                        process((XADLHostInstanceEvent) event);
                    } else if (event instanceof XADLHostPropertyEvent) {
                        process((XADLHostPropertyEvent) event);
                    }
                } catch (Exception e) {
                    logger.warn("An unexpected error occured on message " + message, e);
                }
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
        for (Topic t : topics) {
            if (t.matches(topic)) {
                return true;
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
        IHostedArchInstance instance = modelRoot.getHostedArchInstance(event.getArchitectureRuntimeId());
        IHost host = DBLUtils.createHost(instance, event.getHostId(), modelRoot.getHostpropertyContext());

        switch (event.getXadlEventType()) {
        case ADD:
            logger.info("Adding components/connectors to host " + event.getHostId());
            for (String id : event.getHostedComponentIds()) {
                IElementRef ref = DBLUtils.createElementRef(id, modelRoot.getHostpropertyContext());
                elementRefIndex.put(id, ref);
                host.addHostsComponent(ref);
            }
            for (String id : event.getHostedConnectorIds()) {
                IElementRef ref = DBLUtils.createElementRef(id, modelRoot.getHostpropertyContext());
                elementRefIndex.put(id, ref);
                host.addHostsConnector(ref);
            }
            for (String id : event.getHostedGroupIds()) {
                IElementRef ref = DBLUtils.createElementRef(id, modelRoot.getHostpropertyContext());
                elementRefIndex.put(id, ref);
                host.addHostsGroup(ref);
            }
            // subhosts are currently not supported
            break;
        case REMOVE:
            logger.info("Removing components/connectors from host " + event.getHostId());
            for (String id : event.getHostedComponentIds()) {
                if (elementRefIndex.containsKey(id)) {
                    IElementRef ref = elementRefIndex.get(id);
                    host.removeHostsComponent(ref);
                }
            }
            for (String id : event.getHostedConnectorIds()) {
                if (elementRefIndex.containsKey(id)) {
                    IElementRef ref = elementRefIndex.get(id);
                    host.removeHostsConnector(ref);
                }
            }
            for (String id : event.getHostedGroupIds()) {
                if (elementRefIndex.containsKey(id)) {
                    IElementRef ref = elementRefIndex.get(id);
                    host.removeHostsGroup(ref);
                }
            }
            // subhosts are currently not supported
            break;
        case UPDATE:
        default:
            // not supported
            break;
        }
    }

    /**
     * Process a {@link XADLHostInstanceEvent}.
     * 
     * @param event
     */
    private void process(XADLHostInstanceEvent event) {
        IHostedArchInstance instance = modelRoot.getHostedArchInstance(event.getArchitectureRuntimeId());
        IHost host = DBLUtils.createHost(instance, event.getHostId(), modelRoot.getHostpropertyContext());

        switch (event.getXadlEventType()) {
        case ADD:
            logger.info("Adding host " + event.getHostId());
            host.setDescription(DBLUtils.createDescription(event.getDescription(), modelRoot.getHostpropertyContext()));
            break;
        case REMOVE:
            logger.info("Removing host " + event.getHostId());
            // we only remove the host if it is empty
            if (host.getAllHostsComponents().isEmpty() && host.getAllHostsConnectors().isEmpty()
                    && host.getAllHostsGroups().isEmpty() && host.getAllSubhosts().isEmpty()) {
                instance.removeHost(host);
            }
            break;
        case UPDATE:
        default:
            // not supported
            break;
        }
    }

    /**
     * Process a {@link XADLHostPropertyEvent}.
     * 
     * @param event
     */
    private void process(XADLHostPropertyEvent event) {
        IHostedArchInstance instance = modelRoot.getHostedArchInstance(event.getArchitectureRuntimeId());
        IHost host = DBLUtils.createHost(instance, event.getHostId(), modelRoot.getHostpropertyContext());

        switch (event.getXadlEventType()) {
        case ADD:
            logger.info("Adding hostproperties for host " + event.getHostId());
            for (Entry<Object, Object> entry : event.getHostProperties().entrySet()) {
                String name = entry.getKey().toString();

                // get or create property
                IProperty prop = DBLUtils.getHostProperty(host, name);
                if (prop == null) {
                    prop = DBLUtils.createHostProperty(name, modelRoot.getHostpropertyContext());
                    host.addHostProperty(prop);
                }

                // add values
                if (entry.getValue() instanceof Iterable<?>) {
                    for (Object o : (Iterable<?>) entry.getValue()) {
                        prop.addValue(DBLUtils.createDescription(o.toString(), modelRoot.getHostpropertyContext()));
                    }
                } else {
                    prop.addValue(DBLUtils.createDescription(entry.getValue().toString(),
                            modelRoot.getHostpropertyContext()));
                }
            }
            break;
        case UPDATE:
            logger.info("Updating hostproperties for host " + event.getHostId());
            for (Entry<Object, Object> entry : event.getHostProperties().entrySet()) {
                String name = entry.getKey().toString();

                // get or create property
                IProperty prop = DBLUtils.getHostProperty(host, name);
                if (prop == null) {
                    prop = DBLUtils.createHostProperty(name, modelRoot.getHostpropertyContext());
                    host.addHostProperty(prop);
                }

                // update values
                prop.clearValues();
                if (entry.getValue() instanceof Iterable<?>) {
                    for (Object o : (Iterable<?>) entry.getValue()) {
                        prop.addValue(DBLUtils.createDescription(o.toString(), modelRoot.getHostpropertyContext()));
                    }
                } else {
                    prop.addValue(DBLUtils.createDescription(entry.getValue().toString(),
                            modelRoot.getHostpropertyContext()));
                }
            }
            break;
        case REMOVE:
            logger.info("Removing hostproperties from host " + event.getHostId());
            for (Entry<Object, Object> entry : event.getHostProperties().entrySet()) {
                String name = entry.getKey().toString();

                IProperty prop = DBLUtils.getHostProperty(host, name);
                if (prop != null) {
                    host.removeHostProperty(prop);
                }
            }
            break;
        default:
            // not supported
            break;
        }
    }

}
