package at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces;

import at.ac.tuwien.infosys.pubsub.message.Message;

public interface ISubscriber<E> {
	public void send(Message<E> message);
}
