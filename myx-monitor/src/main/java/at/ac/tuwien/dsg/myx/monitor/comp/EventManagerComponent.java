package at.ac.tuwien.dsg.myx.monitor.comp;

import java.util.Properties;

import at.ac.tuwien.dsg.myx.monitor.MyxProperties;
import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
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
        Properties initProperties = MyxMonitoringUtils.getInitProperties(this);

        String architectureRuntimeId = initProperties.getProperty(MyxProperties.ARCHITECTURE_RUNTIME_ID,
                IdGenerator.generateArchitectureRuntimeId());
        String hostId = initProperties.getProperty(MyxProperties.ARCHITECTURE_HOST_ID, IdGenerator.getHostId());
        
        MyxMonitoringUtils.initEventManager(architectureRuntimeId, hostId);
        eventManager = MyxMonitoringUtils.getEventManager();
    }

}
