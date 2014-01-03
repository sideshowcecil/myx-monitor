package at.ac.tuwien.infosys.pubsub.middleware.arch.component;

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

	@Override
	public Object getServiceObject(IMyxName arg0) {
		return null;
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
	}

}
