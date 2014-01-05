package at.ac.tuwien.infosys.pubsub.middleware;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * This abstract class contains the basic logic for listening for new
 * subscribers.
 * 
 * @author bernd.rathmanner
 * 
 * @param <E>
 *            resembles the message data.
 */
public abstract class SubscriberListener<E> extends Thread {

    private static Logger logger = LoggerFactory.getLogger(SubscriberListener.class);

    private volatile boolean run = true;

    /**
     * Start the listening for new subscribers.
     */
    @Override
    public void run() {
        ExecutorService threadPool = Executors.newCachedThreadPool();

        while (run) {
            logger.info("Waiting for next subscriber");
            SubscriberHandler<E> handler = waitForNextHandler();
            if (handler != null) {
                logger.info("Executing subscriber handler");
                threadPool.execute(handler);
            }
        }
        threadPool.shutdown(); // TODO maybe use shutdownNow()
        shutdown();
    }

    /**
     * Stop listening for new subscribers.
     */
    public void close() {
        run = false;
        try {
            join();
        } catch (InterruptedException e) {
        }
    }

    /**
     * Wait and return the next SubscriberHandler if a new subscriber connects.
     * 
     * @return
     */
    public abstract SubscriberHandler<E> waitForNextHandler();

    /**
     * Shutdown all implementation specific resources.
     */
    public abstract void shutdown();
}
