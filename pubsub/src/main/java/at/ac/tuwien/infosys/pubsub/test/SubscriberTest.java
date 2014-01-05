package at.ac.tuwien.infosys.pubsub.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.message.Message.Type;
import at.ac.tuwien.infosys.pubsub.network.socket.SocketByteMessageProtocol;

/**
 * Test class to show how the subscriber can be created (real Subscriber
 * implementation is still a TODO)
 * 
 * @author bernd.rathmanner
 * 
 */
public class SubscriberTest {

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException,
            InterruptedException {
        Socket socketReceiver = new Socket();
        socketReceiver.connect(new InetSocketAddress(6667));
        SocketByteMessageProtocol protocol = new SocketByteMessageProtocol(socketReceiver);

        protocol.send(new Message<byte[]>("test".getBytes(), Type.TOPIC));

        SourceDataLine line = null;
        boolean play = true;
        while (play) {
            Message<byte[]> m = protocol.receive();
            switch (m.getType()) {
            case TOPIC:
                break;
            case INIT:
                AudioInputStream ais = AudioSystem.getAudioInputStream(new ByteArrayInputStream(m.getData()));
                line = AudioSystem.getSourceDataLine(ais.getFormat());
                line.open();
                line.start();
                break;
            case DATA:
                if (line != null) {
                    line.write(m.getData(), 0, m.getData().length);
                }
                break;
            case CLOSE:
            case ERROR:
            default:
                play = false;
                break;
            }
        }
        if (line != null) {
            line.stop();
            line.close();
        }

        socketReceiver.close();
    }

}
