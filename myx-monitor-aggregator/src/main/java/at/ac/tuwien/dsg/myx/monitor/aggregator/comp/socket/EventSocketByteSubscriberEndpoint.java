package at.ac.tuwien.dsg.myx.monitor.aggregator.comp.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.dsg.myx.monitor.aggregator.events.TopicEvent;
import at.ac.tuwien.dsg.myx.monitor.aggregator.model.ModelRoot;
import at.ac.tuwien.dsg.myx.monitor.aggregator.myx.MyxInterfaceNames;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Topic;
import at.ac.tuwien.dsg.pubsub.middleware.comp.SubscriberEndpoint;
import at.ac.tuwien.dsg.pubsub.network.socket.EventSocketByteMessageProtocol;
import at.ac.tuwien.dsg.pubsub.network.socket.SocketByteMessageProtocol;
import edu.uci.isr.myx.fw.IMyxName;

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
                        Message<Event> message = endpoint.receive();

                        // TODO: handle modelroot requests
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
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
                for (String topic : e.getTopics()) {
                    topics.add(new Topic(topic, e.getType()));
                }
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
}
