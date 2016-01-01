package at.ac.tuwien.dsg.pubsub.subscriber.myx;

import at.ac.tuwien.dsg.myx.util.MyxUtils;
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
    public static IMyxName ISUBSCRIBER = MyxUtils.createName(ISubscriber.class.getName());
}
