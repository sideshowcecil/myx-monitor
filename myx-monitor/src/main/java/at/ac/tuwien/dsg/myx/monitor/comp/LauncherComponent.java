package at.ac.tuwien.dsg.myx.monitor.comp;

import at.ac.tuwien.dsg.myx.monitor.aim.Launcher;
import at.ac.tuwien.dsg.myx.monitor.aim.LauncherImpl;
import at.ac.tuwien.dsg.myx.monitor.model.ModelRoot;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.IMyxRuntime;

public class LauncherComponent extends AbstractMyxSimpleBrick {

    public static final IMyxName INTERFACE_NAME_IN_LAUNCHER = MyxMonitoringUtils.createName("launcher");
    public static final IMyxName INTERFACE_NAME_OUT_MYXRUNTIME = MyxMonitoringUtils.createName("myx-runtime");
    public static final IMyxName INTERFACE_NAME_OUT_MODELROOT = MyxMonitoringUtils.createName("model-root");

    protected Launcher launcher;

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        if (interfaceName.equals(INTERFACE_NAME_IN_LAUNCHER)) {
            return launcher;
        }
        return null;
    }

    @Override
    public void init() {
        IMyxRuntime myx = MyxMonitoringUtils.<IMyxRuntime> getFirstRequiredServiceObject(this,
                INTERFACE_NAME_OUT_MYXRUNTIME);
        ModelRoot modelRoot = MyxMonitoringUtils.<ModelRoot> getFirstRequiredServiceObject(this,
                INTERFACE_NAME_OUT_MODELROOT);
        launcher = new LauncherImpl(myx, modelRoot);
    }

}
