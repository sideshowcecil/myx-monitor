package at.ac.tuwien.dsg.pubsub.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.middleware.arch.network.socket.SocketByteMessageProtocol;

/**
 * Test class to show how the publisher can be created (real Publisher
 * implementation is still a TODO)
 * 
 * @author bernd.rathmanner
 * 
 */
public class PublisherTest {

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException,
            InterruptedException {
        Socket sock = new Socket();
        sock.connect(new InetSocketAddress(6666));
        SocketByteMessageProtocol s = new SocketByteMessageProtocol(sock);

        URL url = ClassLoader.getSystemResource("sound2.wav");
        //URL url = ClassLoader.getSystemResource("sound.wav");
        File file = new File(url.getFile());
        InputStream is = new FileInputStream(file);

        int numBytesRead, numBytesToRead = 1024; // numBytesToRead = 44;
        long total = 0, totalToRead = file.length();
        byte[] myData = new byte[numBytesToRead];
        boolean initSent = false;

        Thread.sleep(1000);
        
        String topic = "test";
        Message<byte[]> m;
        int i = 0;
        while (total < totalToRead) {
            numBytesRead = is.read(myData, 0, numBytesToRead);
            if (numBytesRead == -1)
                break;
            if (!initSent) {
                m = new Message<byte[]>(Message.Type.INIT, topic + "." + ++i, myData);
                initSent = true;
            } else {
                m = new Message<byte[]>(topic + "." + ++i, myData);
            }
            total += numBytesRead;
            s.send(m);
        }
        m = new Message<byte[]>(Message.Type.CLOSE, topic + "." + ++i, new byte[] { 'a' });
        s.send(m);
        is.close();

        // WE HAVE TO WAIT TILL THE CLOSE MESSAGE WAS DELEIVERED
        Thread.sleep(500);

    }

}
