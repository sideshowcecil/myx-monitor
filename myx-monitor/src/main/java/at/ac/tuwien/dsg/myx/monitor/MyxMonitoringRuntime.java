package at.ac.tuwien.dsg.myx.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import at.ac.tuwien.dsg.myx.fw.IMyxInitPropertiesInterfaceDescription;
import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLElementType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEventType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostInstanceEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostingEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLLinkEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLRuntimeEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLRuntimeEventType;
import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.myx.util.MyxUtils;
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

public class MyxMonitoringRuntime extends MyxBasicRuntime {

    protected EventManager eventManager;

    /**
     * Mapping used to track elements of an active brick
     */
    protected Map<String, RuntimeElement> runtimeElements = new HashMap<>();

    public MyxMonitoringRuntime(EventManager eventManager) {
        this.eventManager = eventManager;

        // dispatch an event that the host as been created
        dispatchXADLHostInstanceEvent(XADLEventType.ADD);

        try {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    // get all active bricks
                    final Set<IMyxName> bricks = new HashSet<>();
                    for (IMyxName brick : getBrickNames(null, null)) {
                        bricks.addAll(getBrickNames(null, brick));
                    }
                    long totalEvents = 0;
                    // send runtime events
                    for (IMyxName brick : bricks) {
                        String runtimeId = brick.getName();
                        if (runtimeElements.containsKey(runtimeId)) {
                            // send event
                            dispatchXADLRuntimeEvent(runtimeId, runtimeElements.get(runtimeId).blueprintId,
                                    XADLRuntimeEventType.END);
                            totalEvents++;
                        }
                    }
                    // send link events
                    for (IMyxName brick : bricks) {
                        String runtimeId = brick.getName();
                        if (runtimeElements.containsKey(runtimeId)) {
                            for (IMyxWeld weld : runtimeElements.get(runtimeId).welds) {
                                // send event
                                dispatchXADLLinkEvent(weld, XADLEventType.REMOVE);
                                totalEvents++;
                            }
                        }
                    }
                    // send xadl- and hosting events
                    for (IMyxName brick : bricks) {
                        String runtimeId = brick.getName();
                        if (runtimeElements.containsKey(runtimeId)) {
                            RuntimeElement e = runtimeElements.get(runtimeId);
                            // send events
                            if (e.elementType == XADLElementType.COMPONENT) {
                                dispatchXADLHostingEventForComponent(runtimeId, XADLEventType.REMOVE);
                            } else {
                                dispatchXADLHostingEventForConnector(runtimeId, XADLEventType.REMOVE);
                            }
                            dispatchXADLEvent(runtimeId, e.blueprintId, XADLEventType.REMOVE, e.elementType);
                            totalEvents += 2;
                        }
                    }
                    // dispatch an event that the host as been removed
                    dispatchXADLHostInstanceEvent(XADLEventType.REMOVE);
                    totalEvents++;
                    // wait some time so that all events were dispatched
                    try {
                        Thread.sleep(totalEvents * 10);
                    } catch (InterruptedException e) {
                    }
                }
            });
        } catch (IllegalStateException e) {
        }
    }

    @Override
    public void addBrick(IMyxName[] path, IMyxName brickName, IMyxBrickDescription brickDescription)
            throws MyxBrickLoadException, MyxBrickCreationException {
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
            RuntimeElement e = new RuntimeElement();
            e.runtimeId = runtimeId;
            e.blueprintId = blueprintId;
            e.elementType = elementType;
            runtimeElements.put(runtimeId, e);

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
        if (runtimeElements.containsKey(runtimeId)) {
            RuntimeElement e = runtimeElements.get(runtimeId);

            // send event
            dispatchXADLEvent(runtimeId, e.blueprintId, XADLEventType.REMOVE, e.elementType);

            // send the hosting event
            if (e.elementType == XADLElementType.COMPONENT) {
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
                if (runtimeElements.containsKey(brickName.getName())) {
                    runtimeElements.get(brickName.getName()).interfaceTypes.put(interfaceName.getName(),
                            initProperties.getProperty(MyxProperties.ARCHITECTURE_INTERFACE_TYPE));
                }
            }
        }
    }

    @Override
    public void removeInterface(IMyxName[] path, IMyxName brickName, IMyxName interfaceName) {
        super.removeInterface(path, brickName, interfaceName);

        Tuple<String, String> brickIntf = new Tuple<>();
        brickIntf.setFst(brickName.getName());
        brickIntf.setSnd(interfaceName.getName());
        if (runtimeElements.containsKey(brickName.getName())) {
            // remove the interface
            runtimeElements.get(brickName.getName()).interfaceTypes.remove(interfaceName.getName());
        }
    }

    @Override
    public void addWeld(IMyxWeld weld) {
        super.addWeld(weld);

        dispatchXADLLinkEvent(weld, XADLEventType.ADD);

        // save the weld
        String sourceRuntimeId = weld.getRequiredBrickName().getName();
        if (runtimeElements.containsKey(sourceRuntimeId)) {
            runtimeElements.get(sourceRuntimeId).welds.add(weld);
        }
    }

    @Override
    public void removeWeld(IMyxWeld weld) {
        super.removeWeld(weld);

        dispatchXADLLinkEvent(weld, XADLEventType.REMOVE);

        // remove the saved weld
        String sourceRuntimeId = weld.getRequiredBrickName().getName();
        if (runtimeElements.containsKey(sourceRuntimeId)) {
            runtimeElements.get(sourceRuntimeId).welds.remove(weld);
        }
    }

    @Override
    public void begin(IMyxName[] path, IMyxName brickName) {
        super.begin(path, brickName);

        for (IMyxName b : getBrickNames(path, brickName)) {
            String runtimeId = b.getName();
            if (runtimeElements.containsKey(runtimeId)) {
                // send event
                dispatchXADLRuntimeEvent(runtimeId, runtimeElements.get(runtimeId).blueprintId,
                        XADLRuntimeEventType.BEGIN);
            }
        }
    }

    @Override
    public void end(IMyxName[] path, IMyxName brickName) {
        super.end(path, brickName);

        for (IMyxName b : getBrickNames(path, brickName)) {
            String runtimeId = b.getName();
            if (runtimeElements.containsKey(runtimeId)) {
                // send event
                dispatchXADLRuntimeEvent(runtimeId, runtimeElements.get(runtimeId).blueprintId,
                        XADLRuntimeEventType.END);
            }
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
        dispatchEvent(new XADLEvent(xadlRuntimeId, xadlElementId, xadlEventType, xadlElementType));
    }

    /**
     * Dispatch a {@link XADLLinkEvent} of a {@link IMyxWeld}.
     * 
     * @param weld
     * @param xadlEventType
     */
    private void dispatchXADLLinkEvent(IMyxWeld weld, XADLEventType xadlEventType) {
        String sourceRuntimeId = weld.getRequiredBrickName().getName(), sourceInterfaceName = weld
                .getRequiredInterfaceName().getName();
        String destinationRuntimeId = weld.getProvidedBrickName().getName(), destinationInterfaceName = weld
                .getProvidedInterfaceName().getName();
        if (runtimeElements.containsKey(sourceRuntimeId) && runtimeElements.containsKey(destinationRuntimeId)) {
            RuntimeElement src = runtimeElements.get(sourceRuntimeId), dst = runtimeElements.get(destinationRuntimeId);
            if (src.interfaceTypes.containsKey(sourceInterfaceName)
                    && dst.interfaceTypes.containsKey(destinationInterfaceName)) {
                // send event
                dispatchXADLLinkEvent(sourceRuntimeId, src.blueprintId, src.interfaceTypes.get(sourceInterfaceName),
                        destinationRuntimeId, dst.blueprintId, dst.interfaceTypes.get(destinationInterfaceName),
                        xadlEventType);
            }
        }
    }

    /**
     * Dispatch a {@link XADLLinkEvent}.
     * 
     * @param xadlSourceRuntimeId
     * @param xadlSourceBlueprintId
     * @param xadlSourceInterfaceName
     * @param xadlSourceInterfaceType
     * @param xadlDestinationRuntimeId
     * @param xadlDestinationBlueprintId
     * @param xadlDestinationElementInterfaceName
     * @param xadlDestinationInterfaceType
     * @param xadlEventType
     */
    private void dispatchXADLLinkEvent(String xadlSourceRuntimeId, String xadlSourceBlueprintId,
            String xadlSourceInterfaceType, String xadlDestinationRuntimeId, String xadlDestinationBlueprintId,
            String xadlDestinationInterfaceType, XADLEventType xadlEventType) {
        XADLLinkEvent e = new XADLLinkEvent(xadlSourceRuntimeId, xadlSourceBlueprintId, xadlSourceInterfaceType,
                xadlDestinationRuntimeId, xadlDestinationBlueprintId, xadlDestinationInterfaceType, xadlEventType);
        dispatchEvent(e);
    }

    /**
     * Dispatch a {@link XADLRuntimeEvent}.
     * 
     * @param xadlRuntimeId
     * @param xadlRuntimeType
     */
    private void dispatchXADLRuntimeEvent(String xadlRuntimeId, String xadlBlueprintId,
            XADLRuntimeEventType xadlRuntimeType) {
        XADLRuntimeEvent e = new XADLRuntimeEvent(xadlRuntimeId, xadlBlueprintId, xadlRuntimeType);
        dispatchEvent(e);
    }

    /**
     * Dispatch a {@link XADLHostInstanceEvent} with dynamic injection of the
     * hostname.
     * 
     * @param xadlEventType
     */
    private void dispatchXADLHostInstanceEvent(XADLEventType xadlEventType) {
        String name = IdGenerator.getHostName();
        if (name != null) {
            dispatchXADLHostInstanceEvent(IdGenerator.getHostName(), xadlEventType);
        }
    }

    /**
     * Dispatch a {@link XADLHostInstanceEvent}.
     * 
     * @param description
     * @param xadlEventType
     */
    private void dispatchXADLHostInstanceEvent(String description, XADLEventType xadlEventType) {
        XADLHostInstanceEvent e = new XADLHostInstanceEvent(xadlEventType);
        e.setDescription(description);
        dispatchEvent(e);
    }

    /**
     * Dispatch a {@link XADLHostingEvent} for a component.
     * 
     * @param runtimeId
     * @param xadlEventType
     */
    private void dispatchXADLHostingEventForComponent(String runtimeId, XADLEventType xadlEventType) {
        XADLHostingEvent e = new XADLHostingEvent(xadlEventType);
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
        XADLHostingEvent e = new XADLHostingEvent(xadlEventType);
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
        IMyxContainer container = MyxUtils.resolvePath(mainContainer, path);
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
            IMyxBrick internal = container.getInternalBrick(brickName); 
            if (internal == null) {
                throw new IllegalArgumentException("No brick found with name: " + brickName + " at "
                        + MyxUtils.pathToString(path));
            }
            bricks.add(MyxUtils.getName(internal));
        }
        return bricks;
    }

    protected class RuntimeElement {
        public String runtimeId;
        public String blueprintId;
        public XADLElementType elementType;
        public Map<String, String> interfaceTypes = new HashMap<>();
        public Set<IMyxWeld> welds = new HashSet<>();
    }

}
