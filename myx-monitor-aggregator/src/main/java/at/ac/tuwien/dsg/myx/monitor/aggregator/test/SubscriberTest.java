package at.ac.tuwien.dsg.myx.monitor.aggregator.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import at.ac.tuwien.dsg.myx.monitor.aggregator.events.ModelElementRequestEvent;
import at.ac.tuwien.dsg.myx.monitor.aggregator.events.TopicEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEvent;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Message.Type;
import at.ac.tuwien.dsg.pubsub.network.socket.EventSocketByteMessageProtocol;

/**
 * Test class to show how the subscriber can be created (real Subscriber
 * implementation is still a TODO)
 * 
 * @author bernd.rathmanner
 * 
 */
public class SubscriberTest {

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException,
            InterruptedException {
        Socket socketReceiver = new Socket();
        socketReceiver.connect(new InetSocketAddress(9001));
        EventSocketByteMessageProtocol protocol = new EventSocketByteMessageProtocol(socketReceiver);

        TopicEvent te = new TopicEvent();
        te.getTopics().add("*");
        protocol.send(new Message<Event>(Message.Type.TOPIC, "init", te));

        String id = null;

        while (true) {
            Message<Event> m = protocol.receive();

            if (m.getType() == Type.CLOSE || m.getType() == Type.ERROR) {
                break;
            }

            if (id == null && m.getData() instanceof XADLEvent) {
                id = ((XADLEvent) m.getData()).getXadlRuntimeId();
                protocol.send(new Message<Event>("request", new ModelElementRequestEvent(id)));
            }

            System.out.println(m.getData());
        }

        socketReceiver.close();
    }

}
