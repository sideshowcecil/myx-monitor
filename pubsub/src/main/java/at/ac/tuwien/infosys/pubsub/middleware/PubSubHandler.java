package at.ac.tuwien.infosys.pubsub.middleware;

import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.message.Message.Type;
import at.ac.tuwien.infosys.pubsub.network.MessageReceiver;
import at.ac.tuwien.infosys.pubsub.network.MessageSender;

/**
 * This abstract class contains the logic of the publish-subscribe pattern. It
 * forwards all incoming messages from the publisher to all connected
 * subscribers.
 * 
 * @author bernd.rathmanner
 * 
 * @param <E>
 *            resembles the message data.
 */
public abstract class PubSubHandler<E> extends Thread {

    private static Logger logger = LoggerFactory.getLogger(PubSubHandler.class);

    private volatile boolean run = true;

    protected MessageReceiver<E> receiver;
    protected MessageSender<E> sender;

    private Message<E> initMessage;

    private CopyOnWriteArrayList<SubscriberHandler<E>> subscribers;

    /**
     * Basic constructor with the publishers MessageReceiver and MessageSender.
     * 
     * @param receiver
     * @param sender
     */
    public PubSubHandler(MessageReceiver<E> receiver, MessageSender<E> sender) {
        this.receiver = receiver;
        this.sender = sender;
        initMessage = null;
        subscribers = new CopyOnWriteArrayList<>();
    }

    /**
     * Adds a subscriber as a listener for messages.
     * 
     * @param handler
     */
    public void addSubscriber(SubscriberHandler<E> handler) {
        logger.info("Adding subscriber");
        subscribers.add(handler);
        if (initMessage != null) {
            handler.sendMessage(initMessage);
        }
    }

    /**
     * Removes a subscriber as a listener.
     * 
     * @param handler
     */
    public void removeSubscriber(SubscriberHandler<E> handler) {
        subscribers.remove(handler);
    }

    /**
     * The publish-subscribe pattern is executed.
     */
    @Override
    public void run() {
        // wait for the topic name
        logger.info("Waiting for topic");
        String topicName = waitForTopicName();
        // if we do not get a topic name we assume the subscriber died
        if (topicName != null) {
            // check if the topic exists
            if (Topics.get(topicName) != null) {
                // we send an error to the subscriber
                logger.error("Topic '" + topicName + "' is already registered");
                sendErrorForExistingTopic();
            } else {
                // we add the current PubSubHandler to the Topics
                logger.info("Add hanlder to topics");
                Topics.add(topicName, this);

                // wait for messages and forward them to the subscribers
                while (run) {
                    // wait for a message
                    logger.debug("Waiting for message");
                    Message<E> msg = receiver.receive();
                    // if we receive the first init message we save it so we can
                    // send it to newly connected subscribers
                    if (msg.getType() == Type.INIT) {
                        if (initMessage == null) {
                            logger.debug("Init message received");
                            initMessage = msg;
                        }
                    } else if (msg.getType() == Type.CLOSE) {
                        // if we receive a close message, we will shutdown the
                        // handler
                        logger.debug("Close message received, shutting down hanlder");
                        run = false;
                    } else if (msg.getType() == Type.ERROR) {
                        // in case of an error we also shut down the handler
                        logger.debug("Error message received, shutting downh handler");
                        run = false;
                    }
                    // and send the message to all subscribers
                    logger.debug("Sending message to subscribers");
                    for (SubscriberHandler<E> s : subscribers) {
                        s.addMessage(msg);
                    }
                    logger.debug("Message sent to subscribers");
                }
            }
        }
        shutdown();
    }

    /**
     * Wait and return the topic name used by this publisher.
     * 
     * @return
     */
    public abstract String waitForTopicName();

    /**
     * Send an error to the publisher if the topic name is already registered.
     */
    public abstract void sendErrorForExistingTopic();

    /**
     * Shutdown all implementation specific resources.
     */
    public abstract void shutdown();
}
