package at.ac.tuwien.infosys.pubsub.middleware.arch.component;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.ISubscriber;
import at.ac.tuwien.infosys.pubsub.middleware.arch.myx.AbstractMyxSimpleBrick;
import at.ac.tuwien.infosys.pubsub.middleware.arch.network.Endpoint;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;

public abstract class PublisherEndpoint<E> extends AbstractMyxSimpleBrick {

    private static Logger logger = LoggerFactory.getLogger(PublisherEndpoint.class);

    public static final IMyxName OUT_IDISPATCHER = MyxUtils.createName(IDispatcher.class.getName());
    public static final IMyxName OUT_ISUBSCRIBER = MyxUtils.createName(ISubscriber.class.getName());

    protected IDispatcher<E> dispatcher;
    protected ISubscriber<E> subscriber;

    protected Endpoint<E> endpoint;

    private ExecutorService executor;
    private Runnable runnable;

    @Override
    public Object getServiceObject(@SuppressWarnings("unused") IMyxName arg0) {
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
                    logger.debug("Waiting for messages");
                    try {
                        while (true) {
                            // wait for a message and send it to the subscriber
                            subscriber.send(endpoint.receive());
                        }
                    } catch (IOException e) {

                    }
                    logger.debug("All messages received");
                    endpoint.close();
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public void begin() {
        try {
            // connect interfaces
            dispatcher = (IDispatcher<E>) getFirstRequiredServiceObject(OUT_IDISPATCHER);
            subscriber = (ISubscriber<E>) getFirstRequiredServiceObject(OUT_ISUBSCRIBER);
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            return;
        }
        executor.execute(runnable);
    }

    @Override
    public void end() {
        executor.shutdownNow();
    }
}
