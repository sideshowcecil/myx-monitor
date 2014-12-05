package at.ac.tuwien.dsg.pubsub.subscriber.comp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.dsg.myx.monitor.AbstractVirtualExternalMyxSimpleBrick;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Message.Type;
import at.ac.tuwien.dsg.pubsub.message.topic.TopicFactory;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.ISubscriber;
import at.ac.tuwien.dsg.pubsub.middleware.myx.DynamicArchitectureModelProperties;
import at.ac.tuwien.dsg.pubsub.network.Endpoint;
import at.ac.tuwien.dsg.pubsub.subscriber.myx.MyxInterfaceNames;
import edu.uci.isr.myx.fw.IMyxName;

public abstract class Subscriber<E> extends AbstractVirtualExternalMyxSimpleBrick {

    private static Logger logger = LoggerFactory.getLogger(Subscriber.class);

    public static IMyxName OUT_ISUBSCRIBER = MyxInterfaceNames.ISUBSCRIBER;

    private TopicFactory.Type topicType;
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

        topicType = TopicFactory.Type.valueOf(initProps.getProperty("topicType", "GLOB"));
        for (String key : initProps.stringPropertyNames()) {
            if (key.startsWith("topicName")) {
                topics.add(initProps.getProperty(key));
            }
        }
        // if no topics are given set the default topic
        if (topics.isEmpty()) {
            topics.add("*");
        }

        executor = Executors.newSingleThreadExecutor();
        runnable = new Runnable() {
            @Override
            public void run() {
                logger.info("Connecting");
                endpoint = connect();
                if (endpoint != null) {
                    String connectionIdentifier = getExternalConnectionIdentifier();
                    dispatchExternalLinkConnectedEvent(
                            DynamicArchitectureModelProperties.SUBSCRIBER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE,
                            connectionIdentifier);
                    try {
                        logger.info("Sending topics");
                        // send the topics we subscribe to
                        endpoint.send(getTopicsMessage(topicType, topics));
                        logger.info("Consuming messages");
                        Message<E> msg;
                        do {
                            msg = endpoint.receive();
                            subscriber.consume(msg);
                        } while (msg.getType() != Message.Type.CLOSE && msg.getType() != Message.Type.ERROR);
                        if (msg.getType() == Type.CLOSE) {
                            logger.info("Close message received");
                        } else {
                            logger.info("Error message received");
                        }
                    } catch (IOException e) {
                    }
                    dispatchExternalLinkDisconnectedEvent(
                            DynamicArchitectureModelProperties.SUBSCRIBER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE,
                            connectionIdentifier);
                }
                logger.info("Exiting");
                System.exit(0);
            }
        };
    }

    @Override
    public void begin() {
        subscriber = MyxMonitoringUtils.<ISubscriber<E>> getFirstRequiredServiceObject(this, OUT_ISUBSCRIBER);
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
    protected abstract Message<E> getTopicsMessage(TopicFactory.Type topicType, List<String> topics);

    /**
     * Get the external connection id of the connected {@link Endpoint}.
     * 
     * @return
     */
    protected abstract String getExternalConnectionIdentifier();

}
