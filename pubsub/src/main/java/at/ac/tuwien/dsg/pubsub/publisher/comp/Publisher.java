package at.ac.tuwien.dsg.pubsub.publisher.comp;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.dsg.myx.monitor.AbstractMyxExternalConnectionBrick;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.IPublisher;
import at.ac.tuwien.dsg.pubsub.middleware.myx.DynamicArchitectureModelProperties;
import at.ac.tuwien.dsg.pubsub.network.Endpoint;
import at.ac.tuwien.dsg.pubsub.publisher.myx.MyxInterfaceNames;
import edu.uci.isr.myx.fw.IMyxName;

public abstract class Publisher<E> extends AbstractMyxExternalConnectionBrick implements IPublisher<E> {

    private static Logger logger = LoggerFactory.getLogger(Publisher.class);

    public static final IMyxName IN_IPUBLISHER = MyxInterfaceNames.IPUBLISHER;

    private String connectionIdentifier;

    protected Endpoint<E> endpoint;

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        if (interfaceName.equals(IN_IPUBLISHER)) {
            return this;
        }
        return null;
    }

    @Override
    public void begin() {
        logger.info("Connecting");
        endpoint = connect();
        if (endpoint != null) {
            connectionIdentifier = getExternalConnectionIdentifier();
            dispatchExternalLinkConnectedEvent(
                    DynamicArchitectureModelProperties.PUBLISHER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE,
                    connectionIdentifier);
        }
    }

    @Override
    public void end() {
        logger.info("Disconnecting");
        try {
            endpoint.close();
        } catch (IOException e) {
        }
        dispatchExternalLinkDisconnectedEvent(
                DynamicArchitectureModelProperties.PUBLISHER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE,
                connectionIdentifier);
    }

    @Override
    public void publish(Message<E> message) {
        if (endpoint != null) {
            try {
                endpoint.send(message);
            } catch (IOException e) {
                try {
                    endpoint.close();
                } catch (IOException e1) {
                }
                endpoint = null;
                dispatchExternalLinkDisconnectedEvent(
                        DynamicArchitectureModelProperties.PUBLISHER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE,
                        connectionIdentifier);
            }
        }
    }

    /**
     * Connect to the middleware or similar and return the endpoint.
     */
    protected abstract Endpoint<E> connect();

    /**
     * Get the external connection id of the connected {@link Endpoint}.
     * 
     * @return
     */
    protected abstract String getExternalConnectionIdentifier();

}
