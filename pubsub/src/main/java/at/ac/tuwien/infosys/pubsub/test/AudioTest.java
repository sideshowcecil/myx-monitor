package at.ac.tuwien.infosys.pubsub.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.message.Message.Type;
import at.ac.tuwien.infosys.pubsub.network.socket.SocketByteMessageProtocol;

/**
 * Test class for sampled lib.
 * 
 * @author bernd.rathmanner
 * 
 */
public class AudioTest {

    // private static SourceDataLine line;

    public static void main(String[] args)
            throws UnsupportedAudioFileException, IOException,
            LineUnavailableException, InterruptedException {
        ServerSocket ss = new ServerSocket(6666);
        Socket socketSender = new Socket();
        socketSender.connect(new InetSocketAddress(6666));
        Socket socketReceiver = ss.accept();
        SocketByteMessageProtocol s = new SocketByteMessageProtocol(
                socketSender);
        SocketByteMessageProtocol r = new SocketByteMessageProtocol(
                socketReceiver);

        URL url = ClassLoader.getSystemResource("sound.wav");
        File file = new File(url.getFile());
        InputStream is = new FileInputStream(file);
        AudioFormat format = AudioSystem.getAudioFileFormat(url).getFormat();
        System.out.println(format);

        SourceDataLine line = AudioSystem.getSourceDataLine(format);
        line = AudioSystem.getSourceDataLine(format);

        line.open();
        line.start();

        int numBytesRead, numBytesToRead = 1024; // numBytesToRead = 44;
        long total = 0, totalToRead = file.length();
        byte[] myData = new byte[numBytesToRead];
        boolean formatRead = false;

        // ExecutorService es = Executors.newCachedThreadPool();

        while (total < totalToRead) {
            numBytesRead = is.read(myData, 0, numBytesToRead);
            if (!formatRead) {
                // try {
                AudioInputStream ais = AudioSystem
                        .getAudioInputStream(new ByteArrayInputStream(myData));
                System.out.println(ais.getFormat());
                // System.out.println(new String(myData));
                formatRead = true;
                // } catch (Exception e) {}
            }
            /* break; */
            if (numBytesRead == -1)
                break;
            total += numBytesRead;
            Message<byte[]> m = new Message<byte[]>(myData, Type.INIT);
            s.send(m);
            m = r.receive();
            // System.out.println(new String((byte[])m.getData()));
            line.write((byte[]) m.getData(), 0, numBytesRead);
            // break;
            // es.execute(new FutureTask<>(new WriteLine(myData)));
        }

        line.stop();
        line.close();
        is.close();

        ss.close();

    }

    /*
     * private static class WriteLine implements Callable<Void> {
     * 
     * private byte[] buffer;
     * 
     * public WriteLine(byte[] buffer) { this.buffer = buffer; }
     * 
     * @Override public Void call() throws Exception { synchronized (line) {
     * line.write(buffer, 0, buffer.length); } return null; }
     * 
     * }
     */

}
