package at.ac.tuwien.dsg.pubsub.subscriber.comp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.ac.tuwien.dsg.myx.monitor.AbstractVirtualExternalMyxSimpleBrick;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Topic;
import at.ac.tuwien.dsg.pubsub.middleware.myx.DynamicArchitectureModelProperties;
import at.ac.tuwien.dsg.pubsub.network.Endpoint;
import at.ac.tuwien.dsg.pubsub.subscriber.interfaces.ISubscriber;
import at.ac.tuwien.dsg.pubsub.subscriber.myx.MyxInterfaceNames;
import edu.uci.isr.myx.fw.IMyxName;

public abstract class Subscriber<E> extends AbstractVirtualExternalMyxSimpleBrick {

    public static IMyxName OUT_ISUBSCRIBER = MyxInterfaceNames.ISUBSCRIBER;

    private Topic.Type topicType;
    private List<String> topics = new ArrayList<>();

    protected Endpoint<E> endpoint;
    private ISubscriber<E> subscriber;

    private ExecutorService executor;
    private Runnable runnable;

    @Override
    public Object getServiceObject(@SuppressWarnings("unused") IMyxName interfaceName) {
        return null;
    }

    @Override
    public void init() {
        Properties initProps = MyxMonitoringUtils.getInitProperties(this);

        topicType = Topic.Type.valueOf(initProps.getProperty("topicType", "REGEX"));
        for (String key : initProps.stringPropertyNames()) {
            if (key.startsWith("topicName")) {
                topics.add(initProps.getProperty(key));
            }
        }
        // if no topics are given set the default topic
        if (topics.isEmpty()) {
            topics.add(".*");
        }

        executor = Executors.newSingleThreadExecutor();
        runnable = new Runnable() {
            @Override
            public void run() {
                endpoint = connect();
                if (endpoint != null) {
                    String connectionIdentifier = getExternalConnectionIdentifier();
                    dispatchExternalLinkConnectedEvent(
                            DynamicArchitectureModelProperties.SUBSCRIBER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_NAME,
                            DynamicArchitectureModelProperties.SUBSCRIBER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE,
                            connectionIdentifier);
                    try {
                        // send the topics we subscribe to
                        endpoint.send(getTopicsMessage(topicType, topics));
                        Message<E> msg;
                        do {
                            msg = endpoint.receive();
                            subscriber.consume(msg);
                        } while (msg.getType() != Message.Type.CLOSE && msg.getType() != Message.Type.ERROR);
                    } catch (IOException e) {
                    }
                    dispatchExternalLinkDisconnectedEvent(
                            DynamicArchitectureModelProperties.SUBSCRIBER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_NAME,
                            DynamicArchitectureModelProperties.SUBSCRIBER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE,
                            connectionIdentifier);
                }
                System.exit(0);
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public void begin() {
        subscriber = (ISubscriber<E>) MyxMonitoringUtils.getFirstRequiredServiceObject(this, OUT_ISUBSCRIBER);
        if (subscriber == null) {
            throw new RuntimeException("Interface " + OUT_ISUBSCRIBER + " returned null");
        }
        executor.execute(runnable);
    }

    /**
     * Connect to the middleware or similar and return the endpoint.
     */
    protected abstract Endpoint<E> connect();

    /**
     * Get the message used to subscribe to specific topics.
     * 
     * @param topicType
     * @param topics
     * @return
     */
    protected abstract Message<E> getTopicsMessage(Topic.Type topicType, List<String> topics);

    /**
     * Get the external connection id of the connected {@link Endpoint}.
     * 
     * @return
     */
    protected abstract String getExternalConnectionIdentifier();

}
