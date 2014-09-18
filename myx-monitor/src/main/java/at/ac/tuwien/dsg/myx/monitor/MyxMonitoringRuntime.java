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

public class MyxMonitoringRuntime extends MyxBasicRuntime {

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

    public MyxMonitoringRuntime(EventManager eventManager) {
        this.eventManager = eventManager;

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                List<IMyxName> bricks = MyxMonitoringRuntime.this.getBrickNames(null, null);
                // send runtime events
                for (IMyxName brick : bricks) {
                    for (IMyxName b : getBrickNames(null, brick)) {
                        String runtimeId = b.getName();
                        if (runtime2blueprint.containsKey(runtimeId)) {
                            // send event
                            dispatchXADLRuntimeEvent(runtimeId, runtime2blueprint.get(runtimeId),
                                    XADLRuntimeEventType.END);
                        }
                    }
                }
                // send xadl- and hosting events
                for (IMyxName brick : bricks) {
                    for (IMyxName b : getBrickNames(null, brick)) {
                        String runtimeId = b.getName();
                        if (runtime2blueprint.containsKey(runtimeId)) {
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
            }
        });
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
            String runtimeId = b.getName();
            if (runtime2blueprint.containsKey(runtimeId)) {
                // send event
                dispatchXADLRuntimeEvent(runtimeId, runtime2blueprint.get(runtimeId), XADLRuntimeEventType.BEGIN);
            }
        }
    }

    @Override
    public void end(IMyxName[] path, IMyxName brickName) {
        super.end(path, brickName);

        for (IMyxName b : getBrickNames(path, brickName)) {
            String runtimeId = b.getName();
            if (runtime2blueprint.containsKey(runtimeId)) {
                // send event
                dispatchXADLRuntimeEvent(runtimeId, runtime2blueprint.get(runtimeId), XADLRuntimeEventType.END);
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
        XADLEvent e = new XADLEvent(xadlRuntimeId, xadlElementId, xadlEventType);
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
        String sourceRuntimeId = weld.getRequiredBrickName().getName(), sourceInterfaceName = weld
                .getRequiredInterfaceName().getName();
        String destinationRuntimeId = weld.getProvidedBrickName().getName(), destinationInterfaceName = weld
                .getProvidedInterfaceName().getName();

        Tuple<String, String> sourceIntf = new Tuple<>(sourceRuntimeId, sourceInterfaceName);
        Tuple<String, String> destinationIntf = new Tuple<>(destinationRuntimeId, destinationInterfaceName);

        if (runtime2blueprint.containsKey(sourceRuntimeId) && runtime2blueprint.containsKey(destinationRuntimeId)) {
            String sourceBlueprintId = runtime2blueprint.get(sourceRuntimeId), destinationBlueprintId = runtime2blueprint
                    .get(destinationRuntimeId);
            if (interfaces.containsKey(sourceIntf) && interfaces.containsKey(destinationIntf)) {
                String sourceInterfaceType = interfaces.get(sourceIntf), destinationInterfaceType = interfaces
                        .get(destinationIntf);
                // send event
                dispatchXADLLinkEvent(sourceRuntimeId, sourceBlueprintId, sourceInterfaceType, destinationRuntimeId,
                        destinationBlueprintId, destinationInterfaceType, xadlEventType);
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
