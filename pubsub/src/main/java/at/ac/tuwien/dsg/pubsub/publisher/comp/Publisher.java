package at.ac.tuwien.dsg.pubsub.publisher.comp;

import java.io.IOException;

import at.ac.tuwien.dsg.myx.monitor.AbstractVirtualExternalMyxSimpleBrick;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.middleware.myx.DynamicArchitectureModelProperties;
import at.ac.tuwien.dsg.pubsub.network.Endpoint;
import at.ac.tuwien.dsg.pubsub.publisher.interfaces.IPublisher;
import at.ac.tuwien.dsg.pubsub.publisher.myx.MyxInterfaceNames;
import edu.uci.isr.myx.fw.IMyxName;

public abstract class Publisher<E> extends AbstractVirtualExternalMyxSimpleBrick implements IPublisher<E> {

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
        endpoint = connect();
        if (endpoint != null) {
            connectionIdentifier = getExternalConnectionIdentifier();
            dispatchExternalLinkConnectedEvent(
                    DynamicArchitectureModelProperties.PUBLISHER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_NAME,
                    DynamicArchitectureModelProperties.PUBLISHER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE,
                    connectionIdentifier);
        }
    }

    @Override
    public void end() {
        endpoint.close();
        dispatchExternalLinkDisconnectedEvent(
                DynamicArchitectureModelProperties.PUBLISHER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_NAME,
                DynamicArchitectureModelProperties.PUBLISHER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE,
                connectionIdentifier);
    }

    @Override
    public void publish(Message<E> message) {
        if (endpoint != null) {
            try {
                endpoint.send(message);
            } catch (IOException e) {
                endpoint.close();
                endpoint = null;
                dispatchExternalLinkDisconnectedEvent(
                        DynamicArchitectureModelProperties.PUBLISHER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_NAME,
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
