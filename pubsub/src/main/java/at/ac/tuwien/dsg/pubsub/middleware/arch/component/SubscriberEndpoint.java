package at.ac.tuwien.dsg.pubsub.middleware.arch.component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Topic;
import at.ac.tuwien.dsg.pubsub.middleware.arch.interfaces.IDispatcher;
import at.ac.tuwien.dsg.pubsub.middleware.arch.interfaces.ISubscriber;
import at.ac.tuwien.dsg.pubsub.middleware.arch.myx.AbstractMyxSimpleBrick;
import at.ac.tuwien.dsg.pubsub.middleware.arch.myx.MyxRuntime;
import at.ac.tuwien.dsg.pubsub.middleware.arch.network.Endpoint;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;

public abstract class SubscriberEndpoint<E> extends AbstractMyxSimpleBrick implements ISubscriber<E> {

    private static Logger logger = LoggerFactory.getLogger(SubscriberEndpoint.class);

    public static final IMyxName OUT_IDISPATCHER = MyxUtils.createName(IDispatcher.class.getName());
    public static final IMyxName IN_ISUBSCRIBER = MyxUtils.createName(ISubscriber.class.getName());

    protected IDispatcher<E> dispatcher;

    protected Endpoint<E> endpoint;
    protected List<Topic> topics = null;

    private ExecutorService executor;
    private Runnable runnable;

    private boolean shutdown = false;

    @Override
    public Object getServiceObject(IMyxName arg0) {
        // if no interfaces are going in, always return null
        // in this case, we have an interface coming in
        if (arg0.equals(IN_ISUBSCRIBER)) {
            return this;
        }
        return null;
    }

    @Override
    public void init() {
        executor = Executors.newSingleThreadExecutor();
        runnable = new Runnable() {
            public void run() {
                // get the endpoint from the connected dispatcher
                logger.info("Getting endpoint from dispatcher");
                endpoint = dispatcher.getNextEndpoint();
                if (endpoint != null) {
                    // wait for the topic name
                    logger.info("Waiting for topics");
                    topics = getTopics();
                    // if we do not get topics name we assume the endpoint died
                    if (topics != null) {
                        // connect to MessageDistributor
                        MyxRuntime.getInstance().wireEndpoint(SubscriberEndpoint.this);
                    }
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public void begin() {
        try {
            // connect interfaces
            dispatcher = (IDispatcher<E>) getFirstRequiredServiceObject(OUT_IDISPATCHER);
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            return;
        }
        executor.execute(runnable);
    }

    @Override
    public void end() {
        executor.shutdownNow();
    }

    @Override
    public void send(Message<E> message) {
        if (!shutdown) {
            // we only send the message to the subscriber if the subscribed
            // topic matches
            if (matches(message.getTopic())) {
                try {
                    endpoint.send(message);
                } catch (IOException e) {
                    // shutdown the endpoint
                    MyxRuntime.getInstance().shutdownEndpoint(this);
                    shutdown = true;
                }
            }
        }
    }

    /**
     * Return if the given topic matches any of the subscribed topics.
     * 
     * @param topic
     * @return
     */
    private boolean matches(String topic) {
        for (Topic t : topics) {
            if (t.matches(topic)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the topics for which the subscriber wants notifications.
     * 
     * @return
     */
    public abstract List<Topic> getTopics();

}
