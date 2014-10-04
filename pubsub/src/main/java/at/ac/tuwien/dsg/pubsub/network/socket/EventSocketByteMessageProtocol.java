package at.ac.tuwien.dsg.pubsub.network.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
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
    private BufferedReader in;
    private BufferedWriter out;

    /**
     * Default constructor.
     * 
     * @throws IOException
     */
    public EventSocketByteMessageProtocol(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
    }

    @Override
    public void send(Message<Event> msg) throws IOException {
        // serialize the object
        String serialized = null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput oo = null;
        try {
            oo = new ObjectOutputStream(bos);
            oo.writeObject(msg.getData());
            oo.close();
            serialized = Base64.encodeBase64String(bos.toByteArray());
        } catch (IOException e) {
        } finally {
            try {
                oo.close();
                bos.close();
            } catch (Exception e) {
            }
        }

        // write to the stream
        out.write(msg.getType().toString());
        out.write(SEPARATOR);
        out.write(msg.getTopic());
        out.write(SEPARATOR);
        out.write(serialized);
        out.write(CR);
        out.write(LF);
        out.flush();
    }

    @Override
    public Message<Event> receive() throws IOException {
        String line = in.readLine();
        if (line == null) {
            // the end of stream was reached, thus we return a close message
            return new Message<Event>(Type.CLOSE, "", null);
        }
        String[] parts = line.split(SEPARATOR + "");
        if (parts.length != 3) {
            return new Message<Event>(Type.ERROR, "", null);
        }

        Type type = null;
        try {
            type = Type.valueOf(parts[0]);
        } catch (IllegalArgumentException e) {
            // the type cannot be resolved
            type = Type.ERROR;
        }

        // unserialize the object
        Event event = null;
        if (parts[2].length() > 0) {
            ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decodeBase64(parts[2]));
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

        return new Message<Event>(type, parts[1], event);
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
