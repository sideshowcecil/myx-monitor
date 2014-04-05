package at.ac.tuwien.dsg.pubsub.subscriber.comp.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Topic;
import at.ac.tuwien.dsg.pubsub.network.Endpoint;
import at.ac.tuwien.dsg.pubsub.network.socket.SocketByteMessageProtocol;
import at.ac.tuwien.dsg.pubsub.subscriber.comp.Subscriber;

public class SocketByteSubscriber extends Subscriber<byte[]> {

    private String host;
    private int port;

    @Override
    public void init() {
        host = MyxMonitoringUtils.getInitProperties(this).getProperty("hostname", "localhost");
        try {
            port = Integer.parseInt(MyxMonitoringUtils.getInitProperties(this).getProperty("port", "6667"));
        } catch (NumberFormatException e) {
            // use default value
            port = 6667;
        }
        super.init();
    }

    @Override
    protected Endpoint<byte[]> connect() {
        try {
            Socket s = new Socket(host, port);
            return new SocketByteMessageProtocol(s);
        } catch (IOException e) {
        }
        return null;
    }

    @Override
    protected Message<byte[]> getTopicsMessage(Topic.Type topicType, List<String> topics) {
        StringBuilder topicString = new StringBuilder();
        for (String topic : topics) {
            if (topicString.length() > 0) {
                topicString.append(SocketByteMessageProtocol.SEPARATOR);
            }
            topicString.append(topic);
        }

        return new Message<byte[]>(Message.Type.TOPIC, topicType.name(), topicString.toString().getBytes());
    }

    @Override
    protected String getExternalConnectionIdentifier() {
        if (endpoint instanceof SocketByteMessageProtocol) {
            Socket s = ((SocketByteMessageProtocol) endpoint).getSocket();
            // from,to
            return IdGenerator.generateConnectionIdentifier(s.getInetAddress().getHostAddress() + ":" + s.getPort()
                    + "," + s.getLocalAddress().getHostAddress() + ":" + s.getLocalPort());
        }
        return null;
    }

}
