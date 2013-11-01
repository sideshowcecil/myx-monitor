package at.ac.tuwien.infosys.pubsub.network.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.codec.binary.Base64;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.network.MessageSender;

public class SocketByteMessageSender implements MessageSender<byte[]> {

    private OutputStream out;

    public SocketByteMessageSender(Socket socket) throws IOException {
        out = socket.getOutputStream();
    }

    @Override
    public void send(Message<byte[]> msg) {

        char type;
        switch (msg.getType()) {
        case TOPIC:
            type = Protocol.TOPIC;
            break;
        case INIT:
            type = Protocol.INIT;
            break;
        case DATA:
            type = Protocol.DATA;
            break;
        case CLOSE:
            type = Protocol.CLOSE;
            break;
        case ERROR:
        default:
            type = Protocol.ERROR;
            break;
        }

        long start, end;
        try {
            out.write(type);
            start = System.currentTimeMillis();
            byte[] data = Base64.encodeBase64(msg.getData());
            end = System.currentTimeMillis();
            if (end - start > 75) {
                System.err.println("a: " + (end - start));
            }
            start = System.currentTimeMillis();
            out.write(data);
            end = System.currentTimeMillis();
            if (end - start > 75) {
                System.err.println("b: " + (end - start));
            }
            out.write(Protocol.CR);
            out.write(Protocol.LF);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
