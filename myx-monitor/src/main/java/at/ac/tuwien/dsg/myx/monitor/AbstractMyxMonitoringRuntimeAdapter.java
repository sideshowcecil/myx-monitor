package at.ac.tuwien.dsg.myx.monitor;

import at.ac.tuwien.dsg.myx.util.MyxUtils;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxRuntime;

public abstract class AbstractMyxMonitoringRuntimeAdapter extends AbstractMyxSimpleBrick {

    private IMyxRuntime myx = MyxUtils.getMonitoringImplementation().createRuntime();

    protected final IMyxRuntime getMyxRuntime() {
        return myx;
    }

}
