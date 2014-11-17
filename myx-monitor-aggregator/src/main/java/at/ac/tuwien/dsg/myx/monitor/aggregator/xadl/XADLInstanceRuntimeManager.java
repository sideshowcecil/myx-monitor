package at.ac.tuwien.dsg.myx.monitor.aggregator.xadl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.dsg.myx.monitor.aggregator.model.ModelRoot;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLExternalLinkEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLLinkEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLRuntimeEvent;
import at.ac.tuwien.dsg.myx.util.DBLUtils;
import at.ac.tuwien.dsg.myx.util.EventUtils;
import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.myx.util.Tuple;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.topic.Topic;
import at.ac.tuwien.dsg.pubsub.message.topic.TopicFactory;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.ISubscriber;
import edu.uci.isr.myx.fw.EMyxInterfaceDirection;
import edu.uci.isr.xarch.instance.IArchInstance;
import edu.uci.isr.xarch.instance.IComponentInstance;
import edu.uci.isr.xarch.instance.IConnectorInstance;
import edu.uci.isr.xarch.instance.ILinkInstance;
import edu.uci.isr.xarch.instance.IPoint;
import edu.uci.isr.xarch.instancemapping.IMappedComponentInstance;
import edu.uci.isr.xarch.instancemapping.IMappedConnectorInstance;
import edu.uci.isr.xarch.instancemapping.IMappedInterfaceInstance;
import edu.uci.isr.xarch.types.IArchStructure;
import edu.uci.isr.xarch.types.IComponent;
import edu.uci.isr.xarch.types.IConnector;
import edu.uci.isr.xarch.types.IInterface;

public class XADLInstanceRuntimeManager implements ISubscriber<Event> {

    private static final Logger logger = LoggerFactory.getLogger(XADLInstanceRuntimeManager.class);

    private final Map<IMappedInterfaceInstance, String> interface2element = new HashMap<>();

    /**
     * Mapping of component/connector to the connected link identifiers.
     */
    private final Map<String, List<Tuple<String, String>>> element2Link = new HashMap<>();

    /**
     * Mapping of link identifier (source interface and destination interface)
     * to the real link.
     */
    private final Map<Tuple<String, String>, ILinkInstance> links = new HashMap<>();

    /**
     * Mapping of external connection identifiers to the connected interfaces.
     */
    private final Map<String, List<IMappedInterfaceInstance>> externalConnections = new HashMap<>();

    private final ModelRoot modelRoot;
    private final List<Topic> topics;

    public XADLInstanceRuntimeManager(ModelRoot modelRoot) {
        this.modelRoot = modelRoot;
        TopicFactory factory = new TopicFactory();
        topics = new ArrayList<>();
        topics.add(factory.create(EventUtils.getTopicPattern(XADLEvent.class)));
        topics.add(factory.create(EventUtils.getTopicPattern(XADLExternalLinkEvent.class)));
        topics.add(factory.create(EventUtils.getTopicPattern(XADLLinkEvent.class)));
        topics.add(factory.create(EventUtils.getTopicPattern(XADLRuntimeEvent.class)));
    }

    @Override
    public void consume(Message<Event> message) {
        if (matches(message.getTopic())) {
            logger.info("Consuming event of type " + message.getData().getClass());
            synchronized (modelRoot) {
                try {
                    Event event = message.getData();
                    if (event instanceof XADLEvent) {
                        process((XADLEvent) event);
                    } else if (event instanceof XADLExternalLinkEvent) {
                        process((XADLExternalLinkEvent) event);
                    } else if (event instanceof XADLLinkEvent) {
                        process((XADLLinkEvent) event);
                    } else if (event instanceof XADLRuntimeEvent) {
                        process((XADLRuntimeEvent) event);
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
     * Process a {@link XADLEvent}.
     * 
     * @param event
     */
    private void process(XADLEvent event) {
        IArchInstance instance = modelRoot.getArchInstance(event.getArchitectureRuntimeId());

        switch (event.getXadlEventType()) {
        case ADD:
            switch (event.getXadlElementType()) {
            case COMPONENT:
                logger.info("Adding component " + event.getXadlRuntimeId() + " (" + event.getXadlBlueprintId() + ")");
                IComponentInstance component = DBLUtils.getComponentInstance(instance, event.getXadlRuntimeId());
                if (component == null) {
                    IComponent blueprintComponent = getBlueprintComponent(event.getXadlBlueprintId());
                    if (blueprintComponent != null) {
                        component = createComponentFromBlueprint(event.getXadlRuntimeId(), blueprintComponent);
                        instance.addComponentInstance(component);
                    } else {
                        logger.warn("Component blueprint cannot be found");
                    }
                } else {
                    logger.warn("Component already exists");
                }
                break;
            case CONNECTOR:
                logger.info("Adding connector " + event.getXadlRuntimeId() + " (" + event.getXadlBlueprintId() + ")");
                IConnectorInstance connector = DBLUtils.getConnectorInstance(instance, event.getXadlRuntimeId());
                if (connector == null) {
                    IConnector blueprintConnector = getBlueprintConnector(event.getXadlBlueprintId());
                    if (blueprintConnector != null) {
                        connector = createConnectorFromBlueprint(event.getXadlRuntimeId(), blueprintConnector);
                        instance.addConnectorInstance(connector);
                    } else {
                        logger.warn("Connector blueprint could not be found");
                    }
                } else {
                    logger.warn("Connector already exists");
                }
                break;
            default:
                break;
            }
            break;
        case REMOVE:
            switch (event.getXadlElementType()) {
            case COMPONENT:
                logger.info("Removing component " + event.getXadlRuntimeId() + " (" + event.getXadlBlueprintId() + ")");
                IComponentInstance component = DBLUtils.getComponentInstance(instance, event.getXadlRuntimeId());
                if (component != null) {
                    instance.removeComponentInstance(component);
                    removeLinks(instance, event.getXadlRuntimeId());
                    removeInterface(event.getXadlRuntimeId());
                } else {
                    logger.warn("Component could not be found");
                }
                break;
            case CONNECTOR:
                logger.info("Removing connector " + event.getXadlRuntimeId() + " (" + event.getXadlBlueprintId() + ")");
                IConnectorInstance connector = DBLUtils.getConnectorInstance(instance, event.getXadlRuntimeId());
                if (connector != null) {
                    instance.removeConnectorInstance(connector);
                    removeLinks(instance, event.getXadlRuntimeId());
                    removeInterface(event.getXadlRuntimeId());
                } else {
                    logger.warn("Connector could not be found");
                }
                break;
            default:
                break;
            }
            break;
        case UPDATE:
            // not supported
        default:
            break;
        }
    }

    /**
     * Get the blueprint {@link IComponent}.
     * 
     * @param blueprintId
     * @return
     */
    private IComponent getBlueprintComponent(String blueprintId) {
        for (IArchStructure structure : modelRoot.getArchStructures()) {
            for (IComponent component : DBLUtils.getComponents(structure)) {
                if (component.getId().equals(blueprintId)) {
                    return component;
                }
            }
        }
        return null;
    }

    /**
     * Create a new {@link IComponentInstance} instance based on the blueprint
     * one.
     * 
     * @param blueprintComponent
     * @return
     */
    private IComponentInstance createComponentFromBlueprint(String runtimeId, IComponent blueprintComponent) {
        IMappedComponentInstance component = modelRoot.getInstanceMappingContext().createMappedComponentInstance();
        component.setId(runtimeId);
        component.setBlueprint(DBLUtils.createXMLLink(DBLUtils.getHref(blueprintComponent.getId()),
                modelRoot.getTypesContext()));
        if (blueprintComponent.getDescription() != null) {
            component.setDescription(DBLUtils.createDescription(blueprintComponent.getDescription().getValue(),
                    modelRoot.getInstanceContext()));
        }
        for (IInterface blueprintInterface : DBLUtils.getInterfaces(blueprintComponent)) {
            IMappedInterfaceInstance intf = modelRoot.getInstanceMappingContext().createMappedInterfaceInstance();
            intf.setId(IdGenerator.generateRuntimeInstantiationId(blueprintInterface.getId()));
            if (blueprintInterface.getDescription() != null) {
                intf.setDescription(DBLUtils.createDescription(blueprintInterface.getDescription().getValue(),
                        modelRoot.getInstanceContext()));
            }
            if (blueprintInterface.getDirection() != null) {
                intf.setDirection(DBLUtils.createDirection(blueprintInterface.getDirection().getValue(),
                        modelRoot.getInstanceContext()));
            }
            if (blueprintInterface.getType() != null) {
                intf.setType(DBLUtils.createXMLLink(blueprintInterface.getType().getHref(),
                        modelRoot.getInstanceContext()));
            }
            component.addInterfaceInstance(intf);
            interface2element.put(intf, runtimeId);
        }
        return component;
    }

    /**
     * Get the blueprint {@link IConnector}.
     * 
     * @param blueprintId
     * @return
     */
    private IConnector getBlueprintConnector(String blueprintId) {
        for (IArchStructure structure : modelRoot.getArchStructures()) {
            for (IConnector connector : DBLUtils.getConnectors(structure)) {
                if (connector.getId().equals(blueprintId)) {
                    return connector;
                }
            }
        }
        return null;
    }

    /**
     * Create a new {@link IConnectorInstance} instance based on the blueprint
     * one.
     * 
     * @param blueprintConnector
     * @return
     */
    private IConnectorInstance createConnectorFromBlueprint(String runtimeId, IConnector blueprintConnector) {
        IMappedConnectorInstance connector = modelRoot.getInstanceMappingContext().createMappedConnectorInstance();
        connector.setId(runtimeId);
        connector.setBlueprint(DBLUtils.createXMLLink(DBLUtils.getHref(blueprintConnector.getId()),
                modelRoot.getTypesContext()));
        if (blueprintConnector.getDescription() != null) {
            connector.setDescription(DBLUtils.createDescription(blueprintConnector.getDescription().getValue(),
                    modelRoot.getInstanceContext()));
        }
        for (IInterface blueprintInterface : DBLUtils.getInterfaces(blueprintConnector)) {
            IMappedInterfaceInstance intf = modelRoot.getInstanceMappingContext().createMappedInterfaceInstance();
            intf.setId(IdGenerator.generateRuntimeInstantiationId(blueprintInterface.getId()));
            if (blueprintInterface.getDescription() != null) {
                intf.setDescription(DBLUtils.createDescription(blueprintInterface.getDescription().getValue(),
                        modelRoot.getInstanceContext()));
            }
            if (blueprintInterface.getDirection() != null) {
                intf.setDirection(DBLUtils.createDirection(blueprintInterface.getDirection().getValue(),
                        modelRoot.getInstanceContext()));
            }
            if (blueprintInterface.getType() != null) {
                intf.setType(DBLUtils.createXMLLink(blueprintInterface.getType().getHref(),
                        modelRoot.getInstanceContext()));
            }
            connector.addInterfaceInstance(intf);
            interface2element.put(intf, runtimeId);
        }
        return connector;
    }

    /**
     * Remove all links connected to the given {@link IComponent}/
     * {@link IConnector} from a {@link IArchInstance}.
     * 
     * @param instance
     * @param runtimeId
     */
    private void removeLinks(IArchInstance instance, String runtimeId) {
        if (element2Link.containsKey(runtimeId)) {
            for (Tuple<String, String> linkIdentifier : element2Link.get(runtimeId)) {
                if (links.containsKey(linkIdentifier)) {
                    instance.removeLinkInstance(links.get(linkIdentifier));
                    links.remove(linkIdentifier);
                }
            }
            element2Link.remove(runtimeId);
        }
    }

    /**
     * Remove all interfaces of a component or connector from the internal
     * structures.
     * 
     * @param runtimeId
     */
    private void removeInterface(String runtimeId) {
        List<IMappedInterfaceInstance> toBeRemoved = new ArrayList<>();

        for (Entry<IMappedInterfaceInstance, String> entry : interface2element.entrySet()) {
            if (entry.getValue().equals(runtimeId)) {
                toBeRemoved.add(entry.getKey());
            }
        }

        for (IMappedInterfaceInstance intf : toBeRemoved) {
            interface2element.remove(intf);
        }
    }

    /**
     * Process a {@link XADLExternalLinkEvent}.
     * 
     * @param event
     */
    private void process(XADLExternalLinkEvent event) {
        IArchInstance instance = modelRoot.getArchInstance(event.getArchitectureRuntimeId());

        // get the interface
        IMappedInterfaceInstance intf = getMappedInterfaceInstance(instance, event.getXadlRuntimeId(),
                event.getXadlInterfaceType());

        switch (event.getXadlEventType()) {
        case ADD:
            logger.info("Establishing external link on " + event.getXadlRuntimeId() + ": "
                    + event.getXadlExternalConnectionIdentifier());
            if (intf != null) {
                if (!externalConnections.containsKey(event.getXadlExternalConnectionIdentifier())) {
                    externalConnections.put(event.getXadlExternalConnectionIdentifier(),
                            new ArrayList<IMappedInterfaceInstance>());
                } else if (!externalConnections.get(event.getXadlExternalConnectionIdentifier()).isEmpty()) {
                    for (IMappedInterfaceInstance destination : externalConnections.get(event
                            .getXadlExternalConnectionIdentifier())) {
                        ILinkInstance link = createLink(event.getXadlRuntimeId(), intf,
                                event.getXadlExternalConnectionIdentifier(), destination);
                        if (link != null) {
                            instance.addLinkInstance(link);
                            Tuple<String, String> linkIdentifier = new Tuple<String, String>(intf.getId(),
                                    destination.getId());
                            saveLink(event.getXadlRuntimeId(), interface2element.get(intf), linkIdentifier, link);
                        } else {
                            logger.warn("External link on " + event.getXadlRuntimeId() + ": "
                                    + event.getXadlExternalConnectionIdentifier()
                                    + "could not be established due to non matching interfaces");
                        }
                    }
                }
                externalConnections.get(event.getXadlExternalConnectionIdentifier()).add(intf);
            } else {
                logger.warn("External link on " + event.getXadlRuntimeId() + ": "
                        + event.getXadlExternalConnectionIdentifier()
                        + "could not be established due to non matching interfaces");
            }
            break;
        case REMOVE:
            logger.info("Removing external link on " + event.getXadlRuntimeId() + ": "
                    + event.getXadlExternalConnectionIdentifier());
            if (externalConnections.containsKey(event.getXadlExternalConnectionIdentifier())) {
                if (intf != null) {
                    for (IMappedInterfaceInstance destination : externalConnections.get(event
                            .getXadlExternalConnectionIdentifier())) {
                        Tuple<String, String> linkIdentifier = new Tuple<String, String>(intf.getId(),
                                destination.getId());
                        if (links.containsKey(linkIdentifier)) {
                            instance.removeLinkInstance(links.get(linkIdentifier));
                            removeLink(event.getXadlRuntimeId(), interface2element.get(intf), linkIdentifier);
                        } else {
                            logger.warn("External link on " + event.getXadlRuntimeId() + ": "
                                    + event.getXadlExternalConnectionIdentifier()
                                    + " could not be removed because it does not exist");
                        }
                    }
                } else {
                    logger.warn("External link on " + event.getXadlRuntimeId() + ": "
                            + event.getXadlExternalConnectionIdentifier()
                            + "could not be removed due to non matching interfaces");
                }
            } else {
                logger.warn("External link on " + event.getXadlRuntimeId() + ": "
                        + event.getXadlExternalConnectionIdentifier()
                        + " could not be removed because it does not exist");
            }
            break;
        case UPDATE:
            break;
        default:
            break;
        }
    }

    /**
     * Process a {@link XADLLinkEvent}.
     * 
     * @param event
     */
    private void process(XADLLinkEvent event) {
        IArchInstance instance = modelRoot.getArchInstance(event.getArchitectureRuntimeId());

        // get the interfaces
        IMappedInterfaceInstance source = getMappedInterfaceInstance(instance, event.getXadlSourceRuntimeId(),
                event.getXadlSourceInterfaceType(), EMyxInterfaceDirection.OUT);
        IMappedInterfaceInstance destination = getMappedInterfaceInstance(instance,
                event.getXadlDestinationRuntimeId(), event.getXadlDestinationInterfaceType(), EMyxInterfaceDirection.IN);

        switch (event.getXadlEventType()) {
        case ADD:
            logger.info("Establishing link between " + event.getXadlSourceRuntimeId() + " and "
                    + event.getXadlDestinationRuntimeId());
            if (source != null && destination != null) {
                ILinkInstance link = createLink(event.getXadlSourceRuntimeId(), source,
                        event.getXadlDestinationRuntimeId(), destination);
                if (link != null) {
                    instance.addLinkInstance(link);
                    Tuple<String, String> linkIdentifier = new Tuple<String, String>(source.getId(),
                            destination.getId());
                    saveLink(event.getXadlSourceRuntimeId(), event.getXadlDestinationRuntimeId(), linkIdentifier, link);
                } else {
                    logger.warn("Link between " + event.getXadlSourceRuntimeId() + " and "
                            + event.getXadlDestinationRuntimeId()
                            + "could not be established due to non matching interfaces");
                }
            } else {
                logger.warn("Link between " + event.getXadlSourceRuntimeId() + " and "
                        + event.getXadlDestinationRuntimeId()
                        + " could not be established due to interfaces that do not exist or are unambigious");
                logger.warn(event.getXadlSourceInterfaceType() + ", " + event.getXadlDestinationInterfaceType());
            }
            break;
        case REMOVE:
            logger.info("Removing link between " + event.getXadlSourceRuntimeId() + " and "
                    + event.getXadlDestinationRuntimeId());
            if (source != null && destination != null) {
                Tuple<String, String> linkIdentifier = new Tuple<String, String>(source.getId(), destination.getId());
                if (links.containsKey(linkIdentifier)) {
                    instance.removeLinkInstance(links.get(linkIdentifier));
                    removeLink(event.getXadlSourceRuntimeId(), event.getXadlDestinationRuntimeId(), linkIdentifier);
                } else {
                    logger.warn("Link between " + event.getXadlSourceRuntimeId() + " and "
                            + event.getXadlDestinationRuntimeId() + " could not be removed because it does not exist");
                }
            } else {
                logger.warn("Link between " + event.getXadlSourceRuntimeId() + " and "
                        + event.getXadlDestinationRuntimeId()
                        + " could not be removed due to interfaces that do not exist or are unambigious");
            }
            break;
        case UPDATE:
            // not supported
        default:
            break;
        }
    }

    /**
     * Get a {@link IMappedInterfaceInstance} from a existing
     * {@link IComponentInstance} or {@link IConnectorInstance}. If two
     * interfaces with the same interface type exist it is the same as no
     * interface exists.
     * 
     * @param instance
     * @param id
     * @param interfaceType
     * @param direction
     * @return
     */
    private IMappedInterfaceInstance getMappedInterfaceInstance(IArchInstance instance, String id,
            String interfaceType, EMyxInterfaceDirection direction) {
        List<IMappedInterfaceInstance> matchingInterfaces = new ArrayList<>();

        IComponentInstance component = DBLUtils.getComponentInstance(instance, id);
        if (component != null) {
            for (IMappedInterfaceInstance intf : DBLUtils.getMappedInterfaceInstances(component)) {
                if (interfaceType.equals(DBLUtils.getId(intf.getType()))
                        && DBLUtils.getDirection(intf).equals(direction)) {
                    matchingInterfaces.add(intf);
                }
            }
            if (matchingInterfaces.size() == 1) {
                return matchingInterfaces.get(0);
            }
        }
        IConnectorInstance connector = DBLUtils.getConnectorInstance(instance, id);
        if (connector != null) {
            for (IMappedInterfaceInstance intf : DBLUtils.getMappedInterfaceInstances(connector)) {
                if (interfaceType.equals(DBLUtils.getId(intf.getType()))
                        && DBLUtils.getDirection(intf).equals(direction)) {
                    matchingInterfaces.add(intf);
                }
            }
            if (matchingInterfaces.size() == 1) {
                return matchingInterfaces.get(0);
            }
        }
        return null;
    }

    /**
     * Get a {@link IMappedInterfaceInstance} from a existing
     * {@link IComponentInstance} or {@link IConnectorInstance}. If two
     * interfaces with the same interface type exist it is the same as no
     * interface exists.
     * 
     * @param instance
     * @param id
     * @param interfaceType
     * @return
     */
    private IMappedInterfaceInstance getMappedInterfaceInstance(IArchInstance instance, String id, String interfaceType) {
        List<IMappedInterfaceInstance> matchingInterfaces = new ArrayList<>();

        IComponentInstance component = DBLUtils.getComponentInstance(instance, id);
        if (component != null) {
            for (IMappedInterfaceInstance intf : DBLUtils.getMappedInterfaceInstances(component)) {
                if (interfaceType.equals(DBLUtils.getId(intf.getType()))) {
                    matchingInterfaces.add(intf);
                }
            }
            if (matchingInterfaces.size() == 1) {
                return matchingInterfaces.get(0);
            }
        }
        IConnectorInstance connector = DBLUtils.getConnectorInstance(instance, id);
        if (connector != null) {
            for (IMappedInterfaceInstance intf : DBLUtils.getMappedInterfaceInstances(connector)) {
                if (interfaceType.equals(DBLUtils.getId(intf.getType()))) {
                    matchingInterfaces.add(intf);
                }
            }
            if (matchingInterfaces.size() == 1) {
                return matchingInterfaces.get(0);
            }
        }
        return null;
    }

    /**
     * Create a {@link ILinkInstance} instance and validate the two interfaces.
     * 
     * @param sourceRuntimeId
     * @param soucre
     * @param destinationRuntimeId
     * @param destination
     * @return
     */
    private ILinkInstance createLink(String sourceRuntimeId, IMappedInterfaceInstance source,
            String destinationRuntimeId, IMappedInterfaceInstance destination) {
        ILinkInstance link = modelRoot.getInstanceContext().createLinkInstance();
        link.setId(IdGenerator.generateId("link"));
        link.setDescription(DBLUtils.createDescription(sourceRuntimeId + "2" + destinationRuntimeId,
                modelRoot.getTypesContext()));
        // source
        IPoint sourcePoint = modelRoot.getTypesContext().createPoint();
        sourcePoint.setAnchorOnInterface(DBLUtils.createXMLLink(source.getId(), modelRoot.getTypesContext()));
        link.addPoint(sourcePoint);
        // destination
        IPoint destinationPoint = modelRoot.getTypesContext().createPoint();
        destinationPoint.setAnchorOnInterface(DBLUtils.createXMLLink(destination.getId(), modelRoot.getTypesContext()));
        link.addPoint(destinationPoint);

        return link;
    }

    /**
     * Save a link to the internal data structures.
     * 
     * @param source
     * @param destination
     * @param linkIdentifier
     * @param link
     */
    private void saveLink(String source, String destination, Tuple<String, String> linkIdentifier, ILinkInstance link) {
        links.put(linkIdentifier, link);
        if (!element2Link.containsKey(source)) {
            element2Link.put(source, new ArrayList<Tuple<String, String>>());
        }
        element2Link.get(source).add(linkIdentifier);
        if (!element2Link.containsKey(destination)) {
            element2Link.put(destination, new ArrayList<Tuple<String, String>>());
        }
        element2Link.get(destination).add(linkIdentifier);
    }

    /**
     * Remove a link from the internal data structures.
     * 
     * @param source
     * @param destination
     * @param linkIdentifier
     */
    private void removeLink(String source, String destination, Tuple<String, String> linkIdentifier) {
        links.remove(linkIdentifier);
        if (element2Link.containsKey(source)) {
            element2Link.get(source).remove(linkIdentifier);
        }
        if (element2Link.containsKey(destination)) {
            element2Link.get(destination).remove(linkIdentifier);
        }
    }

    /**
     * Process a {@link XADLRuntimeEvent}.
     * 
     * @param event
     */
    private void process(XADLRuntimeEvent event) {
        IArchInstance instance = modelRoot.getArchInstance(event.getArchitectureRuntimeId());

        switch (event.getXadlRuntimeType()) {
        case BEGIN:
            logger.info("Setting status of " + event.getXadlRuntimeId() + " to RUNNING");
            setRuntimeStatus(instance, event.getXadlRuntimeId(), "RUNNING");
            break;
        case END:
            logger.info("Setting status of " + event.getXadlRuntimeId() + " to NOT RUNNING");
            setRuntimeStatus(instance, event.getXadlRuntimeId(), null);
            break;
        default:
            break;
        }
    }

    /**
     * Set the runtime status of a specific component or connector.
     * 
     * @param instance
     * @param id
     */
    private void setRuntimeStatus(IArchInstance instance, String id, String status) {
        IComponentInstance component = DBLUtils.getComponentInstance(instance, id);
        IConnectorInstance connector = DBLUtils.getConnectorInstance(instance, id);

        if (component == null && connector == null) {
            logger.warn("Could not set the status of " + id + " because it does not exist");
            return;
        }

        if (component != null) {
            String description = getRuntimeDescription(component.getDescription().getValue(), status);
            component.getDescription().setValue(description);
            return;
        }
        if (connector != null) {
            String description = getRuntimeDescription(connector.getDescription().getValue(), status);
            connector.getDescription().setValue(description);
            return;
        }
    }

    /**
     * Get the description string representing the runtime status.
     * 
     * @param description
     * @param status
     * @return
     */
    private String getRuntimeDescription(String description, String status) {
        if (description.contains("[")) {
            description = description.substring(0, description.lastIndexOf("[")).trim();
        }
        if (status != null) {
            description += " [" + status + "]";
        }
        return description;
    }

}
