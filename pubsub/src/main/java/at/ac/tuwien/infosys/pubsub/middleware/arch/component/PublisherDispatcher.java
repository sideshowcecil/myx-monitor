package at.ac.tuwien.infosys.pubsub.middleware.arch.component;

import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IDispatcher;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;

public abstract class PublisherDispatcher<E> extends AbstractMyxSimpleBrick
		implements IDispatcher<E> {

	public static final IMyxName msg_IDispatcher = MyxUtils
			.createName("at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IDispatcher");

	@Override
	public Object getServiceObject(IMyxName arg0) {
		///////////////////////////////////////////
		// TODO: if the given IMyxName equals an ingoing interface we have to return ourself!!!!
		///////////////////////////////////////////
		if (arg0.equals(msg_IDispatcher)) {
			return this;
		}
		return null;
	}

}
