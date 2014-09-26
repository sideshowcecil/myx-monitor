package at.ac.tuwien.dsg.myx.monitor.evaluation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.ac.tuwien.dsg.myx.monitor.Bootstrap;

public final class LoadTestBootstrap extends Bootstrap {

    public static void main(final String[] args) {
        // read the count argument if it is given
        int count = 1;
        int rampUpTime = 0;
        int runtime = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-a") || args[i].equals("--amount")) {
                if (++i == args.length) {
                    new LoadTestBootstrap().usage();
                }
                count = Integer.parseInt(args[i]);
            } else if (args[i].equals("-r") || args[i].equals("--ramp-up-time")) {
                if (++i == args.length) {
                    new LoadTestBootstrap().usage();
                }
                rampUpTime = Integer.parseInt(args[i]);
            } else if (args[i].equals("-t") || args[i].equals("--run-time")) {
                if (++i == args.length) {
                    new LoadTestBootstrap().usage();
                }
                runtime = Integer.parseInt(args[i]);
            }
        }

        // compute the time to sleep between each launch
        int timeInBetween = rampUpTime <= 0 ? 0 : (int) rampUpTime * 1000 / count;

        long maximumRuntime = runtime > 0 ? (System.currentTimeMillis() / 1000) + runtime : 0;
        ExecutorService executor = Executors.newFixedThreadPool(count);
        // launch the application <count> times
        for (int i = 0; i < count; i++) {
            // run the instance
            System.out.println("Starting instance " + (i + 1));
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    new LoadTestBootstrap().run(args);
                }
            });
            if (timeInBetween > 0) {
                try {
                    // sleep to match the ramp up time
                    Thread.sleep(timeInBetween);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
        if (maximumRuntime > 0) {
            try {
                // sleep until we should stop
                Thread.sleep(maximumRuntime - (System.currentTimeMillis() / 1000));
            } catch (InterruptedException e) {
            }
            // shutdown the instances
            executor.shutdownNow();
        } else {
            executor.shutdown();
        }
    }

    /**
     * Print usage information and exit.
     */
    @Override
    protected void usage() {
        System.err.println("Usage:");
        System.err
                .println("  java "
                        + this.getClass().getName()
                        + " file [-s|--structure structureName] [-i|--id architectureRuntimeId] [-d|--event-dispatcher className] [-e|--event-manager className] [-c|--event-manager-connection-string connectionString] [-a|--amount count] [-r|--ramp-up-time seconds] [-t | --run-time seconds]");
        System.err.println();
        System.err.println("  where:");
        System.err.println("    file: the name of the xADL file to bootstrap");
        System.err.println("    -s structureName: the name of the structure to bootstrap");
        System.err.println("    -i architectureInstanceId: the architecture runtime id");
        System.err.println("    -d className: the event dispatcher class name that should be instantiated");
        System.err.println("    -e className: the event manager class name that should be used to propagate events");
        System.err.println("    -c connectionString: the connection string that should be used to propate events");
        System.err.println("    -a count: the amount of instances to create");
        System.err.println("    -r seconds: the time in which the instances should be launched");
        System.err.println("    -t seconds: the time in seconds how long the application is kept running");
        System.err.println();
        System.exit(-2);
    }
}
