package at.ac.tuwien.infosys.pubsub.middleware.arch.myx;

import edu.uci.isr.myx.conn.SynchronousProxyConnector;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.IMyxRuntime;
import edu.uci.isr.myx.fw.MyxBrickCreationException;
import edu.uci.isr.myx.fw.MyxBrickLoadException;
import edu.uci.isr.myx.fw.MyxJavaClassBrickDescription;
import edu.uci.isr.myx.fw.MyxJavaClassInterfaceDescription;
import edu.uci.isr.myx.fw.MyxUtils;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.Dispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.PublisherEndpoint;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.SubscriberDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.SubscriberEndpoint;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IRegistry;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.ISubscriber;

public class MyxRuntime {

	// interface names
	public static final String IDISPATCHER = IDispatcher.class
			.getCanonicalName();
	public static final String IREGISTRY = IRegistry.class.getCanonicalName();
	public static final String ISUBSCRIBER = ISubscriber.class
			.getCanonicalName();

	// component names
	public static final String PUBLISHER_DISPATCHER = Dispatcher.class
			.getCanonicalName();
	public static final String PUBLISHER_ENDPOINT = PublisherEndpoint.class
			.getCanonicalName();
	public static final String SUBSCRIBER_DISPATCHER = SubscriberDispatcher.class
			.getCanonicalName();
	public static final String SUBSCRIBER_ENDPOINT = SubscriberEndpoint.class
			.getCanonicalName();

	// connector names
	public static final String MESSAGE_DISTRIBUTOR = MessageDistributor.class
			.getCanonicalName();
	public static final String SYNCHRONOUS_PROXY = SynchronousProxyConnector.class
			.getCanonicalName();

	// interface descriptions
	private MyxJavaClassInterfaceDescription _iDispatcher;
	private MyxJavaClassInterfaceDescription _iRegistry;
	private MyxJavaClassInterfaceDescription _iSubscriber;

	// component descriptions
	private MyxJavaClassBrickDescription _publisherDispatcher;
	private MyxJavaClassBrickDescription _publisherEndpoint;
	private MyxJavaClassBrickDescription _subscriberDispatcher;
	private MyxJavaClassBrickDescription _subscriberEndpoint;

	// connector descriptions
	private MyxJavaClassBrickDescription _messageDistributor;
	private MyxJavaClassBrickDescription _synchronousProxy;

	public static void main(String args[]) { // TODO: remove main from here
		try {
			getInstance().boostrapArchitecture();
		} catch (MyxBrickLoadException | MyxBrickCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static MyxRuntime _instance = new MyxRuntime();

	public static MyxRuntime getInstance() {
		return _instance;
	}

	private IMyxRuntime _myx;

	private MyxRuntime() {
		_myx = MyxUtils.getDefaultImplementation().createRuntime();
	}

	public void boostrapArchitecture() throws MyxBrickLoadException,
			MyxBrickCreationException {
		// interfaces
		_iDispatcher = new MyxJavaClassInterfaceDescription(
				new String[] { IDISPATCHER });
		_iRegistry = new MyxJavaClassInterfaceDescription(
				new String[] { IREGISTRY });
		_iSubscriber = new MyxJavaClassInterfaceDescription(
				new String[] { ISUBSCRIBER });

		// components
		_publisherDispatcher = new MyxJavaClassBrickDescription(null,
				PUBLISHER_DISPATCHER);
		_publisherEndpoint = new MyxJavaClassBrickDescription(null,
				PUBLISHER_ENDPOINT);
		_subscriberDispatcher = new MyxJavaClassBrickDescription(null,
				SUBSCRIBER_DISPATCHER);
		_subscriberEndpoint = new MyxJavaClassBrickDescription(null,
				SUBSCRIBER_ENDPOINT);

		// connectors
		_messageDistributor = new MyxJavaClassBrickDescription(null,
				MESSAGE_DISTRIBUTOR);
		_synchronousProxy = new MyxJavaClassBrickDescription(null,
				SYNCHRONOUS_PROXY);

		// startup the application
		_myx.init(null, null);
		_myx.begin(null, null);
	}

	public void createPublisherEndpoint(Dispatcher<?> dispatcher) {
		IMyxName dispatcherName = MyxUtils.getName(dispatcher);
	}

	public void createSubscriberEndpoint(Dispatcher<?> dispatcher) {
		IMyxName dispatcherName = MyxUtils.getName(dispatcher);
	}

	public MessageDistributor createMessageDistributor(
			PublisherEndpoint<?> endpoint) {

		return null;
	}

	public void removeMessageDistributor(MessageDistributor distributor) {

	}

	public void wireMessageDistributor(SubscriberEndpoint<?> endpoint,
			MessageDistributor distributor) {

	}

	public void unwireMessageDistributor(SubscriberEndpoint<?> endpoint,
			MessageDistributor distributor) {

	}
}
