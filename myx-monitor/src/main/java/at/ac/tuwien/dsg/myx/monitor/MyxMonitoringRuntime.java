package at.ac.tuwien.dsg.myx.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import at.ac.tuwien.dsg.myx.fw.IMyxInitPropertiesInterfaceDescription;
import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLElementType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEventType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostingEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLLinkEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLRuntimeEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLRuntimeEventType;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.myx.util.Tuple;
import edu.uci.isr.myx.fw.EMyxInterfaceDirection;
import edu.uci.isr.myx.fw.IMyxBrick;
import edu.uci.isr.myx.fw.IMyxBrickDescription;
import edu.uci.isr.myx.fw.IMyxBrickItems;
import edu.uci.isr.myx.fw.IMyxContainer;
import edu.uci.isr.myx.fw.IMyxInterfaceDescription;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.IMyxWeld;
import edu.uci.isr.myx.fw.MyxBasicRuntime;
import edu.uci.isr.myx.fw.MyxBrickCreationException;
import edu.uci.isr.myx.fw.MyxBrickLoadException;
import edu.uci.isr.myx.fw.MyxInvalidPathException;
import edu.uci.isr.myx.fw.MyxJavaClassBrickDescription;

public class MyxMonitoringRuntime extends MyxBasicRuntime {

    protected String architectureRuntimeId;
    protected String hostId;
    protected EventManager eventManager;

    /**
     * This map is used to reference the runtime id to the blueprint id.
     */
    protected Map<String, String> runtime2blueprint = new HashMap<>();
    /**
     * This map is used to reference the runtime id to the element type.
     */
    protected Map<String, XADLElementType> runtime2elemntType = new HashMap<>();
    /**
     * This map is used to keep track of the added interfaces.
     */
    protected Map<Tuple<String, String>, String> interfaces = new HashMap<>();

    public MyxMonitoringRuntime(String architectureRuntimeId, String hostId, EventManager eventManager) {
        this.architectureRuntimeId = architectureRuntimeId;
        this.hostId = hostId;
        this.eventManager = eventManager;

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                List<IMyxName> bricks = MyxMonitoringRuntime.this.getBrickNames(null, null);
                // send runtime events
                for (IMyxName brick : bricks) {
                    for (IMyxName b : getBrickNames(null, brick)) {
                        String runtimeId = b.getName();
                        // send event
                        dispatchXADLRuntimeEvent(runtimeId, XADLRuntimeEventType.END);
                    }
                }
                // send xadl- and hosting events
                for (IMyxName brick : bricks) {
                    for (IMyxName b : getBrickNames(null, brick)) {
                        String runtimeId = b.getName();
                        XADLElementType elementType = runtime2elemntType.get(runtimeId);
                        // send events
                        if (elementType == XADLElementType.COMPONENT) {
                            dispatchXADLHostingEventForComponent(runtimeId, XADLEventType.REMOVE);
                        } else {
                            dispatchXADLHostingEventForConnector(runtimeId, XADLEventType.REMOVE);
                        }
                        dispatchXADLEvent(runtimeId, runtime2blueprint.get(runtimeId), XADLEventType.REMOVE,
                                elementType);
                    }
                }
            }
        });
    }

    @Override
    public void addBrick(IMyxName[] path, IMyxName brickName, IMyxBrickDescription brickDescription)
            throws MyxBrickLoadException, MyxBrickCreationException {
        // TODO should we assign the architectureRuntimeId just to the EventManager, thus it could be injected to each event (the base event has to be changed to make this possible)
        // changes have to be integrated here and in the virtual external simple brick
        if (brickDescription instanceof MyxJavaClassBrickDescription) {
            try {
                Class<?> brickClass = Class.forName(((MyxJavaClassBrickDescription) brickDescription)
                        .getMainBrickClassName());
                if (AbstractVirtualExternalMyxSimpleBrick.class.isAssignableFrom(brickClass)) {
                    // here we have to inject the architectureRuntimeId
                    if (brickDescription.getInitParams() != null) {
                        brickDescription.getInitParams().put(MyxProperties.ARCHITECTURE_RUNTIME_ID,
                                architectureRuntimeId);
                    }
                }
            } catch (ClassNotFoundException e) {
            }
        }

        super.addBrick(path, brickName, brickDescription);

        Properties initProperties = brickDescription.getInitParams();
        if (initProperties != null && initProperties.containsKey(MyxProperties.ARCHITECTURE_BLUEPRINT_ID)
                && initProperties.containsKey(MyxProperties.ARCHITECTURE_BRICK_TYPE)) {
            String runtimeId = brickName.getName(), blueprintId = initProperties
                    .getProperty(MyxProperties.ARCHITECTURE_BLUEPRINT_ID);
            XADLElementType elementType = (XADLElementType) initProperties.get(MyxProperties.ARCHITECTURE_BRICK_TYPE);
            // send event
            dispatchXADLEvent(runtimeId, blueprintId, XADLEventType.ADD, elementType);

            // save the id and type
            runtime2blueprint.put(runtimeId, blueprintId);
            runtime2elemntType.put(runtimeId, elementType);

            // send the hosting event
            if (elementType == XADLElementType.COMPONENT) {
                dispatchXADLHostingEventForComponent(runtimeId, XADLEventType.ADD);
            } else {
                dispatchXADLHostingEventForConnector(runtimeId, XADLEventType.ADD);
            }
        }
    }

    @Override
    public void removeBrick(IMyxName[] path, IMyxName brickName) {
        super.removeBrick(path, brickName);

        String runtimeId = brickName.getName();
        if (runtime2blueprint.containsKey(runtimeId)) {
            String blueprintId = runtime2blueprint.get(runtimeId);
            XADLElementType elementType = runtime2elemntType.get(runtimeId);

            // send event
            dispatchXADLEvent(runtimeId, blueprintId, XADLEventType.REMOVE, elementType);

            // send the hosting event
            if (elementType == XADLElementType.COMPONENT) {
                dispatchXADLHostingEventForComponent(runtimeId, XADLEventType.REMOVE);
            } else {
                dispatchXADLHostingEventForConnector(runtimeId, XADLEventType.REMOVE);
            }
        }
    }

    @Override
    public void addInterface(IMyxName[] path, IMyxName brickName, IMyxName interfaceName,
            IMyxInterfaceDescription interfaceDescription, EMyxInterfaceDirection interfaceDirection) {
        super.addInterface(path, brickName, interfaceName, interfaceDescription, interfaceDirection);

        if (interfaceDescription instanceof IMyxInitPropertiesInterfaceDescription) {
            Properties initProperties = ((IMyxInitPropertiesInterfaceDescription) interfaceDescription).getInitParams();
            if (initProperties != null && initProperties.containsKey(MyxProperties.ARCHITECTURE_INTERFACE_TYPE)) {
                // save interface type for brick- and interface name
                Tuple<String, String> brickIntf = new Tuple<>();
                brickIntf.setFst(brickName.getName());
                brickIntf.setSnd(interfaceName.getName());
                interfaces.put(brickIntf, initProperties.getProperty(MyxProperties.ARCHITECTURE_INTERFACE_TYPE));
            }
        }
    }

    @Override
    public void removeInterface(IMyxName[] path, IMyxName brickName, IMyxName interfaceName) {
        super.removeInterface(path, brickName, interfaceName);

        Tuple<String, String> brickIntf = new Tuple<>();
        brickIntf.setFst(brickName.getName());
        brickIntf.setSnd(interfaceName.getName());
        if (interfaces.containsKey(brickIntf)) {
            // remove the interface
            interfaces.remove(brickIntf);
        }
    }

    @Override
    public void addWeld(IMyxWeld weld) {
        super.addWeld(weld);

        dispatchXADLLinkEvent(weld, XADLEventType.ADD);
    }

    @Override
    public void removeWeld(IMyxWeld weld) {
        super.removeWeld(weld);

        dispatchXADLLinkEvent(weld, XADLEventType.REMOVE);
    }

    @Override
    public void begin(IMyxName[] path, IMyxName brickName) {
        super.begin(path, brickName);

        for (IMyxName b : getBrickNames(path, brickName)) {
            // send event
            dispatchXADLRuntimeEvent(b.getName(), XADLRuntimeEventType.BEGIN);
        }
    }

    @Override
    public void end(IMyxName[] path, IMyxName brickName) {
        super.end(path, brickName);

        for (IMyxName b : getBrickNames(path, brickName)) {
            // send event
            dispatchXADLRuntimeEvent(b.getName(), XADLRuntimeEventType.END);
        }
    }

    /**
     * Dispatch a {@link XADLEvent}.
     * 
     * @param xadlRuntimeId
     * @param xadlElementId
     * @param xadlEventType
     * @param xadlElementType
     */
    private void dispatchXADLEvent(String xadlRuntimeId, String xadlElementId, XADLEventType xadlEventType,
            XADLElementType xadlElementType) {
        XADLEvent e = new XADLEvent(architectureRuntimeId, xadlRuntimeId, xadlElementId, xadlEventType);
        e.setXadlElementType(xadlElementType);
        dispatchEvent(e);
    }

    /**
     * Dispatch a {@link XADLLinkEvent} of a {@link IMyxWeld}.
     * 
     * @param weld
     * @param xadlEventType
     */
    private void dispatchXADLLinkEvent(IMyxWeld weld, XADLEventType xadlEventType) {
        String requiredBrickName = weld.getRequiredBrickName().getName(), requiredInterfaceName = weld
                .getRequiredInterfaceName().getName();
        String providedBrickName = weld.getProvidedBrickName().getName(), providedInterfaceName = weld
                .getProvidedInterfaceName().getName();

        Tuple<String, String> requiredBrickIntf = new Tuple<>(requiredBrickName, requiredInterfaceName);
        Tuple<String, String> providedBrickIntf = new Tuple<>(providedBrickName, providedInterfaceName);

        if (interfaces.containsKey(requiredBrickIntf) && interfaces.containsKey(providedBrickIntf)) {
            String requiredInterfaceType = interfaces.get(requiredBrickIntf), providedInterfaceType = interfaces
                    .get(providedBrickIntf);
            // send event
            dispatchXADLLinkEvent(requiredBrickName, requiredInterfaceName, requiredInterfaceType, providedBrickName,
                    providedInterfaceName, providedInterfaceType, xadlEventType);
        }
    }

    /**
     * Dispatch a {@link XADLLinkEvent}.
     * 
     * @param xadlSourceRuntimeId
     * @param xadlSourceInterfaceName
     * @param xadlSourceInterfaceType
     * @param xadlDestinationRuntimeId
     * @param xadlDestinationElementInterfaceName
     * @param xadlDestinationInterfaceType
     * @param xadlEventType
     */
    private void dispatchXADLLinkEvent(String xadlSourceRuntimeId, String xadlSourceInterfaceName,
            String xadlSourceInterfaceType, String xadlDestinationRuntimeId,
            String xadlDestinationElementInterfaceName, String xadlDestinationInterfaceType, XADLEventType xadlEventType) {
        XADLLinkEvent e = new XADLLinkEvent(architectureRuntimeId, xadlSourceRuntimeId, xadlSourceInterfaceName,
                xadlSourceInterfaceType, xadlDestinationRuntimeId, xadlDestinationElementInterfaceName,
                xadlDestinationInterfaceType, xadlEventType);
        dispatchEvent(e);
    }

    /**
     * Dispatch a {@link XADLRuntimeEvent}.
     * 
     * @param xadlRuntimeId
     * @param xadlRuntimeType
     */
    private void dispatchXADLRuntimeEvent(String xadlRuntimeId, XADLRuntimeEventType xadlRuntimeType) {
        XADLRuntimeEvent e = new XADLRuntimeEvent(architectureRuntimeId, xadlRuntimeId, xadlRuntimeType);
        dispatchEvent(e);
    }

    /**
     * Dispatch a {@link XADLHostingEvent} for a component.
     * 
     * @param runtimeId
     * @param xadlEventType
     */
    private void dispatchXADLHostingEventForComponent(String runtimeId, XADLEventType xadlEventType) {
        XADLHostingEvent e = new XADLHostingEvent(architectureRuntimeId, hostId, xadlEventType);
        e.getHostedComponentIds().add(runtimeId);
        dispatchEvent(e);
    }

    /**
     * Dispatch a {@link XADLHostingEvent} for a connector.
     * 
     * @param runtimeId
     * @param xadlEventType
     */
    private void dispatchXADLHostingEventForConnector(String runtimeId, XADLEventType xadlEventType) {
        XADLHostingEvent e = new XADLHostingEvent(architectureRuntimeId, hostId, xadlEventType);
        e.getHostedConnectorIds().add(runtimeId);
        dispatchEvent(e);
    }

    /**
     * Dispatch a {@link Event}.
     * 
     * @param e
     */
    private void dispatchEvent(Event e) {
        e.setEventSourceId(this.getClass().getName());
        eventManager.handle(e);
    }

    /**
     * Get all bricks that get used by the lifecycle method.
     * 
     * @param path
     * @param brickName
     * @return
     */
    private List<IMyxName> getBrickNames(IMyxName[] path, IMyxName brickName) {
        IMyxContainer container = MyxMonitoringUtils.resolvePath(mainContainer, path);
        if (container == null) {
            throw new MyxInvalidPathException(path);
        }
        List<IMyxName> bricks = new ArrayList<>();
        if (brickName == null) {
            for (IMyxBrick brick : container.getInternalBricks()) {
                IMyxBrickItems brickItems = brick.getMyxBrickItems();
                if (brickItems != null) {
                    bricks.add(brickItems.getBrickName());
                }
            }
        } else {
            if (container.getInternalBrick(brickName) == null) {
                throw new IllegalArgumentException("No brick found with name: " + brickName + " at "
                        + MyxMonitoringUtils.pathToString(path));
            }
            bricks.add(brickName);
        }
        return bricks;
    }

}
