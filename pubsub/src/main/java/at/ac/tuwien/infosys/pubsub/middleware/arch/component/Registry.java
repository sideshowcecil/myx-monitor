package at.ac.tuwien.infosys.pubsub.middleware.arch.component;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IRegistry;
import at.ac.tuwien.infosys.pubsub.middleware.arch.myx.AbstractMyxSimpleBrick;
import at.ac.tuwien.infosys.pubsub.middleware.arch.myx.MyxRuntime;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;

public final class Registry extends AbstractMyxSimpleBrick implements IRegistry {

    public static final IMyxName IN_IREGISTRY = MyxUtils.createName(IRegistry.class.getName());

    private Map<String, PublisherEndpoint<?>> _topics = new HashMap<>();

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
            _topics.put(topic, publisher);
        }
    }

    @Override
    public void unregister(String topic, PublisherEndpoint<?> publisher) {
        synchronized (_topics) {
            if (!_topics.containsKey(topic)) {
                throw new IllegalArgumentException("Topic '" + topic + "' is not registered");
            }
            if (_topics.get(topic) != publisher) {
                throw new IllegalArgumentException("Topic '" + topic + "' is registered with another endpoint");
            }
            _topics.remove(topic);
            MyxRuntime.getInstance().shutdownEndpoint(publisher);
        }
    }

    @Override
    public void register(String topic, SubscriberEndpoint<?> subscriber) {
        synchronized (_topics) {
            if (!_topics.containsKey(topic)) {
                throw new IllegalArgumentException("Topic '" + topic + "' is not registered");
            }
            PublisherEndpoint<?> publisher = _topics.get(topic);
            MyxRuntime.getInstance().wireEndpoint(subscriber, publisher);
        }
    }

    @Override
    public void unregister(String topic, SubscriberEndpoint<?> subscriber) {
        synchronized (_topics) {
            if (!_topics.containsKey(topic)) {
                throw new IllegalArgumentException("Topic '" + topic + "' is not registered");
            }
            PublisherEndpoint<?> publisher = _topics.get(topic);
            MyxRuntime.getInstance().unwireEndpoint(subscriber, publisher);
            MyxRuntime.getInstance().shutdownEndpoint(subscriber);
        }
    }

}
