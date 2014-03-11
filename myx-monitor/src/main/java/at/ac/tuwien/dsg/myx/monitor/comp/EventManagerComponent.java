package at.ac.tuwien.dsg.myx.monitor.comp;

import at.ac.tuwien.dsg.myx.MyxMonitoringUtils;
import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.EventManagerImpl;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;

public class EventManagerComponent extends AbstractMyxSimpleBrick {

    public static final IMyxName INTERFACE_NAME_IN_EVENTMANAGER = MyxMonitoringUtils.createName("event-manager");
    
    protected EventManager eventManager;

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        if (interfaceName.equals(INTERFACE_NAME_IN_EVENTMANAGER)) {
            return eventManager;
        }
        return null;
    }
    
    @Override
    public void init() {
        eventManager = new EventManagerImpl();
    }

}
