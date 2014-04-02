package at.ac.tuwien.dsg.pubsub.middleware.myx;

import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.IDispatcher;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.IMyxRuntimeAdapter;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.ISubscriber;
import edu.uci.isr.myx.fw.IMyxName;

public interface MyxNames {
    // interfaces
    public static IMyxName IDISPATCHER = MyxMonitoringUtils.createName(IDispatcher.class.getName());
    public static IMyxName IMYX_ADAPTER = MyxMonitoringUtils.createName(IMyxRuntimeAdapter.class.getName());
    public static IMyxName ISUBSCRIBER = MyxMonitoringUtils.createName(ISubscriber.class.getName());
}
