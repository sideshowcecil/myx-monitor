package at.ac.tuwien.dsg.myx.monitor.comp;

import at.ac.tuwien.dsg.myx.monitor.MyxProperties;
import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.IMyxRuntime;

public class MyxRuntimeComponent extends AbstractMyxSimpleBrick {

    public static final IMyxName INTERFACE_NAME_IN_MYXRUNTIME = MyxMonitoringUtils.createName("myxruntime");
    public static final IMyxName INTERFACE_NAME_OUT_EVENTMANAGER = MyxMonitoringUtils.createName("event-manager");

    protected IMyxRuntime runtime;

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        if (interfaceName.equals(INTERFACE_NAME_IN_MYXRUNTIME)) {
            return runtime;
        }
        return null;
    }

    @Override
    public void init() {
        String archRuntimeId = MyxMonitoringUtils.getInitProperties(this).getProperty(MyxProperties.ARCHITECTURE_RUNTIME_ID,
                IdGenerator.generateArchitectureRuntimeId());
        String hostId = IdGenerator.getHostId();
        EventManager eventManager = (EventManager) MyxMonitoringUtils.getFirstRequiredServiceObject(this,
                INTERFACE_NAME_OUT_EVENTMANAGER);

        MyxMonitoringUtils.initMontioringImplementation(archRuntimeId, hostId, eventManager);
        runtime = MyxMonitoringUtils.getMonitoringImplementation().createRuntime();
    }

}
