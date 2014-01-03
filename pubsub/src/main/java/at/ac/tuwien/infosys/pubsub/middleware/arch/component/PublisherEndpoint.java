package at.ac.tuwien.infosys.pubsub.middleware.arch.component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IRegistry;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.ISubscriber;
import at.ac.tuwien.infosys.pubsub.middleware.arch.myx.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;

public abstract class PublisherEndpoint<E> extends AbstractMyxSimpleBrick {

	public static final IMyxName OUT_IDISPATCHER = MyxUtils
			.createName(IDispatcher.class.getCanonicalName());
	public static final IMyxName OUT_IREGISTRY = MyxUtils
			.createName(IRegistry.class.getCanonicalName());
	public static final IMyxName OUT_ISUBSCRIBER = MyxUtils
			.createName(ISubscriber.class.getCanonicalName());

	protected IDispatcher<E> _dispatcher;
	protected IRegistry<E> _registry;
	protected ISubscriber<E> _subscriber;

	private ExecutorService _executor;
	private Runnable _endpoint;

	@Override
	public Object getServiceObject(IMyxName arg0) {
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
						_registry.register(topic, PublisherEndpoint.this);
					} catch (IllegalArgumentException ex) {
						sendErrorForExistingTopic();
						return;
					}
					// wait for messages and forward them to the subscribers
					while (true) {
						// wait for a message
						Message<E> msg = receive();
						// TODO: check for CLOSE or ERROR messages so we may shut down the endpoint.
						_subscriber.send(msg);
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
			// TODO: can we initialize the interface now?
			_subscriber = (ISubscriber<E>) getFirstRequiredServiceObject(OUT_ISUBSCRIBER);
		} catch (IllegalArgumentException ex) {
			System.err.println(ex.getMessage());
			return;
		}
		_executor.execute(_endpoint);
	}

	/**
	 * Get the next message.
	 * 
	 * @return
	 */
	public abstract Message<E> receive();

	/**
	 * Wait and return the topic name used by this publisher.
	 * 
	 * @return
	 */
	public abstract String waitForTopicName();

	/**
	 * Send an error to the publisher if the topic name is already registered.
	 */
	public abstract void sendErrorForExistingTopic();

}
