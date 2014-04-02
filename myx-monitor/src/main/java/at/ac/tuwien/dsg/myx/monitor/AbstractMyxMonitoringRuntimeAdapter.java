package at.ac.tuwien.dsg.myx.monitor;

import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxRuntime;

public abstract class AbstractMyxMonitoringRuntimeAdapter extends AbstractMyxSimpleBrick {

    private IMyxRuntime myx = MyxMonitoringUtils.getMonitoringImplementation().createRuntime();

    protected IMyxRuntime getMyxRuntime() {
        return myx;
    }

}
