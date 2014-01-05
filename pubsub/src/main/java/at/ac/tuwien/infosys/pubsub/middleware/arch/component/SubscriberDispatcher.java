package at.ac.tuwien.infosys.pubsub.middleware.arch.component;

import at.ac.tuwien.infosys.pubsub.middleware.arch.myx.MyxRuntime;

public abstract class SubscriberDispatcher<E> extends Dispatcher<E> {

    @Override
    public void createEndpoint() {
        MyxRuntime.getInstance().createSubscriberEndpoint(getSubscriberEndpointClass(), this);
    }

    /**
     * Get the class that is used to represent the {@link SubscriberEndpoint}.
     * This class must have the same generic type as this class.
     * 
     * @return
     */
    protected abstract String getSubscriberEndpointClass();

}
