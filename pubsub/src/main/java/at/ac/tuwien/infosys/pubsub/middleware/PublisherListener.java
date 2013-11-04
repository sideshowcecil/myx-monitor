package at.ac.tuwien.infosys.pubsub.middleware;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This abstract class contains the basic logic for listening for new
 * publishers.
 * 
 * @author bernd.rathmanner
 * 
 * @param <E>
 *            resembles the message data.
 */
public abstract class PublisherListener<E> extends Thread {

    private static Logger logger = LoggerFactory
            .getLogger(PublisherListener.class);

    private volatile boolean run = true;

    /**
     * Start the listening for new publishers.
     */
    @Override
    public void run() {
        ExecutorService threadPool = Executors.newCachedThreadPool();

        while (run) {
            logger.info("Waiting for next publisher");
            PubSubHandler<E> handler = waitForNextHandler();
            if (handler != null) {
                logger.info("Executing publisher handler");
                threadPool.execute(handler);
            }
        }
        threadPool.shutdown(); // TODO maybe use shutdownNow()
        shutdown();
    }

    /**
     * Stop listening for new publishers.
     */
    public void close() {
        run = false;
        try {
            join();
        } catch (InterruptedException e) {
        }
    }

    /**
     * Wait and return the next PubSubHandler if a new publisher connects.
     * 
     * @return
     */
    public abstract PubSubHandler<E> waitForNextHandler();

    /**
     * Shutdown all implementation specific resources.
     */
    public abstract void shutdown();
}
