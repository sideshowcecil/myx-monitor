package at.ac.tuwien.dsg.pubsub.network.socket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.codec.binary.Base64;

import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Message.Type;
import at.ac.tuwien.dsg.pubsub.network.Endpoint;

/**
 * Base64 byte protocol specification and the implementation of the
 * {@link Endpoint}.
 * 
 * @author bernd.rathmanner
 * 
 */
public final class EventSocketByteMessageProtocol implements Endpoint<Event> {
    public static final char NULL = '\0';
    public static final char CR = '\r';
    public static final char LF = '\n';
    public static final char SEPARATOR = ';';

    private Socket socket;

    /**
     * Default constructor.
     * 
     * @throws IOException
     */
    public EventSocketByteMessageProtocol(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void send(Message<Event> msg) throws IOException {
        // serialize the object
        byte[] serialized = null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput oo = null;
        try {
            oo = new ObjectOutputStream(bos);
            oo.writeObject(msg.getData());
            oo.close();
            serialized = Base64.encodeBase64String(bos.toByteArray()).getBytes();
        } catch (IOException e) {
        } finally {
            try {
                oo.close();
                bos.close();
            } catch (Exception e) {
            }
        }
        
        // write to the stream        
        OutputStream out = socket.getOutputStream();
        out.write(msg.getType().toString().getBytes());
        out.write(SEPARATOR);
        out.write(msg.getTopic().getBytes());
        out.write(SEPARATOR);
        out.write(serialized);
        out.write(CR);
        out.write(LF);
        out.flush();
    }

    @Override
    public Message<Event> receive() throws IOException {
        Type type = null;
        String topic = null;
        StringBuilder sb = new StringBuilder();
        
        // read the message
        char last = NULL;
        InputStream in = socket.getInputStream();
        while (true) {
            int b = in.read();
            // check if a byte is available
            if (b == -1) {
                break;
            }

            char c = (char) b;
            if (c == SEPARATOR) {
                // we caught the end of the message
                if (type == null) {
                    // the type has to be filled first
                    try {
                        type = Type.valueOf(sb.toString());
                    } catch (IllegalArgumentException e) {
                        // the type cannot be resolved
                        type = Type.ERROR;
                    }
                } else if (topic == null) {
                    // after this we fill the topic
                    topic = sb.toString();
                } else {
                    // this should not have happened
                }
                sb = new StringBuilder();
            } else {
                // read all characters until the end of the message
                if (c == CR && last == NULL) {
                    last = c;
                } else if (last == CR && c == LF) {
                    sb.setLength(sb.length() - 1);
                    break;
                }
                sb.append(c);
            }
        }
        if (type == null || topic == null) {
            type = Type.ERROR;
            topic = "";
        }
        
        // unserialize the object
        Event event = null;
        if (sb.length() > 0) {
            ByteArrayInputStream bis = new ByteArrayInputStream(
                    Base64.decodeBase64(sb.toString()));
            ObjectInput oi = null;
            try {
                oi = new ObjectInputStream(bis);
                event = (Event) oi.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    oi.close();
                    bis.close();
                } catch (Exception e) {
                }
            }
        }
        
        return new Message<Event>(type, topic, event);
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
        }
    }

    /**
     * Get the used {@link Socket} object.
     * 
     * @return
     */
    public Socket getSocket() {
        return socket;
    }

}
