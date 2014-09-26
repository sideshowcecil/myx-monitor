package at.ac.tuwien.dsg.pubsub.publisher.comp.socket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Message.Type;
import at.ac.tuwien.dsg.pubsub.publisher.comp.MessageCreator;

public class LoopedAudioMessageCreator extends MessageCreator<byte[]> {

    private String audioFileName;
    private int loopCount;

    private ExecutorService executor;
    private Runnable runnable;

    @Override
    public void init() {
        audioFileName = MyxMonitoringUtils.getInitProperties(this).getProperty("audioFile");
        if (audioFileName == null) {
            throw new RuntimeException("No audio filename given!");
        }
        loopCount = Integer.parseInt(MyxMonitoringUtils.getInitProperties(this).getProperty("loopCount", "1"));
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

                            int numBytesRead, numBytesToRead = 1024;
                            byte[] buffer = new byte[numBytesToRead];

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
                    publisher.publish(new Message<byte[]>(Type.CLOSE, audioFileName, new byte[0]));
                    // wait some time so the close message is written to the
                    // network
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
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
