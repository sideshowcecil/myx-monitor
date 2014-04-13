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
import at.ac.tuwien.dsg.pubsub.message.Topic;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.ISubscriber;
import edu.uci.isr.myx.fw.EMyxInterfaceDirection;
import edu.uci.isr.xarch.instance.IPoint;
import edu.uci.isr.xarch.types.IArchStructure;
import edu.uci.isr.xarch.types.IComponent;
import edu.uci.isr.xarch.types.IConnector;
import edu.uci.isr.xarch.types.IInterface;
import edu.uci.isr.xarch.types.ILink;

public class XADLRuntimeManager implements ISubscriber<Event> {

    private static final Logger logger = LoggerFactory.getLogger(XADLRuntimeManager.class);

    private final Map<IInterface, String> interface2element = new HashMap<>();

    /**
     * Mapping of component/connector to the connected link identifiers.
     */
    private final Map<String, List<Tuple<String, String>>> element2Link = new HashMap<>();

    /**
     * Mapping of link identifier (source interface and destination interface)
     * to the real link.
     */
    private final Map<Tuple<String, String>, ILink> links = new HashMap<>();

    /**
     * Mapping of external connection identifiers to the connected interfaces.
     */
    private final Map<String, List<IInterface>> externalConnections = new HashMap<>();

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
            logger.info("Consuming event of type " + message.getData().getClass());
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
        IArchStructure structure = modelRoot.getArchStructure(event.getArchitectureRuntimeId());

        switch (event.getXadlEventType()) {
        case ADD:
            switch (event.getXadlElementType()) {
            case COMPONENT:
                logger.info("Adding component " + event.getXadlRuntimeId() + " (" + event.getXadlBlueprintId() + ")");
                IComponent component = DBLUtils.getComponent(structure, event.getXadlRuntimeId());
                if (component == null) {
                    IComponent blueprintComponent = getBlueprintComponent(event.getXadlBlueprintId());
                    if (blueprintComponent != null) {
                        component = createComponentFromBlueprint(event.getXadlRuntimeId(), blueprintComponent);
                        structure.addComponent(component);
                    } else {
                        logger.warn("Component blueprint cannot be found");
                    }
                } else {
                    logger.warn("Component already exists");
                }
                break;
            case CONNECTOR:
                logger.info("Adding connector " + event.getXadlRuntimeId() + " (" + event.getXadlBlueprintId() + ")");
                IConnector connector = DBLUtils.getConnector(structure, event.getXadlRuntimeId());
                if (connector == null) {
                    IConnector blueprintConnector = getBlueprintConnector(event.getXadlBlueprintId());
                    if (blueprintConnector != null) {
                        connector = createConnectorFromBlueprint(event.getXadlRuntimeId(), blueprintConnector);
                        structure.addConnector(connector);
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
                IComponent component = DBLUtils.getComponent(structure, event.getXadlRuntimeId());
                if (component != null) {
                    structure.removeComponent(component);
                    removeLinks(structure, event.getXadlRuntimeId());
                    removeInterface(event.getXadlRuntimeId());
                } else {
                    logger.warn("Component could not be found");
                }
                break;
            case CONNECTOR:
                logger.info("Removing connector " + event.getXadlRuntimeId() + " (" + event.getXadlBlueprintId() + ")");
                IConnector connector = DBLUtils.getConnector(structure, event.getXadlRuntimeId());
                if (connector != null) {
                    structure.removeConnector(connector);
                    removeLinks(structure, event.getXadlRuntimeId());
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
     * Create a new {@link IComponent} instance based on the blueprint one.
     * 
     * @param blueprintComponent
     * @return
     */
    private IComponent createComponentFromBlueprint(String runtimeId, IComponent blueprintComponent) {
        IComponent component = modelRoot.getTypesContext().createComponent();
        component.setId(runtimeId);
        if (blueprintComponent.getDescription() != null) {
            component.setDescription(blueprintComponent.getDescription());
        }
        if (blueprintComponent.getType() != null) {
            component.setType(blueprintComponent.getType());
        }
        for (IInterface blueprintInterface : DBLUtils.getInterfaces(blueprintComponent)) {
            IInterface intf = modelRoot.getTypesContext().createInterface();
            intf.setId(IdGenerator.generateRuntimeInstantiationId(blueprintInterface.getId()));
            if (blueprintInterface.getDescription() != null) {
                intf.setDescription(blueprintInterface.getDescription());
            }
            if (blueprintInterface.getDirection() != null) {
                intf.setDirection(blueprintInterface.getDirection());
            }
            if (blueprintInterface.getSignature() != null) {
                intf.setSignature(blueprintInterface.getSignature());
            }
            if (blueprintInterface.getType() != null) {
                intf.setType(blueprintInterface.getType());
            }
            component.addInterface(intf);
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
     * Create a new {@link IConnector} instance based on the blueprint one.
     * 
     * @param blueprintConnector
     * @return
     */
    private IConnector createConnectorFromBlueprint(String runtimeId, IConnector blueprintConnector) {
        IConnector connector = modelRoot.getTypesContext().createConnector();
        connector.setId(runtimeId);
        if (blueprintConnector.getDescription() != null) {
            connector.setDescription(blueprintConnector.getDescription());
        }
        if (blueprintConnector.getDescription() != null) {
            connector.setType(blueprintConnector.getType());
        }
        for (IInterface blueprintInterface : DBLUtils.getInterfaces(blueprintConnector)) {
            IInterface intf = modelRoot.getTypesContext().createInterface();
            intf.setId(IdGenerator.generateRuntimeInstantiationId(blueprintInterface.getId()));
            if (blueprintInterface.getDescription() != null) {
                intf.setDescription(blueprintInterface.getDescription());
            }
            if (blueprintInterface.getDirection() != null) {
                intf.setDirection(blueprintInterface.getDirection());
            }
            if (blueprintInterface.getSignature() != null) {
                intf.setSignature(blueprintInterface.getSignature());
            }
            if (blueprintInterface.getType() != null) {
                intf.setType(blueprintInterface.getType());
            }
            connector.addInterface(intf);
            interface2element.put(intf, runtimeId);
        }
        return connector;
    }

    /**
     * Remove all links connected to the given {@link IComponent}/
     * {@link IConnector} from a {@link IArchStructure}.
     * 
     * @param structure
     * @param runtimeId
     */
    private void removeLinks(IArchStructure structure, String runtimeId) {
        if (element2Link.containsKey(runtimeId)) {
            for (Tuple<String, String> linkIdentifier : element2Link.get(runtimeId)) {
                if (links.containsKey(linkIdentifier)) {
                    structure.removeLink(links.get(linkIdentifier));
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
        List<IInterface> toBeRemoved = new ArrayList<>();

        for (Entry<IInterface, String> entry : interface2element.entrySet()) {
            if (entry.getValue().equals(runtimeId)) {
                toBeRemoved.add(entry.getKey());
            }
        }

        for (IInterface intf : toBeRemoved) {
            interface2element.remove(intf);
        }
    }

    /**
     * Process a {@link XADLExternalLinkEvent}.
     * 
     * @param event
     */
    private void process(XADLExternalLinkEvent event) {
        IArchStructure structure = modelRoot.getArchStructure(event.getArchitectureRuntimeId());

        // get the interface
        IInterface intf = getInterface(structure, event.getXadlRuntimeId(), event.getXadlInterfaceType());

        switch (event.getXadlEventType()) {
        case ADD:
            logger.info("Establishing external link on " + event.getXadlRuntimeId() + ": "
                    + event.getXadlExternalConnectionIdentifier());
            if (!externalConnections.containsKey(event.getXadlExternalConnectionIdentifier())) {
                externalConnections.put(event.getXadlExternalConnectionIdentifier(), new ArrayList<IInterface>());
            }
            if (!externalConnections.get(event.getXadlExternalConnectionIdentifier()).isEmpty()) {
                for (IInterface destination : externalConnections.get(event.getXadlExternalConnectionIdentifier())) {
                    ILink link = createLink(event.getXadlRuntimeId(), intf,
                            event.getXadlExternalConnectionIdentifier(), destination);
                    if (link != null) {
                        structure.addLink(link);
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
            break;
        case REMOVE:
            logger.info("Removing external link on " + event.getXadlRuntimeId() + ": "
                    + event.getXadlExternalConnectionIdentifier());
            if (externalConnections.containsKey(event.getXadlExternalConnectionIdentifier())) {
                for (IInterface destination : externalConnections.get(event.getXadlExternalConnectionIdentifier())) {
                    Tuple<String, String> linkIdentifier = new Tuple<String, String>(intf.getId(), destination.getId());
                    if (links.containsKey(linkIdentifier)) {
                        structure.removeLink(links.get(linkIdentifier));
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
        IArchStructure structure = modelRoot.getArchStructure(event.getArchitectureRuntimeId());

        // get the interfaces
        IInterface source = getInterface(structure, event.getXadlSourceRuntimeId(), event.getXadlSourceInterfaceType(),
                EMyxInterfaceDirection.OUT);
        IInterface destination = getInterface(structure, event.getXadlDestinationRuntimeId(),
                event.getXadlDestinationInterfaceType(), EMyxInterfaceDirection.IN);

        switch (event.getXadlEventType()) {
        case ADD:
            logger.info("Establishing link between " + event.getXadlSourceRuntimeId() + " and "
                    + event.getXadlDestinationRuntimeId());
            if (source != null && destination != null) {
                ILink link = createLink(event.getXadlSourceRuntimeId(), source, event.getXadlDestinationRuntimeId(),
                        destination);
                if (link != null) {
                    structure.addLink(link);
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
                    structure.removeLink(links.get(linkIdentifier));
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
     * Get a {@link IInterface} from a existing {@link IComponent} or
     * {@link IConnector}. If two interfaces with the same interface type exist
     * it is the same as no interface exists.
     * 
     * @param structure
     * @param id
     * @param interfaceType
     * @param direction
     * @return
     */
    private IInterface getInterface(IArchStructure structure, String id, String interfaceType,
            EMyxInterfaceDirection direction) {
        List<IInterface> matchingInterfaces = new ArrayList<>();

        IComponent component = DBLUtils.getComponent(structure, id);
        if (component != null) {
            for (IInterface intf : DBLUtils.getInterfaces(component)) {
                if (interfaceType.equals(DBLUtils.getId(intf.getType()))
                        && DBLUtils.getDirection(intf).equals(direction)) {
                    matchingInterfaces.add(intf);
                }
            }
            if (matchingInterfaces.size() == 1) {
                return matchingInterfaces.get(0);
            }
        }
        IConnector connector = DBLUtils.getConnector(structure, id);
        if (connector != null) {
            for (IInterface intf : DBLUtils.getInterfaces(connector)) {
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
     * Get a {@link IInterface} from a existing {@link IComponent} or
     * {@link IConnector}. If two interfaces with the same interface type exist
     * it is the same as no interface exists.
     * 
     * @param structure
     * @param id
     * @param interfaceType
     * @return
     */
    private IInterface getInterface(IArchStructure structure, String id, String interfaceType) {
        List<IInterface> matchingInterfaces = new ArrayList<>();

        IComponent component = DBLUtils.getComponent(structure, id);
        if (component != null) {
            for (IInterface intf : DBLUtils.getInterfaces(component)) {
                if (interfaceType.equals(DBLUtils.getId(intf.getType()))) {
                    matchingInterfaces.add(intf);
                }
            }
            if (matchingInterfaces.size() == 1) {
                return matchingInterfaces.get(0);
            }
        }
        IConnector connector = DBLUtils.getConnector(structure, id);
        if (connector != null) {
            for (IInterface intf : DBLUtils.getInterfaces(connector)) {
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
     * Create a {@link ILink} instance and validate the two interfaces.
     * 
     * @param sourceRuntimeId
     * @param soucre
     * @param destinationRuntimeId
     * @param destination
     * @return
     */
    private ILink createLink(String sourceRuntimeId, IInterface source, String destinationRuntimeId,
            IInterface destination) {
        ILink link = modelRoot.getTypesContext().createLink();
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
    private void saveLink(String source, String destination, Tuple<String, String> linkIdentifier, ILink link) {
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
        IArchStructure structure = modelRoot.getArchStructure(event.getArchitectureRuntimeId());

        switch (event.getXadlRuntimeType()) {
        case BEGIN:
            logger.info("Setting status of " + event.getXadlRuntimeId() + " to RUNNING");
            if (!setRuntimeStatus(structure, event.getXadlRuntimeId(), "RUNNING")) {
                logger.warn("Could not set the status of " + event.getXadlRuntimeId() + " because it does not exist");
            }
            break;
        case END:
            logger.info("Setting status of " + event.getXadlRuntimeId() + " to NOT RUNNING");
            if (!setRuntimeStatus(structure, event.getXadlRuntimeId(), "NOT_RUNNING")) {
                logger.warn("Could not set the status of " + event.getXadlRuntimeId() + " because it does not exist");
            }
            break;
        default:
            break;
        }
    }

    /**
     * Set the runtime status of a specific component or connector.
     * 
     * @param structure
     * @param id
     * @return
     */
    private boolean setRuntimeStatus(IArchStructure structure, String id, String status) {
        IComponent component = DBLUtils.getComponent(structure, id);
        if (component != null) {
            String description = getRuntimeDescription(component.getDescription().getValue(), status);
            component.getDescription().setValue(description);
            return true;
        }
        IConnector connector = DBLUtils.getConnector(structure, id);
        if (connector != null) {
            String description = getRuntimeDescription(connector.getDescription().getValue(), status);
            connector.getDescription().setValue(description);
            return true;
        }
        return false;
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
        description += " [" + status + "]";
        return description;
    }

}
