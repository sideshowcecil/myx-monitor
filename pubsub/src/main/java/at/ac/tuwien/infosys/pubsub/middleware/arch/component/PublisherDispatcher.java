package at.ac.tuwien.infosys.pubsub.middleware.arch.component;

import at.ac.tuwien.infosys.pubsub.middleware.arch.myx.MyxRuntime;

public abstract class PublisherDispatcher<E> extends Dispatcher<E> {

    @Override
    public void createEndpoint() {
        MyxRuntime.getInstance().createPublisherEndpoint(getPublisherEndpointClass(), this);
    }

    /**
     * Get the class that is used to represent the {@link PublisherEndpoint}.
     * This class must have the same generic type as this class.
     * 
     * @return
     */
    protected abstract String getPublisherEndpointClass();
}
