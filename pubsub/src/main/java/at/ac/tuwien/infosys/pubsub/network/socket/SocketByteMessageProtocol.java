package at.ac.tuwien.infosys.pubsub.network.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.codec.binary.Base64;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.message.Message.Type;
import at.ac.tuwien.infosys.pubsub.network.MessageReceiver;
import at.ac.tuwien.infosys.pubsub.network.MessageSender;

/**
 * Base64 byte protocol specification and the implementation of the
 * MessageReceiver and MessageSender.
 * 
 * @author bernd.rathmanner
 * 
 */
public final class SocketByteMessageProtocol implements MessageReceiver<byte[]>, MessageSender<byte[]> {
    public static final char NULL = '\0';
    public static final char CR = '\r';
    public static final char LF = '\n';

    public static final char TOPIC = '#';
    public static final char INIT = '*';
    public static final char ACK = '/';
    public static final char DATA = '+';
    public static final char CLOSE = '$';
    public static final char ERROR = '-';

    private InputStream in;
    private OutputStream out;

    /**
     * Default constructor.
     * 
     * @throws IOException
     */
    public SocketByteMessageProtocol(Socket socket) throws IOException {
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }

    @Override
    public void send(Message<byte[]> msg) {
        char type;
        switch (msg.getType()) {
        case TOPIC:
            type = TOPIC;
            break;
        case INIT:
            type = INIT;
            break;
        case ACK:
            type = ACK;
            break;
        case DATA:
            type = DATA;
            break;
        case CLOSE:
            type = CLOSE;
            break;
        case ERROR:
        default:
            type = ERROR;
            break;
        }

        try {
            out.write(type);
            out.write(Base64.encodeBase64(msg.getData()));
            out.write(CR);
            out.write(LF);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public Message<byte[]> receive() {
        Message.Type type = null;
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        while (true) {
            try {
                int b = in.read();
                // check if a byte is available
                if (b == -1) {
                    break;
                }

                char c = (char) b;
                // the first char defines the type of message
                if (type == null) {
                    switch (c) {
                    case TOPIC:
                        type = Type.TOPIC;
                        break;
                    case INIT:
                        type = Type.INIT;
                        break;
                    case ACK:
                        type = Type.ACK;
                        break;
                    case DATA:
                        type = Type.DATA;
                        break;
                    case CLOSE:
                        type = Type.CLOSE;
                        break;
                    case ERROR:
                    default:
                        type = Type.ERROR;
                        break;
                    }
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
            } catch (IOException e) {
                type = Type.ERROR;
                if (sb.length() == 0) {
                    sb.append(e.getMessage());
                }
                break;
            }
        }
        if (type == null) {
            type = Type.ERROR;
        }
        return new Message<byte[]>(Base64.decodeBase64(sb.toString().getBytes()), type);
    }

}
