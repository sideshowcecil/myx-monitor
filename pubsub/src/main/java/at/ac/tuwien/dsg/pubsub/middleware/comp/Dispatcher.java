package at.ac.tuwien.dsg.pubsub.middleware.comp;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.dsg.myx.util.MyxUtils;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.IDispatcher;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.IMyxRuntimeAdapter;
import at.ac.tuwien.dsg.pubsub.middleware.myx.MyxInterfaceNames;
import at.ac.tuwien.dsg.pubsub.network.Endpoint;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;

public abstract class Dispatcher<E> extends AbstractMyxSimpleBrick implements IDispatcher<E> {

    private static Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    public static final IMyxName IN_IDISPATCHER = MyxInterfaceNames.IDISPATCHER;
    public static final IMyxName OUT_MYX_ADAPTER = MyxInterfaceNames.IMYX_ADAPTER;

    private ExecutorService executor;
    private Runnable runnable;

    protected IMyxRuntimeAdapter myxAdapter;

    private Queue<Endpoint<E>> queue = new ConcurrentLinkedQueue<>();

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        if (interfaceName.equals(IN_IDISPATCHER)) {
            return this;
        }
        return null;
    }

    @Override
    public void init() {
        executor = Executors.newSingleThreadExecutor();
        runnable = new Runnable() {
            public void run() {
                while (true) {
                    logger.info("Waiting for next endpoint");
                    Endpoint<E> endpoint = waitForNextEndpoint();
                    if (endpoint != null) {
                        logger.info("Endpoint connected");
                        // enqueue the endpoint
                        queue.add(endpoint);
                        // create the endpoint
                        createEndpoint();
                    }
                }
            }
        };
    }

    @Override
    public void begin() {
        myxAdapter = MyxUtils.<IMyxRuntimeAdapter> getFirstRequiredServiceObject(this, OUT_MYX_ADAPTER);
        executor.execute(runnable);
    }

    @Override
    public void end() {
        executor.shutdownNow();
    }

    @Override
    public Endpoint<E> getNextEndpoint() {
        return queue.poll();
    }

    /**
     * Wait and return the next Endpoint.
     * 
     * @return
     */
    public abstract Endpoint<E> waitForNextEndpoint();

    /**
     * Create the real endpoint and execute it.
     */
    public abstract void createEndpoint();

}
