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

public class AudioMessageCreator extends MessageCreator<byte[]> {

    private String audioFileName;

    private ExecutorService executor;
    private Runnable runnable;

    @Override
    public void init() {
        audioFileName = MyxMonitoringUtils.getInitProperties(this).getProperty("audioFile");
        if (audioFileName == null) {
            throw new RuntimeException("No audio filename given!");
        }
        executor = Executors.newSingleThreadExecutor();
        runnable = new Runnable() {
            @Override
            public void run() {
                URL url = ClassLoader.getSystemResource(audioFileName);
                if (url != null) {
                    File audioFile = new File(url.getFile());
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
                            publisher.publish(new Message<>(total == 0 ? Type.INIT : Type.DATA, audioFileName, buffer));
                        }
                        publisher.publish(new Message<byte[]>(Type.CLOSE, audioFileName, new byte[0]));
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
