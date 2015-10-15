package at.ac.tuwien.dsg.myx.monitor.aggregator.comp;

import at.ac.tuwien.dsg.myx.monitor.aggregator.hosted.HostedInstanceRuntimeManager;
import at.ac.tuwien.dsg.myx.monitor.aggregator.model.ModelRoot;
import at.ac.tuwien.dsg.myx.monitor.aggregator.myx.MyxInterfaceNames;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.util.MyxUtils;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.ISubscriber;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;

public class HostedRuntimeManagerComponent extends AbstractMyxSimpleBrick {

    public static final IMyxName IN_ISUBSCRIBER = MyxInterfaceNames.ISUBSCRIBER;
    public static final IMyxName OUT_MODEL_ROOT = MyxInterfaceNames.MODEL_ROOT;

    private ISubscriber<Event> hostedRuntimeManager;

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        if (interfaceName.equals(IN_ISUBSCRIBER)) {
            return hostedRuntimeManager;
        }
        return null;
    }

    @Override
    public void init() {
        hostedRuntimeManager = new HostedInstanceRuntimeManager(
                MyxUtils.<ModelRoot> getFirstRequiredServiceObject(this, OUT_MODEL_ROOT));
    }

}
