package at.ac.tuwien.dsg.pubsub.publisher.comp;

import edu.uci.isr.myx.fw.IMyxName;
import at.ac.tuwien.dsg.myx.monitor.AbstractVirtualExternalMyxSimpleBrick;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.IPublisher;
import at.ac.tuwien.dsg.pubsub.publisher.myx.MyxInterfaceNames;

public abstract class MessageCreator<E> extends AbstractVirtualExternalMyxSimpleBrick {

    public static final IMyxName OUT_PUBLISHER = MyxInterfaceNames.IPUBLISHER;

    protected IPublisher<E> publisher;

    @Override
    public Object getServiceObject(@SuppressWarnings("unused") IMyxName interfaceName) {
        return null;
    }

    @Override
    public void begin() {
        publisher = MyxMonitoringUtils.<IPublisher<E>> getFirstRequiredServiceObject(this, OUT_PUBLISHER);
    }

}
