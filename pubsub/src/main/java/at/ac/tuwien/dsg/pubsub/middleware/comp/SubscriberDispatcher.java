package at.ac.tuwien.dsg.pubsub.middleware.comp;

public abstract class SubscriberDispatcher<E> extends Dispatcher<E> {

    @Override
    public void createEndpoint() {
        myxAdapter.createSubscriberEndpoint(getSubscriberEndpointClass(), this);
    }

    /**
     * Get the class that is used to represent the {@link SubscriberEndpoint}.
     * This class must have the same generic type as this class.
     * 
     * @return
     */
    protected abstract String getSubscriberEndpointClass();

}
