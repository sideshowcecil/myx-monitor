package at.ac.tuwien.dsg.pubsub;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.ac.tuwien.dsg.myx.monitor.Bootstrap;

public class MonitoringPubSubMiddleware {

    public static final String ARCHITECTURE_FILE_NAME = "pubsub.xml";

    public static void main(String[] args) {
        String architectureFILEPath = null;

        // check if the architecture file is available in the working directory
        String workingDir = System.getProperty("user.dir");
        if (workingDir != null) {
            File xmlFile = new File(new File(workingDir), ARCHITECTURE_FILE_NAME);
            if (xmlFile.exists()) {
                architectureFILEPath = xmlFile.getPath();
            }
        }

        // check if the architecture file is available as a resource
        if (architectureFILEPath == null) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL url = classLoader.getResource(ARCHITECTURE_FILE_NAME);
            if (url != null) {
                architectureFILEPath = url.getPath();
            }
        }

        List<String> adaptedArgs = new ArrayList<>();
        // if an architecture file was found we add it to the arguments
        if (architectureFILEPath != null) {
            adaptedArgs.add(architectureFILEPath);
        }
        adaptedArgs.addAll(Arrays.asList(args));
        // call the myx-monitor bootstrap
        Bootstrap.main(adaptedArgs.toArray(new String[0]));
    }
}
