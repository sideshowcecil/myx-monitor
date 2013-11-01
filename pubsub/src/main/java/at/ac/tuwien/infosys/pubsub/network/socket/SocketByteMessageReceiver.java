package at.ac.tuwien.infosys.pubsub.network.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.apache.commons.codec.binary.Base64;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.message.Message.Type;
import at.ac.tuwien.infosys.pubsub.network.MessageReceiver;

public class SocketByteMessageReceiver implements MessageReceiver<byte[]> {

    private InputStream in;

    public SocketByteMessageReceiver(Socket socket) throws IOException {
        in = socket.getInputStream();
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
                    case Protocol.TOPIC:
                        type = Type.TOPIC;
                        break;
                    case Protocol.INIT:
                        type = Type.INIT;
                        break;
                    case Protocol.DATA:
                        type = Type.DATA;
                        break;
                    case Protocol.CLOSE:
                        type = Type.CLOSE;
                        break;
                    case Protocol.ERROR:
                    default:
                        type = Type.ERROR;
                        break;
                    }
                } else {
                    // read all characters until the end of the message
                    if (c == Protocol.CR && last == Protocol.NULL) {
                        last = c;
                    } else if (last == Protocol.CR && c == Protocol.LF) {
                        sb.setLength(sb.length() - 1);
                        break;
                    }
                    sb.append(c);
                }
            } catch (IOException e) {
                type = Type.ERROR;
                break;
            }
        }
        if (type == null) {
            type = Type.ERROR;
        }
        return new Message<byte[]>(
                Base64.decodeBase64(sb.toString().getBytes()), type);
    }

}
