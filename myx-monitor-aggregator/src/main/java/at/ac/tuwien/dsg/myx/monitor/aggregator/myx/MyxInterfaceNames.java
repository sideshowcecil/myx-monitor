package at.ac.tuwien.dsg.myx.monitor.aggregator.myx;

import at.ac.tuwien.dsg.myx.monitor.aggregator.model.ModelRoot;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import edu.uci.isr.myx.fw.IMyxName;

public interface MyxInterfaceNames extends at.ac.tuwien.dsg.pubsub.middleware.myx.MyxInterfaceNames {
    public static IMyxName MODEL_ROOT = MyxMonitoringUtils.createName(ModelRoot.class.getName());
}
