package at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces;

import at.ac.tuwien.infosys.pubsub.middleware.arch.network.Endpoint;

public interface IDispatcher<E> {
	/**
	 * Get the next pending endpoint.
	 * 
	 * @return
	 */
	public Endpoint<E> getNextEndpoint();
}
