package at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces;

import at.ac.tuwien.infosys.pubsub.middleware.arch.component.PublisherEndpoint;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.SubscriberEndpoint;

public interface IRegistry<E> {
	
	public void register(String topic, PublisherEndpoint<E> publisher);
	
	public void unregister(String topic, PublisherEndpoint<E> publisher); 

	public void register(String topic, SubscriberEndpoint<E> subscriber);
	
	public void unregister(String topic, SubscriberEndpoint<E> subscriber);
}
