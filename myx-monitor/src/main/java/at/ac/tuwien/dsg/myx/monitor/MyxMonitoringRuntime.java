package at.ac.tuwien.dsg.myx.monitor;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.dsg.myx.MyxMonitoringUtils;
import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEventType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLElementType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLLinkEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLRuntimeEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLRuntimeEventType;
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

    public MyxMonitoringRuntime(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public void addBrick(IMyxName[] path, IMyxName brickName, IMyxBrickDescription brickDescription)
            throws MyxBrickLoadException, MyxBrickCreationException {
        super.addBrick(path, brickName, brickDescription);

        // TODO send event
        XADLEvent e = new XADLEvent("1", brickName.getName(), XADLEventType.ADD);
        e.setEventSourceId(this.getClass().getName());
        e.setXadlElementType(XADLElementType.COMPONENT); // TODO: how can we know
                                                         // if it is really a
                                                         // component
        eventManager.handle(e);
    }
    
    @Override
    public void addInterface(IMyxName[] path, IMyxName brickName, IMyxName interfaceName,
            IMyxInterfaceDescription interfaceDescription, EMyxInterfaceDirection interfaceDirection) {
        // TODO Auto-generated method stub
        super.addInterface(path, brickName, interfaceName, interfaceDescription, interfaceDirection);
    }

    @Override
    public void addWeld(IMyxWeld weld) {
        super.addWeld(weld);

        // TODO send event
        XADLLinkEvent e = new XADLLinkEvent("1", weld.getRequiredBrickName().getName(), weld.getRequiredInterfaceName()
                .getName(), weld.getProvidedBrickName().getName(), weld.getProvidedInterfaceName().getName(),
                XADLEventType.ADD);
        e.setEventSourceId(this.getClass().getName());
        eventManager.handle(e);
    }

    @Override
    public void begin(IMyxName[] path, IMyxName brickName) {
        super.begin(path, brickName);

        // TODO send event
        for (IMyxName b : getBrickNames(path, brickName)) {
            XADLRuntimeEvent e = new XADLRuntimeEvent("1", b.getName(), XADLRuntimeEventType.BEGIN);
            e.setEventSourceId(this.getClass().getName());
            eventManager.handle(e);
        }
    }

    @Override
    public void end(IMyxName[] path, IMyxName brickName) {
        super.end(path, brickName);

        // TODO send event
        for (IMyxName b : getBrickNames(path, brickName)) {
            XADLRuntimeEvent e = new XADLRuntimeEvent("1", b.getName(), XADLRuntimeEventType.END);
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
