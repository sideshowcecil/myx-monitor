package at.ac.tuwien.dsg.myx.monitor.comp;

import at.ac.tuwien.dsg.myx.util.MyxUtils;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.IMyxRuntime;

public class MyxRuntimeComponent extends AbstractMyxSimpleBrick {

    public static final IMyxName INTERFACE_NAME_IN_MYXRUNTIME = MyxUtils.createName("myxruntime");
    public static final IMyxName INTERFACE_NAME_OUT_EVENTMANAGER = MyxUtils.createName("event-manager");

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
        runtime = MyxUtils.getMonitoringImplementation().createRuntime();
    }

}
