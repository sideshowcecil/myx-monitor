package at.ac.tuwien.infosys.pubsub;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.infosys.pubsub.middleware.PublisherListener;
import at.ac.tuwien.infosys.pubsub.middleware.SubscriberListener;
import at.ac.tuwien.infosys.pubsub.util.Tuple;

public class PubSubMiddleware implements Runnable {

    private List<Tuple<PublisherListener<?>, SubscriberListener<?>>> listeners;

    public PubSubMiddleware() {
        listeners = new ArrayList<>();
    }

    public PubSubMiddleware(
            List<Tuple<PublisherListener<?>, SubscriberListener<?>>> listeners) {
        this.listeners = listeners;
    }

    public void addListener(PublisherListener<?> pub, SubscriberListener<?> sub) {
        listeners.add(new Tuple<PublisherListener<?>, SubscriberListener<?>>(
                pub, sub));
    }

    @Override
    public void run() {
        for (Tuple<PublisherListener<?>, SubscriberListener<?>> pubsub : listeners) {
            pubsub.getFst().start();
            pubsub.getSnd().start();
        }
    }
    
    public void close() {
        for (Tuple<PublisherListener<?>, SubscriberListener<?>> pubsub : listeners) {
            pubsub.getFst().close();
            pubsub.getSnd().close();
        }
    }

}
