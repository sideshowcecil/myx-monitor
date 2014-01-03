package at.ac.tuwien.infosys.pubsub.middleware.arch.component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IRegistry;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.ISubscriber;
import at.ac.tuwien.infosys.pubsub.middleware.arch.myx.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;

public abstract class SubscriberEndpoint<E> extends AbstractMyxSimpleBrick
		implements ISubscriber<E> {

	public static final IMyxName OUT_IDISPATCHER = MyxUtils
			.createName(IDispatcher.class.getCanonicalName());
	public static final IMyxName OUT_IREGISTRY = MyxUtils
			.createName(IRegistry.class.getCanonicalName());
	public static final IMyxName IN_ISUBSCRIBER = MyxUtils
			.createName(ISubscriber.class.getCanonicalName());

	protected IDispatcher<E> _dispatcher;
	protected IRegistry<E> _registry;

	private ExecutorService _executor;
	private Runnable _endpoint;
	
    private BlockingQueue<Message<E>> _queue = new LinkedBlockingQueue<>();

	@Override
	public Object getServiceObject(IMyxName arg0) {
		// if no interfaces are going in, always return null
		// in this case, we have an interface coming in
		if (arg0.equals(IN_ISUBSCRIBER)) {
			return this;
		}
		return null;
	}

	@Override
	public void init() {
		_executor = Executors.newSingleThreadExecutor();
		_endpoint = new Runnable() {
			public void run() {
				// wait for the topic name
				String topic = waitForTopicName();
				// if we do not get a topic name we assume the publisher died
				if (topic != null) {
					// check if the topic exists
					try {
						_registry.register(topic, SubscriberEndpoint.this);
					} catch (IllegalArgumentException ex) {
						sendErrorForNonExistingTopic();
						return;
					}
					// wait for messages and forward them to the subscribers
					while (true) {
						// wait for a message
						Message<E> msg = _queue.poll();
						// TODO: check for CLOSE or ERROR messages so we may shut down the endpoint.
						// TODO: maybe don't use a queue here but rather forward it directly to the real endpoint
					}
				}
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public void begin() {
		try {
			// connect interfaces
			_dispatcher = (IDispatcher<E>) getFirstRequiredServiceObject(OUT_IDISPATCHER);
			_registry = (IRegistry<E>) getFirstRequiredServiceObject(OUT_IREGISTRY);
		} catch (IllegalArgumentException ex) {
			System.err.println(ex.getMessage());
			return;
		}
		_executor.execute(_endpoint);
	}
	
	@Override
	public void send(Message<E> message) {
		_queue.offer(message);
	}

    /**
     * Wait and return the topic name used by this subscriber.
     * 
     * @return
     */
    public abstract String waitForTopicName();

    /**
     * Send an error to the subscriber if the topic name is not registered.
     */
    public abstract void sendErrorForNonExistingTopic();

}
