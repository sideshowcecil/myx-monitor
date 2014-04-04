package at.ac.tuwien.dsg.pubsub.middleware.network.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.codec.binary.Base64;

import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Message.Type;
import at.ac.tuwien.dsg.pubsub.middleware.network.Endpoint;

/**
 * Base64 byte protocol specification and the implementation of the
 * {@link Endpoint}.
 * 
 * @author bernd.rathmanner
 * 
 */
public final class SocketByteMessageProtocol implements Endpoint<byte[]> {
    public static final char NULL = '\0';
    public static final char CR = '\r';
    public static final char LF = '\n';
    public static final char SEPARATOR = ';';

    public static final char TOPIC = '#';
    public static final char INIT = '*';
    public static final char DATA = '+';
    public static final char CLOSE = '$';
    public static final char ERROR = '-';

    private Socket socket;

    /**
     * Default constructor.
     * 
     * @throws IOException
     */
    public SocketByteMessageProtocol(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void send(Message<byte[]> msg) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write(msg.getType().toString().getBytes());
        out.write(SEPARATOR);
        out.write(msg.getTopic().getBytes());
        out.write(SEPARATOR);
        out.write(Base64.encodeBase64(msg.getData()));
        out.write(CR);
        out.write(LF);
    }

    @Override
    public Message<byte[]> receive() throws IOException {
        Type type = null;
        String topic = null;

        StringBuilder sb = new StringBuilder();
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
        return new Message<byte[]>(type, topic, Base64.decodeBase64(sb.toString().getBytes()));
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
