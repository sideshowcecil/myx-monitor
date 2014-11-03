package at.ac.tuwien.dsg.pubsub.publisher.comp.socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.publisher.comp.MessageCreator;

public class DummyMessageCreator extends MessageCreator<byte[]> {

    private static Logger logger = LoggerFactory.getLogger(DummyMessageCreator.class);

    private String topicName;
    private int loopCount;
    private int messageCount;
    private int timeout;

    private ExecutorService executor;
    private Runnable runnable;

    @Override
    public void init() {
        topicName = MyxMonitoringUtils.getInitProperties(this).getProperty("topicName");
        if (topicName == null) {
            throw new RuntimeException("No topic given");
        }
        loopCount = Integer.parseInt(MyxMonitoringUtils.getInitProperties(this).getProperty("loopCount", "1"));
        messageCount = Integer.parseInt(MyxMonitoringUtils.getInitProperties(this).getProperty("messageCount", "1"));
        timeout = Integer.parseInt(MyxMonitoringUtils.getInitProperties(this).getProperty("timeout", "1000"));
        executor = Executors.newSingleThreadExecutor();
        runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < loopCount; i++) {
                    logger.info("Sending messages (count: " + i + ")");
                    for (int j = 0; j < messageCount; j++) {
                        publisher.publish(createDummyMessage());
                    }
                    try {
                        Thread.sleep(timeout);
                    } catch (InterruptedException e) {
                        break;
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

    private Message<byte[]> createDummyMessage() {
        return new Message<byte[]>(topicName, new byte[] { 'a', 'b', 'c', 'd' });
    }
}
