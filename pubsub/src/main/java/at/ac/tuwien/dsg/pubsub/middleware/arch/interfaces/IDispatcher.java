package at.ac.tuwien.dsg.pubsub.middleware.arch.interfaces;

import at.ac.tuwien.dsg.pubsub.middleware.arch.network.Endpoint;

public interface IDispatcher<E> {
    /**
     * Get the next pending endpoint.
     * 
     * @return
     */
    public Endpoint<E> getNextEndpoint();
}
