package at.ac.tuwien.dsg.pubsub.middleware.comp;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.IDispatcher;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.IMyxRuntimeAdapter;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.ISubscriber;
import at.ac.tuwien.dsg.pubsub.middleware.myx.MyxNames;
import at.ac.tuwien.dsg.pubsub.middleware.network.Endpoint;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;

public abstract class PublisherEndpoint<E> extends edu.uci.isr.myx.fw.AbstractMyxSimpleBrick {

    private static Logger logger = LoggerFactory.getLogger(PublisherEndpoint.class);

    public static final IMyxName OUT_IDISPATCHER = MyxUtils.createName(IDispatcher.class.getName());
    public static final IMyxName OUT_ISUBSCRIBER = MyxUtils.createName(ISubscriber.class.getName());
    public static final IMyxName OUT_MYX_ADAPTER = MyxNames.IMYX_ADAPTER;

    protected IDispatcher<E> dispatcher;
    protected ISubscriber<E> subscriber;
    protected IMyxRuntimeAdapter myxAdapter;

    protected Endpoint<E> endpoint;

    private ExecutorService executor;
    private Runnable runnable;

    @Override
    public Object getServiceObject(@SuppressWarnings("unused") IMyxName interfaceName) {
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
                    logger.info("Waiting for messages");
                    try {
                        while (true) {
                            // wait for a message and send it to the subscriber
                            subscriber.send(endpoint.receive());
                        }
                    } catch (IOException e) {

                    }
                    logger.info("All messages received");
                    endpoint.close();
                }
                myxAdapter.shutdownPublisherEndpoint(PublisherEndpoint.this);
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public void begin() {
        dispatcher = (IDispatcher<E>) MyxMonitoringUtils.getFirstRequiredServiceObject(this, OUT_IDISPATCHER);
        if (dispatcher == null) {
            throw new RuntimeException("Interface " + OUT_IDISPATCHER + " returned null");
        }
        subscriber = (ISubscriber<E>) MyxMonitoringUtils.getFirstRequiredServiceObject(this, OUT_ISUBSCRIBER);
        if (subscriber == null) {
            throw new RuntimeException("Interface " + OUT_ISUBSCRIBER + " returned null");
        }
        myxAdapter = (IMyxRuntimeAdapter) MyxMonitoringUtils.getFirstRequiredServiceObject(this, OUT_MYX_ADAPTER);
        if (myxAdapter == null) {
            throw new RuntimeException("Interface " + OUT_MYX_ADAPTER + " returned null");
        }
        executor.execute(runnable);
    }

    @Override
    public void end() {
        executor.shutdownNow();
    }
}
