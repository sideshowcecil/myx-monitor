package at.ac.tuwien.dsg.pubsub.subscriber.comp.socket;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.subscriber.comp.MessageConsumer;

public class AudioMessageConsumer extends MessageConsumer<byte[]> {

    SourceDataLine audioLine;

    @Override
    public void consume(Message<byte[]> msg) {
        switch (msg.getType()) {
        case TOPIC:
            break;
        case INIT:
            try {
                AudioInputStream in = AudioSystem.getAudioInputStream(new ByteArrayInputStream(msg.getData()));
                audioLine = AudioSystem.getSourceDataLine(in.getFormat());
                audioLine.open();
                audioLine.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                audioLine = null;
            }
            break;
        case DATA:
            if (audioLine != null) {
                audioLine.write(msg.getData(), 0, msg.getData().length);
            }
            break;
        case CLOSE:
            if (audioLine != null) {
                audioLine.stop();
                audioLine.close();
            }
        case ERROR:
        default:
            audioLine = null;
            break;
        }
    }

    @Override
    public void end() {
        if (audioLine != null) {
            audioLine.stop();
            audioLine.close();
        }
    }

}
