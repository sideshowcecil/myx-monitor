package at.ac.tuwien.dsg.pubsub.publisher.comp.socket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.dsg.myx.util.MyxUtils;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Message.Type;
import at.ac.tuwien.dsg.pubsub.publisher.comp.MessageCreator;

public class LoopedAudioMessageCreator extends MessageCreator<byte[]> {

    private static Logger logger = LoggerFactory.getLogger(LoopedAudioMessageCreator.class);

    private String audioFileName;
    private int loopCount;

    private ExecutorService executor;
    private Runnable runnable;

    @Override
    public void init() {
        audioFileName = MyxUtils.getInitProperties(this).getProperty("audioFile");
        if (audioFileName == null) {
            throw new RuntimeException("No audio filename given!");
        }
        loopCount = Integer.parseInt(MyxUtils.getInitProperties(this).getProperty("loopCount", "1"));
        executor = Executors.newSingleThreadExecutor();
        runnable = new Runnable() {
            @Override
            public void run() {
                URL url = ClassLoader.getSystemResource(audioFileName);
                if (url != null) {
                    boolean initSent = false;
                    File audioFile = new File(url.getFile());
                    for (int i = 0; i < loopCount; i++) {
                        InputStream in = null;
                        try {
                            in = new FileInputStream(audioFile);

                            int numBytesRead, numBytesToRead = 4096;
                            byte[] buffer = new byte[numBytesToRead];

                            logger.info("Reading audiofile/sending messages (count: " + i + ")");
                            for (long total = 0; total < audioFile.length(); total += numBytesRead) {
                                numBytesRead = in.read(buffer, 0, numBytesToRead);
                                if (numBytesRead == -1) {
                                    break;
                                }
                                if (total == 0) {
                                    if (!initSent) {
                                        publisher.publish(new Message<>(Type.INIT, audioFileName, buffer));
                                        initSent = true;
                                    }
                                } else {
                                    publisher.publish(new Message<>(Type.DATA, audioFileName, buffer));
                                }
                            }
                        } catch (IOException e) {
                        } finally {
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e) {
                                }
                            }
                        }
                    }
                    logger.info("Sending close message");
                    publisher.publish(new Message<byte[]>(Type.CLOSE, audioFileName, new byte[0]));
                    // wait some time so the close message is written to the
                    // network
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
                // exit
                logger.info("Exiting");
                System.exit(0);
            }
        };
    }

    @Override
    public void begin() {
        super.begin();
        executor.execute(runnable);
    }
}
