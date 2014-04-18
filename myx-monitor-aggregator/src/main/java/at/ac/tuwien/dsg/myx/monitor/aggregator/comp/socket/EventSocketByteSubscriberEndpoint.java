package at.ac.tuwien.dsg.myx.monitor.aggregator.comp.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.dsg.myx.monitor.aggregator.events.ModelElementRequestEvent;
import at.ac.tuwien.dsg.myx.monitor.aggregator.events.ModelElementResponseEvent;
import at.ac.tuwien.dsg.myx.monitor.aggregator.events.ModelNoSuchElementResponseEvent;
import at.ac.tuwien.dsg.myx.monitor.aggregator.events.ModelRequestEvent;
import at.ac.tuwien.dsg.myx.monitor.aggregator.events.ModelResponseEvent;
import at.ac.tuwien.dsg.myx.monitor.aggregator.events.TopicEvent;
import at.ac.tuwien.dsg.myx.monitor.aggregator.model.ModelRoot;
import at.ac.tuwien.dsg.myx.monitor.aggregator.myx.MyxInterfaceNames;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLElementType;
import at.ac.tuwien.dsg.myx.util.DBLUtils;
import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Topic;
import at.ac.tuwien.dsg.pubsub.middleware.comp.SubscriberEndpoint;
import at.ac.tuwien.dsg.pubsub.network.socket.EventSocketByteMessageProtocol;
import at.ac.tuwien.dsg.pubsub.network.socket.SocketByteMessageProtocol;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.xarch.types.IArchStructure;
import edu.uci.isr.xarch.types.IComponent;
import edu.uci.isr.xarch.types.IConnector;
import edu.uci.isr.xarch.types.IInterface;

/**
 * Implementation of {@link SubscriberEndpoint} based on the
 * {@link SocketByteMessageProtocol}.
 * 
 * @author bernd.rathmanner
 * 
 */
public class EventSocketByteSubscriberEndpoint extends SubscriberEndpoint<Event> {

    public static final IMyxName OUT_MODEL_ROOT = MyxInterfaceNames.MODEL_ROOT;

    private static Logger logger = LoggerFactory.getLogger(EventSocketByteSubscriberEndpoint.class);

    protected ModelRoot modelRoot;

    private ExecutorService executor;
    private Runnable runnable;

    @Override
    public void init() {
        super.init();
        executor = Executors.newSingleThreadExecutor();
        runnable = new Runnable() {
            public void run() {
                // wait until we are fully connected
                while (topics == null) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                logger.info("Listening for incoming model requests");
                try {
                    while (true) {
                        Message<Event> receivedMessage = endpoint.receive();

                        if (receivedMessage.getData() instanceof ModelRequestEvent) {
                            logger.info("Consuming event of type " + receivedMessage.getData().getClass());
                            
                            ModelRequestEvent request = (ModelRequestEvent) receivedMessage.getData();
                            ModelResponseEvent response = null;

                            if (request instanceof ModelElementRequestEvent) {
                                response = getElementRespone((ModelElementRequestEvent) request);
                            }

                            if (response != null) {
                                response.setEventSourceId(this.getClass().getName());
                                Message<Event> message = new Message<Event>(receivedMessage.getTopic(), response);
                                consume(message);
                                logger.info("Sent modelroot data");
                            }
                        }
                    }
                } catch (IOException e) {
                }
            }

            /**
             * 
             * @param request
             * @return
             */
            private ModelResponseEvent getElementRespone(ModelElementRequestEvent request) {
                for (IArchStructure structure : modelRoot.getArchStructures()) {
                    for (IComponent component : DBLUtils.getComponents(structure)) {
                        if (component.getId().equals(request.getRuntimeId())) {
                            ModelElementResponseEvent response = new ModelElementResponseEvent(request.getRuntimeId(), XADLElementType.COMPONENT);
                            response.setDescription(DBLUtils.getDescription(component));
                            for (IInterface intf : DBLUtils.getInterfaces(component)) {
                                response.getInterfaces().put(DBLUtils.getId(intf), DBLUtils.getId(intf.getType()));
                            }
                            return response;
                        }
                    }
                    for (IConnector connector : DBLUtils.getConnectors(structure)) {
                        if (connector.getId().equals(request.getRuntimeId())) {
                            ModelElementResponseEvent response = new ModelElementResponseEvent(request.getRuntimeId(), XADLElementType.CONNECTOR);
                            response.setDescription(DBLUtils.getDescription(connector));
                            for (IInterface intf : DBLUtils.getInterfaces(connector)) {
                                response.getInterfaces().put(DBLUtils.getId(intf), DBLUtils.getId(intf.getType()));
                            }
                            return response;
                        }
                    }
                }
                return new ModelNoSuchElementResponseEvent(request.getRuntimeId());
            }
        };
    }

    @Override
    public void begin() {
        modelRoot = (ModelRoot) MyxMonitoringUtils.getFirstRequiredServiceObject(this, OUT_MODEL_ROOT);
        if (modelRoot == null) {
            throw new RuntimeException("Interface " + OUT_MODEL_ROOT + " returned null");
        }
        super.begin();
        executor.execute(runnable);
    }

    @Override
    public List<Topic> getTopics() {
        try {
            Message<Event> msg = endpoint.receive();
            if (msg.getType() == Message.Type.TOPIC && msg.getData() instanceof TopicEvent) {
                TopicEvent e = (TopicEvent) msg.getData();
                List<Topic> topics = new ArrayList<>();
                for (String topic : e.getTopics()) {
                    topics.add(new Topic(topic, e.getType()));
                }
                return topics;
            }
        } catch (IOException | IllegalArgumentException e) {
        }
        return null;
    }

    @Override
    protected String getExternalConnectionIdentifier() {
        if (endpoint instanceof EventSocketByteMessageProtocol) {
            Socket s = ((EventSocketByteMessageProtocol) endpoint).getSocket();
            // from,to
            return IdGenerator.generateConnectionIdentifier(s.getLocalAddress().getHostAddress() + ":"
                    + s.getLocalPort() + "," + s.getInetAddress().getHostAddress() + ":" + s.getPort());
        }
        return null;
    }

    @Override
    public void consume(Message<Event> message) {
        synchronized (endpoint) {
            super.consume(message);
        }
    }
}
