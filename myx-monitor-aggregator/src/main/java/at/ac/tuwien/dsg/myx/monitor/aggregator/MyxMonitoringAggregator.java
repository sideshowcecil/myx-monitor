package at.ac.tuwien.dsg.myx.monitor.aggregator;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.ac.tuwien.dsg.myx.monitor.Bootstrap;

public class MyxMonitoringAggregator extends Bootstrap {

    public static final String ARCHITECTURE_FILE_NAME = "myx-monitor-aggregator.xml";
    public static final String STRUCTURE_NAME = "aggregator";

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
        adaptedArgs.addAll(realArgs);

        // call the myx-monitor bootstrap
        new MyxMonitoringAggregator().run(adaptedArgs.toArray(new String[0]));
    }

    @Override
    protected void usage() {
        System.err.println("Usage:");
        System.err
                .println("  java "
                        + this.getClass().getName()
                        + " [file] [-s|--structure structureName] [-i|--id architectureRuntimeId] [-d|--event-dispatcher className] [-e|--event-manager className] [-c|--event-manager-connection-string connectionString]");
        System.err.println();
        System.err.println("  where:");
        System.err.println("    file: the name of the xADL file to bootstrap, default: " + ARCHITECTURE_FILE_NAME);
        System.err.println("    -s structureName: the name of the structure to bootstrap, default: " + STRUCTURE_NAME);
        System.err.println("    -i architectureInstanceId: the architecture runtime id");
        System.err.println("    -d className: the event dispatcher class name that should be instantiated");
        System.err.println("    -e className: the event manager class name that should be used to propagate events");
        System.err.println("    -c connectionString: the connection string that should be used to propate events");
        System.err.println();
        System.exit(-2);
    }
}
