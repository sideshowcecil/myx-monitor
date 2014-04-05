package at.ac.tuwien.dsg.pubsub.publisher.comp;

import java.io.IOException;

import at.ac.tuwien.dsg.myx.monitor.AbstractVirtualExternalMyxSimpleBrick;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.network.Endpoint;
import at.ac.tuwien.dsg.pubsub.publisher.interfaces.IPublisher;
import edu.uci.isr.myx.fw.IMyxName;

public abstract class Publisher<E> extends AbstractVirtualExternalMyxSimpleBrick implements IPublisher<E> {

    public static final IMyxName IN_PUBLISHER = MyxMonitoringUtils.createName("in");

    protected Endpoint<E> endpoint;

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        if (interfaceName.equals(IN_PUBLISHER)) {
            return this;
        }
        return null;
    }

    @Override
    public void publish(Message<E> message) {
        // lazy connect
        if (endpoint == null) {
            connect();
        }
        if (endpoint != null) {
            // we only send the message to the subscriber if the subscribed
            // topic matches
            try {
                endpoint.send(message);
            } catch (IOException e) {
                endpoint.close();
                endpoint = null;
            }
        }
    }

    /**
     * Connect to the middleware or similar.
     */
    protected abstract void connect();

    /**
     * Get the external connection id of the connected {@link Endpoint}.
     * 
     * @return
     */
    protected abstract String getExternalConnectionIdentifier();

}
