package at.ac.tuwien.infosys.pubsub.middleware.arch.component;

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

	@Override
	public Object getServiceObject(IMyxName arg0) {
		// if no interfaces are going in, always return null
		// in this case, we have an interface coming in
		if (arg0.equals(IN_ISUBSCRIBER)) {
			return this;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			// connect interfaces
			_dispatcher = (IDispatcher<E>) getFirstRequiredServiceObject(OUT_IDISPATCHER);
			_registry = (IRegistry<E>) getFirstRequiredServiceObject(OUT_IREGISTRY);
		} catch (IllegalArgumentException ex) {
			System.err.println(ex.getMessage());
			return;
		}
	}

}
