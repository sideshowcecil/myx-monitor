package at.ac.tuwien.infosys.pubsub;

import java.util.ArrayList;
import java.util.List;

import edu.uci.isr.myx.fw.MyxBrickCreationException;
import edu.uci.isr.myx.fw.MyxBrickLoadException;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.PublisherDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.SubscriberDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.myx.MyxRuntime;
import at.ac.tuwien.infosys.pubsub.util.Tuple;

/**
 * Contains the basic logic for starting and stopping all kinds of Publishers
 * and Subscribers.
 * 
 * @author bernd.rathmanner
 * 
 */
public class PubSubMiddleware implements Runnable {

    private List<Tuple<Class<? extends PublisherDispatcher<?>>, Class<? extends SubscriberDispatcher<?>>>> dispatchers;

    /**
     * Basic constructor.
     */
    public PubSubMiddleware() {
        dispatchers = new ArrayList<>();
    }

    /**
     * Extended constructor with predefined list of listener pairs.
     * 
     * @param dispatchers
     */
    public PubSubMiddleware(List<Tuple<Class<? extends PublisherDispatcher<?>>, Class<? extends SubscriberDispatcher<?>>>> dispatchers) {
        this.dispatchers = dispatchers;
    }

    /**
     * Adds a listener pair composed of a PublisherListener and a
     * SubscriberListener.
     * 
     * @param pub
     * @param sub
     */
    public void addDispatcher(Class<? extends PublisherDispatcher<?>> publisherDispatcher,
            Class<? extends SubscriberDispatcher<?>> subscriberDispatcher) {
        dispatchers.add(new Tuple<Class<? extends PublisherDispatcher<?>>, Class<? extends SubscriberDispatcher<?>>>(publisherDispatcher,
                subscriberDispatcher));
    }

    /**
     * Starts all PublisherListeners and SubscriberListeners so that publishers
     * and listeners may connect.
     */
    @Override
    public void run() {
        try {
            MyxRuntime.getInstance().boostrapArchitecture();
            for (Tuple<Class<? extends PublisherDispatcher<?>>, Class<? extends SubscriberDispatcher<?>>> pubsub : dispatchers) {
                MyxRuntime.getInstance().createDispatcher(pubsub.getFst().getName(), null, pubsub.getSnd().getName(), null);
            }
        } catch (MyxBrickLoadException | MyxBrickCreationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Shutdown all publishers and subscribers.
     */
    public void close() {
        System.exit(0);
    }

}
