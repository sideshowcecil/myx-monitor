package at.ac.tuwien.dsg.pubsub;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.ac.tuwien.dsg.myx.monitor.Bootstrap;
import at.ac.tuwien.dsg.pubsub.em.EventManagerImpl;

public class MonitoringSubscriber extends Bootstrap {

    public static final String ARCHITECTURE_FILE_NAME = "pubsub.xml";
    public static final String STRUCTURE_NAME = "sub";

    public static void main(String[] args) {
        List<String> realArgs = Arrays.asList(args);

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
        // add the structure name if none was given
        if (!realArgs.contains("-s") && !realArgs.contains("--structure")) {
            adaptedArgs.add("--structure");
            adaptedArgs.add(STRUCTURE_NAME);
        }
        // add the event manager class if none was given
        if (!realArgs.contains("-e") && !realArgs.contains("--event-manager")) {
            adaptedArgs.add("--event-manager");
            adaptedArgs.add(EventManagerImpl.class.getName());
        }
        adaptedArgs.addAll(realArgs);

        // call the myx-monitor bootstrap
        new MonitoringSubscriber().run(adaptedArgs.toArray(new String[0]));
    }
}
