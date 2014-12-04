package at.ac.tuwien.dsg.pubsub.network.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import redis.clients.util.RedisInputStream;
import redis.clients.util.RedisOutputStream;
import redis.clients.util.SafeEncoder;
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

    public static final byte DOLLAR_BYTE = '$';
    public static final byte ASTERISK_BYTE = '*';

    public static final char SEPARATOR = ';';

    private Socket socket;
    private RedisInputStream in;
    private RedisOutputStream out;

    /**
     * Default constructor.
     * 
     * @throws IOException
     */
    public SocketByteMessageProtocol(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new RedisInputStream(this.socket.getInputStream());
        this.out = new RedisOutputStream(this.socket.getOutputStream());
    }

    @Override
    public void send(Message<byte[]> msg) throws IOException {
        byte[][] args = { SafeEncoder.encode(msg.getType().toString()), SafeEncoder.encode(msg.getTopic()),
                msg.getData() };

        // write the data as a redis multi-bulk data
        out.write(ASTERISK_BYTE);
        out.writeIntCrLf(args.length); // type, topic and data
        for (final byte[] arg : args) {
            out.write(DOLLAR_BYTE);
            out.writeIntCrLf(arg.length);
            out.write(arg);
            out.writeCrLf();
        }
        // flush the stream
        out.flush();
    }

    @Override
    public Message<byte[]> receive() throws IOException {
        @SuppressWarnings("unchecked")
        List<byte[]> streamData = (List<byte[]>) read(in);

        if (streamData.size() != 3) {
            // error
            throw new IOException("Wrong number of chuncks received");
        }

        Type type = Type.valueOf(SafeEncoder.encode(streamData.get(0)));
        String topic = SafeEncoder.encode(streamData.get(1));
        byte[] data = streamData.get(2);

        return new Message<byte[]>(type, topic, data);
    }

    /**
     * Read a multi-bulk or bulk reply from the given input stream.
     * 
     * @param is
     * @return
     * @throws IOException
     */
    private static Object read(final RedisInputStream is) throws IOException {
        return process(is);
    }

    private static Object process(final RedisInputStream is) throws IOException {
        byte b = is.readByte();
        if (b == ASTERISK_BYTE) {
            return processMultiBulkReply(is);
        } else if (b == DOLLAR_BYTE) {
            return processBulkReply(is);
        } else {
            throw new IOException("Unknown message: " + (char) b);
        }
    }

    private static byte[] processBulkReply(final RedisInputStream is) throws IOException {
        int len = Integer.parseInt(is.readLine());
        if (len == -1) {
            return null;
        }
        byte[] read = new byte[len];
        int offset = 0;
        while (offset < len) {
            int size = is.read(read, offset, (len - offset));
            if (size == -1)
                throw new IOException("It seems like server has closed the connection.");
            offset += size;
        }
        // read 2 more bytes for the command delimiter
        is.readByte();
        is.readByte();

        return read;
    }

    private static List<Object> processMultiBulkReply(final RedisInputStream is) throws IOException {
        int num = Integer.parseInt(is.readLine());
        if (num == -1) {
            return null;
        }
        List<Object> ret = new ArrayList<Object>(num);
        for (int i = 0; i < num; i++) {
            ret.add(process(is));
        }
        return ret;
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
