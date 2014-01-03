package at.ac.tuwien.infosys.pubsub.middleware.arch.component;

import at.ac.tuwien.infosys.pubsub.middleware.arch.myx.MyxRuntime;

public abstract class SubscriberDispatcher<E> extends Dispatcher<E> {

	@Override
	public void createEndpoint() {
		MyxRuntime.getInstance().createSubscriberEndpoint(this);
	}

}
