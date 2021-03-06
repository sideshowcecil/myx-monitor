package at.ac.tuwien.dsg.pubsub.middleware.comp;

public abstract class PublisherDispatcher<E> extends Dispatcher<E> {

    @Override
    public void createEndpoint() {
        myxAdapter.createPublisherEndpoint(getPublisherEndpointClass(), this);
    }

    /**
     * Get the class that is used to represent the {@link PublisherEndpoint}.
     * This class must have the same generic type as this class.
     * 
     * @return
     */
    protected abstract String getPublisherEndpointClass();
}
