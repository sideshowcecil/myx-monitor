package at.ac.tuwien.dsg.pubsub.middleware.interfaces;

import at.ac.tuwien.dsg.pubsub.network.Endpoint;

public interface IDispatcher<E> {
    /**
     * Get the next pending endpoint.
     * 
     * @return
     */
    public Endpoint<E> getNextEndpoint();
}
