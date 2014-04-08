package at.ac.tuwien.dsg.myx.monitor.aggregator.hosted;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import at.ac.tuwien.dsg.pubsub.message.Topic;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.ISubscriber;
import edu.uci.isr.xarch.hostproperty.IElementRef;
import edu.uci.isr.xarch.hostproperty.IHost;
import edu.uci.isr.xarch.hostproperty.IHostedArchStructure;
import edu.uci.isr.xarch.hostproperty.IProperty;

public class HostedRuntimeManager implements ISubscriber<Event> {

    private static final Logger logger = LoggerFactory.getLogger(HostedRuntimeManager.class);

    private final Map<String, IElementRef> elementRefIndex = new HashMap<>();

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
            logger.info("Consuming event of type " + message.getData().getClass());
            Event event = message.getData();
            if (event instanceof XADLHostingEvent) {
                process((XADLHostingEvent) event);
            } else if (event instanceof XADLHostInstanceEvent) {
                process((XADLHostInstanceEvent) event);
            } else if (event instanceof XADLHostPropertyEvent) {
                process((XADLHostPropertyEvent) event);
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
        IHostedArchStructure hostedArchStructure = modelRoot.getHostedArchStructure(event.getArchitectureRuntimeId());
        IHost host = DBLUtils.getOrCreateHost(hostedArchStructure, event.getHostId(),
                modelRoot.getHostpropertyContext());

        switch (event.getXadlEventType()) {
        case ADD:
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
            for (String id : event.getHostedComponentIds()) {
                IElementRef ref = elementRefIndex.get(id);
                host.removeHostsComponent(ref);
            }
            for (String id : event.getHostedConnectorIds()) {
                IElementRef ref = elementRefIndex.get(id);
                host.removeHostsConnector(ref);
            }
            for (String id : event.getHostedGroupIds()) {
                IElementRef ref = elementRefIndex.get(id);
                host.removeHostsGroup(ref);
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
        IHostedArchStructure hostedArchStructure = modelRoot.getHostedArchStructure(event.getArchitectureRuntimeId());
        IHost host = DBLUtils.getOrCreateHost(hostedArchStructure, event.getHostId(),
                modelRoot.getHostpropertyContext());

        switch (event.getXadlEventType()) {
        case ADD:
            host.setDescription(DBLUtils.createDescription(event.getDescription(), modelRoot.getHostpropertyContext()));
            break;
        case REMOVE:
            // we only remove the host if it is empty
            if (host.getAllHostPropertys().isEmpty() && host.getAllHostsComponents().isEmpty()
                    && host.getAllHostsConnectors().isEmpty() && host.getAllHostsGroups().isEmpty()
                    && host.getAllSubhosts().isEmpty()) {
                hostedArchStructure.removeHost(host);
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
        IHostedArchStructure hostedArchStructure = modelRoot.getHostedArchStructure(event.getArchitectureRuntimeId());
        IHost host = DBLUtils.getOrCreateHost(hostedArchStructure, event.getHostId(),
                modelRoot.getHostpropertyContext());

        switch (event.getXadlEventType()) {
        case ADD:
        case UPDATE:
            Set<String> stringNames = event.getHostProperties().stringPropertyNames();
            Set<Entry<Object, Object>> entryset = event.getHostProperties().entrySet();
            for (Entry<Object, Object> entry : event.getHostProperties().entrySet()) {
                String name = entry.getKey().toString();
                String value = entry.getValue().toString();

                IProperty prop = DBLUtils.getHostProperty(host, name);
                if (prop == null) {
                    prop = DBLUtils.createHostProperty(name, modelRoot.getHostpropertyContext());
                    host.addHostProperty(prop);
                }
                prop.clearValues();
                prop.addValue(DBLUtils.createDescription(value, modelRoot.getHostpropertyContext()));
            }
            stringNames.getClass();
            entryset.getClass();
            break;
        case REMOVE:
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
