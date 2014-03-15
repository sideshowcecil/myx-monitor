package at.ac.tuwien.dsg.myx.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import at.ac.tuwien.dsg.myx.fw.IMyxInitPropertiesInterfaceDescription;
import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLElementType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEventType;
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

    protected String architectureRuntimeId;
    protected EventManager eventManager;

    /**
     * This map is currently not used, but can be used if we need the blueprint
     * id on XADLLinkEvents or XADLRuntimeEvents.
     */
    protected Map<String, String> runtime2blueprint = new HashMap<>();
    protected Map<Tuple<String, String>, String> interfaces = new HashMap<>();

    public MyxMonitoringRuntime(String architectureRuntimeId, EventManager eventManager) {
        this.architectureRuntimeId = architectureRuntimeId;
        this.eventManager = eventManager;
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
            // send event
            XADLEvent e = new XADLEvent(architectureRuntimeId, runtimeId, blueprintId, XADLEventType.ADD);
            e.setEventSourceId(this.getClass().getName());
            e.setXadlElementType((XADLElementType) initProperties.get(MyxProperties.ARCHITECTURE_BRICK_TYPE));
            eventManager.handle(e);

            // save the id
            runtime2blueprint.put(runtimeId, blueprintId);
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
    public void addWeld(IMyxWeld weld) {
        super.addWeld(weld);

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
            XADLLinkEvent e = new XADLLinkEvent(architectureRuntimeId, requiredBrickName, requiredInterfaceName,
                    requiredInterfaceType, providedBrickName, providedInterfaceName, providedInterfaceType,
                    XADLEventType.ADD);
            e.setEventSourceId(this.getClass().getName());
            eventManager.handle(e);
        }
    }

    @Override
    public void begin(IMyxName[] path, IMyxName brickName) {
        super.begin(path, brickName);

        for (IMyxName b : getBrickNames(path, brickName)) {
            // send event
            String runtimeId = b.getName();
            XADLRuntimeEvent e = new XADLRuntimeEvent(architectureRuntimeId, runtimeId, XADLRuntimeEventType.BEGIN);
            e.setEventSourceId(this.getClass().getName());
            eventManager.handle(e);
        }
    }

    @Override
    public void end(IMyxName[] path, IMyxName brickName) {
        super.end(path, brickName);

        for (IMyxName b : getBrickNames(path, brickName)) {
            // send event
            String runtimeId = b.getName();
            XADLRuntimeEvent e = new XADLRuntimeEvent(architectureRuntimeId, runtimeId, XADLRuntimeEventType.END);
            e.setEventSourceId(this.getClass().getName());
            eventManager.handle(e);
        }
    }

    /**
     * Get all bricks that get instantiated by the lifecycle method.
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
