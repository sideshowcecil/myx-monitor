package at.ac.tuwien.infosys.pubsub.middleware.arch.component.socket;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.message.Message.Type;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.PublisherEndpoint;

/**
 * Implementation of {@link PublisherEndpoint} based on the
 * SocketByteMessageProtocol.
 * 
 * @author bernd.rathmanner
 * 
 */
public class SocketBytePublisherEndpoint extends PublisherEndpoint<byte[]> {

	@Override
	public String waitForTopicName() {
		Message<byte[]> msg = _endpoint.receive();
		if (msg.getType() == Type.TOPIC) {
			return new String(msg.getData());
		}
		return null;
	}

	@Override
	public void sendErrorForExistingTopic() {
		Message<byte[]> msg = new Message<byte[]>(
				"The given topic already exists!".getBytes(), Type.ERROR);
		_endpoint.send(msg);
	}

}
