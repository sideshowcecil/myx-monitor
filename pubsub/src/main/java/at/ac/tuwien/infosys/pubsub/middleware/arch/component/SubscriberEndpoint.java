package at.ac.tuwien.infosys.pubsub.middleware.arch.component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IRegistry;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.ISubscriber;
import at.ac.tuwien.infosys.pubsub.middleware.arch.myx.AbstractMyxSimpleBrick;
import at.ac.tuwien.infosys.pubsub.middleware.arch.network.Endpoint;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;

public abstract class SubscriberEndpoint<E> extends AbstractMyxSimpleBrick implements ISubscriber<E> {

    private static Logger logger = LoggerFactory.getLogger(SubscriberEndpoint.class);

    public static final IMyxName OUT_IDISPATCHER = MyxUtils.createName(IDispatcher.class.getName());
    public static final IMyxName OUT_IREGISTRY = MyxUtils.createName(IRegistry.class.getName());
    public static final IMyxName IN_ISUBSCRIBER = MyxUtils.createName(ISubscriber.class.getName());

    protected IDispatcher<E> _dispatcher;
    protected IRegistry _registry;

    protected Endpoint<E> _endpoint;
    protected String _topic = null;

    private ExecutorService _executor;
    private Runnable _runnable;

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
        _executor = Executors.newSingleThreadExecutor();
        _runnable = new Runnable() {
            public void run() {
                // get the endpoint from the connected dispatcher
                logger.info("Getting endpoint from dispatcher");
                _endpoint = _dispatcher.getNextEndpoint();
                if (_endpoint != null) {
                    // wait for the topic name
                    logger.info("Waiting for topic");
                    _topic = waitForTopicName();
                    // if we do not get a topic name we assume the subscriber
                    // died
                    if (_topic != null) {
                        // check if the topic exists and register the endpoint
                        logger.error("Registering topic subscriber '" + _topic + "'");
                        try {
                            _registry.register(_topic, SubscriberEndpoint.this);
                        } catch (IllegalArgumentException ex) {
                            logger.error("Topic '" + _topic + "' is not registered");
                            sendErrorForNonExistingTopic();
                        }
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
            _dispatcher = (IDispatcher<E>) getFirstRequiredServiceObject(OUT_IDISPATCHER);
            _registry = (IRegistry) getFirstRequiredServiceObject(OUT_IREGISTRY);
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            return;
        }
        _executor.execute(_runnable);
    }

    @Override
    public void end() {
        if (_topic != null) {
            _registry.unregister(_topic, this);
        }
    }

    @Override
    public void send(Message<E> message) {
        _endpoint.send(message);
    }

    /**
     * Wait and return the topic name used by this subscriber.
     * 
     * @return
     */
    public abstract String waitForTopicName();

    /**
     * Send an error to the subscriber if the topic name is not registered.
     */
    public abstract void sendErrorForNonExistingTopic();

}
