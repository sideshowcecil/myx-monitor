package at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces;

import at.ac.tuwien.infosys.pubsub.middleware.arch.component.PublisherEndpoint;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.SubscriberEndpoint;

public interface IRegistry {
    /**
     * Register a new {@link PublisherEndpoint} with the given topic, including
     * topic existence validation.
     * 
     * @param topic
     * @param publisher
     * @throws IllegalArgumentException
     */
    public void register(String topic, PublisherEndpoint<?> publisher);

    /**
     * Unregister an existing {@link PublisherEndpoint} from the given topic,
     * including topic existence validation.
     * 
     * @param topic
     * @param publisher
     * @throws IllegalArgumentException
     */
    public void unregister(String topic, PublisherEndpoint<?> publisher);

    /**
     * Register a new {@link SubscriberEndpoint} with the given topic, including
     * topic existence validation.
     * 
     * @param topic
     * @param subscriber
     * @throws IllegalArgumentException
     */
    public void register(String topic, SubscriberEndpoint<?> subscriber);

    /**
     * Unregister an existing {@link SubscriberEndpoint} from the given topic,
     * including topic existence validation.
     * 
     * @param topic
     * @param subscriber
     * @throws IllegalArgumentException
     */
    public void unregister(String topic, SubscriberEndpoint<?> subscriber);
}
