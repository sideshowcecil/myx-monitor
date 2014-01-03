package at.ac.tuwien.infosys.pubsub.middleware.arch.component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
				// if we do not get a topic name we assume the subscriber died
				if (topic != null) {
					// check if the topic exists and register the endpoint
					try {
						_registry.register(topic, SubscriberEndpoint.this);
					} catch (IllegalArgumentException ex) {
						sendErrorForNonExistingTopic();
						return;
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
