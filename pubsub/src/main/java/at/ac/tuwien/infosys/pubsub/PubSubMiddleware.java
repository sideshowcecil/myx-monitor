package at.ac.tuwien.infosys.pubsub;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.infosys.pubsub.middleware.PublisherListener;
import at.ac.tuwien.infosys.pubsub.middleware.SubscriberListener;
import at.ac.tuwien.infosys.pubsub.util.Tuple;

/**
 * Contains the basic logic for starting and stopping all kinds of Publishers
 * and Subscribers.
 * 
 * @author bernd.rathmanner
 * 
 */
public class PubSubMiddleware implements Runnable {

    private List<Tuple<PublisherListener<?>, SubscriberListener<?>>> listeners;

    /**
     * Basic constructor.
     */
    public PubSubMiddleware() {
        listeners = new ArrayList<>();
    }

    /**
     * Extended constructor with predefined list of listener pairs.
     * 
     * @param listeners
     */
    public PubSubMiddleware(
            List<Tuple<PublisherListener<?>, SubscriberListener<?>>> listeners) {
        this.listeners = listeners;
    }

    /**
     * Adds a listener pair composed of a PublisherListener and a
     * SubscriberListener.
     * 
     * @param pub
     * @param sub
     */
    public void addListener(PublisherListener<?> pub, SubscriberListener<?> sub) {
        listeners.add(new Tuple<PublisherListener<?>, SubscriberListener<?>>(
                pub, sub));
    }

    /**
     * Starts all PublisherListeners and SubscriberListeners so that publishers
     * and listeners may connect.
     */
    @Override
    public void run() {
        for (Tuple<PublisherListener<?>, SubscriberListener<?>> pubsub : listeners) {
            pubsub.getFst().start();
            pubsub.getSnd().start();
        }
    }

    /**
     * Shutdown all publishers and subscribers.
     */
    public void close() {
        for (Tuple<PublisherListener<?>, SubscriberListener<?>> pubsub : listeners) {
            pubsub.getFst().close();
            pubsub.getSnd().close();
        }
    }

}
