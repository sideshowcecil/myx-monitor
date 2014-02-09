package at.ac.tuwien.infosys.pubsub.middleware.arch.component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.message.Message.Type;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IRegistry;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.ISubscriber;
import at.ac.tuwien.infosys.pubsub.middleware.arch.myx.AbstractMyxSimpleBrick;
import at.ac.tuwien.infosys.pubsub.middleware.arch.network.Endpoint;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;

public abstract class PublisherEndpoint<E> extends AbstractMyxSimpleBrick {

    public static final IMyxName OUT_IDISPATCHER = MyxUtils.createName(IDispatcher.class.getName());
    public static final IMyxName OUT_IREGISTRY = MyxUtils.createName(IRegistry.class.getName());
    public static final IMyxName OUT_ISUBSCRIBER = MyxUtils.createName(ISubscriber.class.getName());

    protected IDispatcher<E> _dispatcher;
    protected IRegistry _registry;
    protected ISubscriber<E> _subscriber;

    protected Endpoint<E> _endpoint;
    protected String _topic = null;

    private ExecutorService _executor;
    private Runnable _runnable;

    @Override
    public Object getServiceObject(IMyxName arg0) {
        return null;
    }

    @Override
    public void init() {
        _executor = Executors.newSingleThreadExecutor();
        _runnable = new Runnable() {
            @SuppressWarnings("unchecked")
            public void run() {
                // get the endpoint from the connected dispatcher
                _endpoint = _dispatcher.getNextEndpoint();
                if (_endpoint != null) {
                    // wait for the topic name
                    _topic = waitForTopicName();
                    // if we do not get a topic name we assume the publisher
                    // died
                    if (_topic != null) {
                        // check if the topic exists and register the endpoint
                        try {
                            _registry.register(_topic, PublisherEndpoint.this);
                            sendTopicAcnowledgement();
                        } catch (IllegalArgumentException ex) {
                            sendErrorForExistingTopic();
                            return;
                        }
                        // initialize the subscriber
                        _subscriber = (ISubscriber<E>) getFirstRequiredServiceObject(OUT_ISUBSCRIBER);
                        // wait for messages and forward them to the subscribers
                        boolean run = true;
                        while (run) {
                            // wait for a message
                            Message<E> msg = _endpoint.receive();
                            // if we receive a CLOSE or ERROR message we
                            // shutdown the endpoint
                            if (msg.getType() == Type.CLOSE || msg.getType() == Type.ERROR) {
                                run = false;
                            }
                            _subscriber.send(msg);
                        }
                        // unregister the endpoint
                        try {
                            _registry.unregister(_topic, PublisherEndpoint.this);
                        } catch (IllegalArgumentException e) {
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
        _executor.shutdownNow();
        if (_topic != null) {
            _registry.unregister(_topic, this);
        }
    }

    /**
     * Wait and return the topic name used by this publisher.
     * 
     * @return
     */
    public abstract String waitForTopicName();

    /**
     * Send an acknowledgment to the publisher that the topic name was successfuly registered.
     */
    public abstract void sendTopicAcnowledgement();

    /**
     * Send an error to the publisher if the topic name is already registered.
     */
    public abstract void sendErrorForExistingTopic();

}
