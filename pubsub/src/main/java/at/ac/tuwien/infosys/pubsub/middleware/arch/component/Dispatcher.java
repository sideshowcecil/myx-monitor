package at.ac.tuwien.infosys.pubsub.middleware.arch.component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.myx.AbstractMyxSimpleBrick;
import at.ac.tuwien.infosys.pubsub.middleware.arch.network.Endpoint;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;

public abstract class Dispatcher<E> extends AbstractMyxSimpleBrick implements IDispatcher<E> {

    public static final IMyxName IN_IDISPATCHER = MyxUtils.createName(IDispatcher.class.getCanonicalName());

    private ExecutorService _executor;
    private Runnable _dispatcher;

    private Queue<Endpoint<E>> _queue = new ConcurrentLinkedQueue<>();

    @Override
    public Object getServiceObject(IMyxName arg0) {
        // if no interfaces are going in, always return null
        // in this case, we have an interface coming in
        if (arg0.equals(IN_IDISPATCHER)) {
            return this;
        }
        return null;
    }

    @Override
    public void init() {
        _executor = Executors.newSingleThreadExecutor();
        _dispatcher = new Runnable() {
            public void run() {
                while (true) {
                    Endpoint<E> endpoint = waitForNewEndpoint();
                    if (endpoint != null) {
                        // enqueue the endpoint
                        _queue.add(endpoint);
                        // create the endpoint
                        createEndpoint();
                    }
                }
            }
        };
    }

    @Override
    public void begin() {
        _executor.execute(_dispatcher);
    }

    @Override
    public void end() {
        _executor.shutdownNow();
    }

    @Override
    public Endpoint<E> getNextEndpoint() {
        return _queue.poll();
    }

    /**
     * Wait and return the next Endpoint.
     * 
     * @return
     */
    public abstract Endpoint<E> waitForNewEndpoint();

    /**
     * Create the real endpoint and execute it.
     */
    public abstract void createEndpoint();

}
