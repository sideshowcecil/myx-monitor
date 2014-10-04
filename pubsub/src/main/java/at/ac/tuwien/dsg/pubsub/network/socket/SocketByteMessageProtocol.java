package at.ac.tuwien.dsg.pubsub.network.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.apache.commons.codec.binary.Base64;

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
    private BufferedReader in;
    private BufferedWriter out;

    /**
     * Default constructor.
     * 
     * @throws IOException
     */
    public SocketByteMessageProtocol(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
    }

    @Override
    public void send(Message<byte[]> msg) throws IOException {
        out.write(msg.getType().toString());
        out.write(SEPARATOR);
        out.write(msg.getTopic());
        out.write(SEPARATOR);
        out.write(Base64.encodeBase64String(msg.getData()));
        out.write(CR);
        out.write(LF);
        out.flush();
    }

    @Override
    public Message<byte[]> receive() throws IOException {
        String line = in.readLine();
        if (line == null) {
            // the end of stream was reached, thus we return a close message
            return new Message<byte[]>(Type.CLOSE, "", new byte[0]);
        }
        String[] parts = line.split(SEPARATOR + "");
        String data;
        if (parts.length == 2 && line.endsWith(SEPARATOR + "")) {
            // if no data is given the part is not contained, thus we have to
            // fill it manually
            data = "";
        } else if (parts.length < 3) {
            // not all parts are available
            return new Message<byte[]>(Type.ERROR, "", new byte[0]);
        } else {
            data = parts[2];
        }

        Type type = null;
        try {
            type = Type.valueOf(parts[0]);
        } catch (IllegalArgumentException e) {
            // the type cannot be resolved
            type = Type.ERROR;
        }
        return new Message<byte[]>(type, parts[1], Base64.decodeBase64(data));
    }

    @Override
    public void close() {
        try {
            in.close();
            out.close();
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
