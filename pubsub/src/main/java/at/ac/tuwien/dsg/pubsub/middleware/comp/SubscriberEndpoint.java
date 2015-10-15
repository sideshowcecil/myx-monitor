package at.ac.tuwien.dsg.pubsub.middleware.comp;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.dsg.myx.monitor.AbstractVirtualExternalMyxSimpleBrick;
import at.ac.tuwien.dsg.myx.util.MyxUtils;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.topic.Topic;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.IDispatcher;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.IMyxRuntimeAdapter;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.ISubscriber;
import at.ac.tuwien.dsg.pubsub.middleware.myx.DynamicArchitectureModelProperties;
import at.ac.tuwien.dsg.pubsub.middleware.myx.MyxInterfaceNames;
import at.ac.tuwien.dsg.pubsub.network.Endpoint;
import edu.uci.isr.myx.fw.IMyxName;

public abstract class SubscriberEndpoint<E> extends AbstractVirtualExternalMyxSimpleBrick implements ISubscriber<E> {

    private static Logger logger = LoggerFactory.getLogger(SubscriberEndpoint.class);

    public static final IMyxName IN_ISUBSCRIBER = MyxInterfaceNames.ISUBSCRIBER;
    public static final IMyxName OUT_IDISPATCHER = MyxInterfaceNames.IDISPATCHER;
    public static final IMyxName OUT_MYX_ADAPTER = MyxInterfaceNames.IMYX_ADAPTER;

    protected IDispatcher<E> dispatcher;
    protected IMyxRuntimeAdapter myxAdapter;

    protected Endpoint<E> endpoint;
    protected Set<Topic> topics = null;

    private ExecutorService executor;
    private Runnable runnable;

    private String connectionIdentifier = null;
    private volatile boolean shutdown = false;

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        if (interfaceName.equals(IN_ISUBSCRIBER)) {
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
                    // send event that the virtual external interface was
                    // connected
                    connectionIdentifier = getExternalConnectionIdentifier();
                    dispatchExternalLinkConnectedEvent(
                            DynamicArchitectureModelProperties.SUBSCRIBER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE,
                            connectionIdentifier);
                    // wait for the topic name
                    logger.info("Waiting for topics");
                    topics = getTopics();
                    // if we do not get topics name we assume the endpoint died
                    if (topics != null) {
                        // connect to MessageDistributor
                        myxAdapter.wireSubscriberEndpoint(SubscriberEndpoint.this);
                        logger.info("Topics received, fully connected");
                        return;
                    }
                }
                myxAdapter.shutdownSubscriberEndpoint(SubscriberEndpoint.this);
                if (connectionIdentifier != null) {
                    // send event that the virtual external interface was
                    // disconnected
                    dispatchExternalLinkDisconnectedEvent(
                            DynamicArchitectureModelProperties.SUBSCRIBER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE,
                            connectionIdentifier);
                }
            }
        };
    }

    @Override
    public void begin() {
        dispatcher = MyxUtils.<IDispatcher<E>> getFirstRequiredServiceObject(this, OUT_IDISPATCHER);
        myxAdapter = MyxUtils.<IMyxRuntimeAdapter> getFirstRequiredServiceObject(this, OUT_MYX_ADAPTER);
        executor.execute(runnable);
    }

    @Override
    public void end() {
        executor.shutdownNow();
    }

    @Override
    public void consume(Message<E> message) {
        if (!shutdown) {
            // we only send the message to the subscriber if the subscribed
            // topic matches
            if (matches(message.getTopic())) {
                try {
                    endpoint.send(message);
                } catch (IOException e) {
                    endpoint.close();
                    shutdown = true;
                    if (connectionIdentifier != null) {
                        // send event that the virtual external interface was
                        // disconnected
                        dispatchExternalLinkDisconnectedEvent(
                                DynamicArchitectureModelProperties.SUBSCRIBER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE,
                                connectionIdentifier);
                    }
                    myxAdapter.shutdownSubscriberEndpoint(this);
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
        if (topics != null) {
            for (Topic t : topics) {
                if (t.matches(topic)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get the topics for which the subscriber wants notifications.
     * 
     * @return
     */
    public abstract Set<Topic> getTopics();

    /**
     * Get the external connection id of the connected {@link Endpoint}.
     * 
     * @return
     */
    protected abstract String getExternalConnectionIdentifier();

}
