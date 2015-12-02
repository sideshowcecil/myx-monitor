package at.ac.tuwien.dsg.pubsub.publisher.comp;

import at.ac.tuwien.dsg.myx.util.MyxUtils;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.IPublisher;
import at.ac.tuwien.dsg.pubsub.publisher.myx.MyxInterfaceNames;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;

public abstract class MessageCreator<E> extends AbstractMyxSimpleBrick {

    public static final IMyxName OUT_PUBLISHER = MyxInterfaceNames.IPUBLISHER;

    protected IPublisher<E> publisher;

    @Override
    public Object getServiceObject(@SuppressWarnings("unused") IMyxName interfaceName) {
        return null;
    }

    @Override
    public void begin() {
        publisher = MyxUtils.<IPublisher<E>> getFirstRequiredServiceObject(this, OUT_PUBLISHER);
    }

}
