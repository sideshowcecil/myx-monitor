package at.ac.tuwien.dsg.myx.monitor;

import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEventType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLElementType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLLinkEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLRuntimeEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLRuntimeEventType;
import edu.uci.isr.myx.fw.IMyxBrickDescription;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.IMyxWeld;
import edu.uci.isr.myx.fw.MyxBasicRuntime;
import edu.uci.isr.myx.fw.MyxBrickCreationException;
import edu.uci.isr.myx.fw.MyxBrickLoadException;

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
        e.setXadlElementType(XADLElementType.COMPONENT); // TODO: how can we now
                                                         // if it is really a
                                                         // component
        eventManager.handle(e);
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
        XADLRuntimeEvent e = new XADLRuntimeEvent("1", brickName.getName(), XADLRuntimeEventType.BEGIN);
        e.setEventSourceId(this.getClass().getName());
        eventManager.handle(e);
    }

    @Override
    public void end(IMyxName[] path, IMyxName brickName) {
        // TODO Auto-generated method stub
        super.end(path, brickName);

        XADLRuntimeEvent e = new XADLRuntimeEvent("1", brickName.getName(), XADLRuntimeEventType.END);
        e.setEventSourceId(this.getClass().getName());
        eventManager.handle(e);
    }

}
