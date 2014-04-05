package at.ac.tuwien.dsg.pubsub.subscriber.comp;

import at.ac.tuwien.dsg.pubsub.subscriber.interfaces.ISubscriber;
import at.ac.tuwien.dsg.pubsub.subscriber.myx.MyxInterfaceNames;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;

public abstract class MessageConsumer<E> extends AbstractMyxSimpleBrick implements ISubscriber<E> {

    public static IMyxName IN_ISUBSCRIBER = MyxInterfaceNames.ISUBSCRIBER;

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        if (interfaceName.equals(IN_ISUBSCRIBER)) {
            return this;
        }
        return null;
    }

}
