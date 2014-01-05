package at.ac.tuwien.infosys.pubsub.middleware.arch.component;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IRegistry;
import at.ac.tuwien.infosys.pubsub.middleware.arch.myx.AbstractMyxSimpleBrick;
import at.ac.tuwien.infosys.pubsub.middleware.arch.myx.MessageDistributor;
import at.ac.tuwien.infosys.pubsub.middleware.arch.myx.MyxRuntime;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;

public final class Registry extends AbstractMyxSimpleBrick implements IRegistry {

    public static final IMyxName IN_IREGISTRY = MyxUtils.createName(IRegistry.class.getCanonicalName());

    private Map<String, MessageDistributor> _topics = new HashMap<>();

    @Override
    public Object getServiceObject(IMyxName arg0) {
        // if no interfaces are going in, always return null
        // in this case, we have an interface coming in
        if (arg0.equals(IN_IREGISTRY)) {
            return this;
        }
        return null;
    }

    @Override
    public void register(String topic, PublisherEndpoint<?> publisher) {
        synchronized (_topics) {
            if (_topics.containsKey(topic)) {
                throw new IllegalArgumentException("Topic '" + topic + "' is already registered");
            }
            MessageDistributor distributor = MyxRuntime.getInstance().createMessageDistributor(publisher);
            _topics.put(topic, distributor);
        }
    }

    @Override
    public void unregister(String topic, PublisherEndpoint<?> publisher) {
        synchronized (_topics) {
            if (!_topics.containsKey(topic)) {
                throw new IllegalArgumentException("Topic '" + topic + "' is not registered");
            }
            MessageDistributor distributor = _topics.remove(topic);
            MyxRuntime.getInstance().removeMessageDistributor(distributor);
        }
    }

    @Override
    public void register(String topic, SubscriberEndpoint<?> subscriber) {
        synchronized (_topics) {
            if (!_topics.containsKey(topic)) {
                throw new IllegalArgumentException("Topic '" + topic + "' is not registered");
            }
            MessageDistributor distributor = _topics.get(topic);
            MyxRuntime.getInstance().wireMessageDistributor(subscriber, distributor);
        }
    }

    @Override
    public void unregister(String topic, SubscriberEndpoint<?> subscriber) {
        synchronized (_topics) {
            if (!_topics.containsKey(topic)) {
                throw new IllegalArgumentException("Topic '" + topic + "' is not registered");
            }
            MessageDistributor distributor = _topics.get(topic);
            MyxRuntime.getInstance().unwireMessageDistributor(subscriber, distributor);
        }
    }

}
