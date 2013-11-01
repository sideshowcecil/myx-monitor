package at.ac.tuwien.infosys.pubsub.middleware;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SubscriberListener<E> extends Thread {

    private static Logger logger = LoggerFactory
            .getLogger(SubscriberListener.class);

    private volatile boolean run = true;

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

    public void close() {
        run = false;
        try {
            join();
        } catch (InterruptedException e) {
        }
    }

    public abstract SubscriberHandler<E> waitForNextHandler();

    public abstract void shutdown();
}
