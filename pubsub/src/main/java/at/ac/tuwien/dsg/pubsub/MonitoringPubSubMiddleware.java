package at.ac.tuwien.dsg.pubsub;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.ac.tuwien.dsg.myx.monitor.Bootstrap;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.middleware.myx.EventManagerImpl;

public class MonitoringPubSubMiddleware extends Bootstrap {

    public static final String ARCHITECTURE_FILE_NAME = "pubsub.xml";

    public static void main(String[] args) {
        String architectureFilePath = null;

        // check if the architecture file is available in the working directory
        String workingDir = System.getProperty("user.dir");
        if (workingDir != null) {
            File xmlFile = new File(new File(workingDir), ARCHITECTURE_FILE_NAME);
            if (xmlFile.exists()) {
                architectureFilePath = xmlFile.getPath();
            }
        }

        // check if the architecture file is available as a resource
        if (architectureFilePath == null) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL url = classLoader.getResource(ARCHITECTURE_FILE_NAME);
            if (url != null) {
                architectureFilePath = url.getPath();
            }
        }

        List<String> adaptedArgs = new ArrayList<>();
        // if an architecture file was found we add it to the arguments
        if (architectureFilePath != null) {
            adaptedArgs.add(architectureFilePath);
        }
        adaptedArgs.addAll(Arrays.asList(args));

        // call the myx-monitor bootstrap
        new MonitoringPubSubMiddleware().run(adaptedArgs.toArray(new String[0]));
    }

    @Override
    public void initEventManager(String architectureRuntimeId, String hostId, String connectionString) {
        MyxMonitoringUtils.initEventManager(new EventManagerImpl(architectureRuntimeId, hostId, connectionString));
    }
}
