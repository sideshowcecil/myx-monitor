package at.ac.tuwien.infosys.pubsub.middleware.arch.myx;

import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;

public abstract class AbstractMyxSimpleBrick extends edu.uci.isr.myx.fw.AbstractMyxSimpleBrick {

	/**
	 * Returns the first required service objects or throws an exception if null
	 * is returned.
	 * 
	 * @param name
	 * @return
	 * @throws IllegalArgumentException
	 */
	protected Object getFirstRequiredServiceObject(IMyxName name) throws IllegalArgumentException {
		Object o = MyxUtils.getFirstRequiredServiceObject(this, name);
		if (o == null) {
			throw new IllegalArgumentException("Error: Interface "
					+ name.getName() + "returned null");
		}
		return o;
	}

}
