package at.ac.tuwien.infosys.pubsub.middleware;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.message.Message.Type;

public abstract class SubscriberHandler<E> extends Thread {

    private static Logger logger = LoggerFactory
            .getLogger(SubscriberHandler.class);

    private BlockingQueue<Message<E>> messageQueue = new LinkedBlockingQueue<>();

    private volatile boolean run = true;

    public void addMessage(Message<E> msg) {
        messageQueue.offer(msg);
    }

    @Override
    public void run() {
        // wait for the topic name
        logger.info("Waiting for topic");
        String topicName = waitForTopicName();
        // if we do not get a topic name we assume the subscriber died
        if (topicName != null) {
            // check if the topic exists
            if (Topics.get(topicName) == null) {
                // we send an error to the subscriber
                logger.error("Topic '" + topicName + "' is not registered");
                sendErrorForNonExistingTopic();
            } else {
                // we add the current SubscriberHandler to the PubSubHandler
                PubSubHandler<E> pubsub = Topics.<E> get(topicName);
                if (pubsub == null) {
                    // if the topic is not compatible we send an error to the
                    // subscriber
                    sendErrorForNonExistingTopic();
                } else {
                    // now we add ourself to the PubSubHandlers subscribers
                    logger.info("Add hanlder to publisher");
                    pubsub.addSubscriber(this);

                    // now we listen for incoming messages
                    while (run) {
                        Message<E> msg = null;
                        try {
                            // wait for a message
                            logger.debug("Waiting for message");
                            // msg = messageQueue.poll(500,
                            // TimeUnit.MILLISECONDS);
                            msg = messageQueue.take();
                        } catch (InterruptedException e) {
                        }
                        if (msg != null) {
                            logger.debug("Queue length: " + messageQueue.size());
                            if (msg.getType() == Type.CLOSE) {
                                // if we receive a close message, we will
                                // shutdown the handler
                                logger.debug("Close message received, shutting down hanlder");
                                run = false;
                            }
                            logger.debug("Sending message to subscriber");
                            sendMessage(msg);
                            logger.debug("Message sent");
                        }
                    }

                    // remove the subscriber from the PubSubHandler
                    pubsub.removeSubscriber(this);
                }
            }
        }
        // execute shutdown tasks
        shutdown();
    }

    public void close() {
        run = false;
        try {
            join();
        } catch (InterruptedException e) {
        }
    }

    public abstract String waitForTopicName();

    public abstract void sendErrorForNonExistingTopic();

    public abstract void sendMessage(Message<E> msg);

    public abstract void shutdown();
}
