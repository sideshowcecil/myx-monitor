package at.ac.tuwien.dsg.pubsub.network.socket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.network.Endpoint;

/**
 * Base64 byte protocol specification and the implementation of the
 * {@link Endpoint}.
 * 
 * @author bernd.rathmanner
 * 
 */
public final class EventSocketByteMessageProtocol implements Endpoint<Event> {

    private SocketByteMessageProtocol protocol;

    /**
     * Default constructor.
     * 
     * @throws IOException
     */
    public EventSocketByteMessageProtocol(Socket socket) throws IOException {
        protocol = new SocketByteMessageProtocol(socket);
    }

    @Override
    public void send(Message<Event> msg) throws IOException {
        // serialize the object
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput oo = null;
        try {
            oo = new ObjectOutputStream(bos);
            oo.writeObject(msg.getData());
            oo.close();
        } finally {
            try {
                oo.close();
                bos.close();
            } catch (Exception e) {
            }
        }

        Message<byte[]> sentMessage = new Message<byte[]>(msg.getType(), msg.getTopic(), bos.toByteArray());

        // write to the stream
        protocol.send(sentMessage);
    }

    @Override
    public Message<Event> receive() throws IOException {
        // receive the message
        Message<byte[]> receivedMessage = protocol.receive();

        // unserialize the object
        Event event = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(receivedMessage.getData());
        ObjectInput oi = null;
        try {
            oi = new ObjectInputStream(bis);
            event = (Event) oi.readObject();
        } catch (ClassNotFoundException e) {
        } finally {
            try {
                oi.close();
                bis.close();
            } catch (Exception e) {
            }
        }

        return new Message<Event>(receivedMessage.getType(), receivedMessage.getTopic(), event);
    }

    @Override
    public void close() throws IOException {
        protocol.close();
    }

    /**
     * Get the used {@link Socket} object.
     * 
     * @return
     */
    public Socket getSocket() {
        return protocol.getSocket();
    }

}
