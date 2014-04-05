package at.ac.tuwien.dsg.pubsub.publisher.myx;

import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.publisher.interfaces.IPublisher;
import edu.uci.isr.myx.fw.IMyxName;

/**
 * Interface containing the names of the interfaces used by myx.
 * 
 * @author bernd.rathmanner
 * 
 */
public interface MyxInterfaceNames {
    // interfaces
    public static IMyxName IPUBLISHER = MyxMonitoringUtils.createName(IPublisher.class.getName());
}
