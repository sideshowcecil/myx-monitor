package at.ac.tuwien.infosys.pubsub.middleware;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PublisherListener<E> extends Thread {

    private static Logger logger = LoggerFactory
            .getLogger(PublisherListener.class);

    private volatile boolean run = true;

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

    public void close() {
        run = false;
        try {
            join();
        } catch (InterruptedException e) {
        }
    }

    public abstract PubSubHandler<E> waitForNextHandler();

    public abstract void shutdown();
}
