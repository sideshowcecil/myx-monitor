package at.ac.tuwien.dsg.pubsub.middleware.myx;

import at.ac.tuwien.dsg.myx.util.MyxUtils;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.IDispatcher;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.IMyxRuntimeAdapter;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.ISubscriber;
import edu.uci.isr.myx.fw.IMyxName;

/**
 * Interface containing the names of the interfaces used by myx.
 * 
 * @author bernd.rathmanner
 * 
 */
public interface MyxInterfaceNames {
    // interfaces
    public static IMyxName IDISPATCHER = MyxUtils.createName(IDispatcher.class.getName());
    public static IMyxName IMYX_ADAPTER = MyxUtils.createName(IMyxRuntimeAdapter.class.getName());
    public static IMyxName ISUBSCRIBER = MyxUtils.createName(ISubscriber.class.getName());
    public static IMyxName VIRTUAL_PUBLISHER_ENDPOINT = MyxUtils
            .createName(DynamicArchitectureModelProperties.PUBLISHER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_NAME);
    public static IMyxName VIRTUAL_SUBSCRIBER_ENDPOINT = MyxUtils
            .createName(DynamicArchitectureModelProperties.SUBSCRIBER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_NAME);
}
