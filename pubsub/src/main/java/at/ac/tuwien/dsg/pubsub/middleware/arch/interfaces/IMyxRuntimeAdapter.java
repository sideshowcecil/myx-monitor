package at.ac.tuwien.dsg.pubsub.middleware.arch.interfaces;

import at.ac.tuwien.dsg.pubsub.middleware.arch.component.Dispatcher;
import at.ac.tuwien.dsg.pubsub.middleware.arch.component.PublisherEndpoint;
import at.ac.tuwien.dsg.pubsub.middleware.arch.component.SubscriberEndpoint;

public interface IMyxRuntimeAdapter {
    /**
     * Create a {@link PublisherEndpoint} and fully add it to the architecture.
     * 
     * @param publisherEndpointClassName
     * @param dispatcher
     */
    public void createPublisherEndpoint(String publisherEndpointClassName, Dispatcher<?> dispatcher);

    /**
     * Shutdown the {@link PublisherEndpoint} and fully remove it from the
     * architecture.
     * 
     * @param endpoint
     */
    public void shutdownPublisherEndpoint(PublisherEndpoint<?> endpoint);

    /**
     * Create a {@link SubscriberEndpoint} and add it to the architecture.
     * 
     * @param subscriberEndpointClassName
     * @param dispatcher
     */
    public void createSubscriberEndpoint(String subscriberEndpointClassName, Dispatcher<?> dispatcher);

    /**
     * Connect a {@link SubscriberEndpoint} fully to the architecture.
     * 
     * @param subscriber
     */
    public void wireSubscriberEndpoint(SubscriberEndpoint<?> subscriber);

    /**
     * Shutdown a {@link SubscriberEndpoint} and fully remove it from the
     * architecture.
     * 
     * @param endpoint
     */
    public void shutdownSubscriberEndpoint(SubscriberEndpoint<?> endpoint);
}
