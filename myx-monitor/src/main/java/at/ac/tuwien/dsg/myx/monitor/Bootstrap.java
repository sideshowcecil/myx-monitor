package at.ac.tuwien.dsg.myx.monitor;

import at.ac.tuwien.dsg.myx.monitor.aim.Launcher;
import at.ac.tuwien.dsg.myx.monitor.comp.*;
import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.EventManagerImpl;
import at.ac.tuwien.dsg.myx.monitor.model.ModelRoot;
import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.myx.util.MyxUtils;
import edu.uci.isr.myx.fw.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Bootstrap {

    public static final IMyxName BOOTSTRAP_NAME = MyxUtils.createName("bootstrap-comp");
    public static final IMyxName EVENT_DISPATCHER_NAME = MyxUtils.createName("event-dispatcher-comp");
    public static final IMyxName EVENT_MANAGER_NAME = MyxUtils.createName("event-manager-comp");
    public static final IMyxName LAUNCHER_NAME = MyxUtils.createName("launcher-comp");
    public static final IMyxName MODEL_ROOT_NAME = MyxUtils.createName("model-root-comp");
    public static final IMyxName MYX_RUNTIME_NAME = MyxUtils.createName("myx-runtime-comp");

    private IMyxRuntime myx;

    public static void main(String[] args) {
        new Bootstrap().run(args);
    }

    public Bootstrap() {
        myx = MyxUtils.getDefaultImplementation().createRuntime();
    }

    /**
     * Run the application.
     * 
     * @param args
     */
    public void run(String[] args) {
        // parse arguments
        Properties[] p = parseArgs(args);

        // init
        initEventManager(p[3].getProperty(MyxProperties.ARCHITECTURE_RUNTIME_ID),
                p[3].getProperty(MyxProperties.ARCHITECTURE_HOST_ID),
                p[3].getProperty(MyxProperties.EVENT_MANAGER_CLASS),
                p[3].getProperty(MyxProperties.EVENT_MANAGER_CONNECTION_STRING));
        initMyxMonitoringImplementation();

        // bootstrap components
        doBootstrap(p[0], p[1], p[2]);
    }

    /**
     * Parse the command line arguments.
     * 
     * @param args
     * @return
     */
    protected Properties[] parseArgs(String[] args) {
        String xadlFile = null;
        String structureName = null;
        String architectureRuntimeId = null;
        List<String> eventDispatcherClasses = new ArrayList<>();
        String eventManagerClass = "";
        String connectionString = "";

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-s") || args[i].equals("--structure")) {
                if (++i == args.length || structureName != null) {
                    usage();
                }
                structureName = args[i];
            } else if (args[i].equals("-i") || args[i].equals("--id")) {
                if (++i == args.length || architectureRuntimeId != null) {
                    usage();
                }
                architectureRuntimeId = args[i];
            } else if (args[i].equals("-d") || args[i].equals("--event-dispatcher")) {
                if (++i == args.length) {
                    usage();
                }
                eventDispatcherClasses.add(args[i]);
            } else if (args[i].equals("-e") || args[i].equals("--event-manager")) {
                if (++i == args.length) {
                    usage();
                }
                eventManagerClass = args[i];
            } else if (args[i].equals("-c") || args[i].equals("--event-manager-connection-string")) {
                if (++i == args.length) {
                    usage();
                }
                connectionString = args[i];
            } else if (xadlFile == null) {
                xadlFile = args[i];
            }
        }
        if (xadlFile == null) {
            usage();
        }

        if (architectureRuntimeId == null) {
            architectureRuntimeId = IdGenerator.generateArchitectureRuntimeId();
        }

        Properties[] p = new Properties[4];

        p[0] = new Properties();
        p[0].setProperty(MyxProperties.XADL_FILE, xadlFile);

        p[1] = new Properties();
        if (structureName != null) {
            p[1].setProperty(MyxProperties.STRUCTURE_NAME, structureName);
        }

        p[2] = new Properties();
        p[2].put(MyxProperties.EVENT_DISPATCHER_CLASSES, eventDispatcherClasses.toArray(new String[eventDispatcherClasses.size()]));

        p[3] = new Properties();
        p[3].setProperty(MyxProperties.ARCHITECTURE_RUNTIME_ID, architectureRuntimeId);
        p[3].setProperty(MyxProperties.ARCHITECTURE_HOST_ID, IdGenerator.getHostId());
        p[3].setProperty(MyxProperties.EVENT_MANAGER_CLASS, eventManagerClass);
        p[3].setProperty(MyxProperties.EVENT_MANAGER_CONNECTION_STRING, connectionString);

        return p;
    }

    /**
     * Print usage information and exit.
     */
    protected void usage() {
        System.err.println("Usage:");
        System.err
                .println("  java "
                        + this.getClass().getName()
                        + " file [-s|--structure structureName] [-i|--id architectureRuntimeId] [-d|--event-dispatcher className] [-e|--event-manager className] [-c|--event-manager-connection-string connectionString]");
        System.err.println();
        System.err.println("  where:");
        System.err.println("    file: the name of the xADL file to bootstrap");
        System.err.println("    -s structureName: the name of the structure to bootstrap");
        System.err.println("    -i architectureInstanceId: the architecture runtime id");
        System.err.println("    -d className: the event dispatcher class name that should be instantiated");
        System.err.println("    -e className: the event manager class name that should be used to propagate events");
        System.err.println("    -c connectionString: the connection string that should be used to propate events");
        System.err.println();
        System.exit(-2);
    }

    /**
     * Initialize the {@link EventManager} instance.
     * 
     * @param architectureRuntimeId
     * @param hostId
     * @param connectionString
     */
    protected static void initEventManager(String architectureRuntimeId, String hostId, String eventManagerClassName,
            String connectionString) {
        try {
            // check if the event manager has already been initialized
            MyxUtils.getEventManager();
            return;
        } catch (NullPointerException e) {
        }
        
        EventManager eventManager = null;
        if (!eventManagerClassName.isEmpty()) {
            try {
                Class<?> eventManagerClass = Class.forName(eventManagerClassName);
                if (EventManager.class.isAssignableFrom(eventManagerClass)) {
                    Constructor<?> c = eventManagerClass.getConstructor(String.class, String.class,
                            String.class);
                    eventManager = (EventManager) c.newInstance(architectureRuntimeId, hostId, connectionString);
                }
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
                    | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                // we ignore non compatible event managers
            }
        }
        if (eventManager == null) {
            eventManager = new EventManagerImpl();
        }

        MyxUtils.initEventManager(eventManager);
    }

    /**
     * Initialize the {@link MyxMonitoringImplementation}.
     */
    protected void initMyxMonitoringImplementation() {
        MyxUtils.initMontioringImplementation(MyxUtils.getEventManager());
    }

    /**
     * Bootstrap all bricks and welds and run the application.
     * 
     * @param modelRootProps
     * @param bootstrapProps
     * @param eventDispatcherProps
     */
    protected void doBootstrap(Properties modelRootProps, Properties bootstrapProps, Properties eventDispatcherProps) {
        try {
            // ModelRoot
            IMyxBrickDescription modelRootDesc = new MyxJavaClassBrickDescription(modelRootProps,
                    ModelRootComponent.class.getName());
            IMyxInterfaceDescription modelRootIfaceDesc = new MyxJavaClassInterfaceDescription(
                    new String[] { ModelRoot.class.getName() });

            myx.addBrick(null, MODEL_ROOT_NAME, modelRootDesc);
            myx.addInterface(null, MODEL_ROOT_NAME, ModelRootComponent.INTERFACE_NAME_IN_MODELROOT, modelRootIfaceDesc,
                    EMyxInterfaceDirection.IN);

            myx.init(null, MODEL_ROOT_NAME);

            // EventManager
            IMyxBrickDescription eventManagerDesc = new MyxJavaClassBrickDescription(null,
                    EventManagerComponent.class.getName());
            IMyxInterfaceDescription eventManagerIfaceDesc = new MyxJavaClassInterfaceDescription(
                    new String[] { EventManager.class.getName() });

            myx.addBrick(null, EVENT_MANAGER_NAME, eventManagerDesc);
            myx.addInterface(null, EVENT_MANAGER_NAME, EventManagerComponent.INTERFACE_NAME_IN_EVENTMANAGER,
                    eventManagerIfaceDesc, EMyxInterfaceDirection.IN);

            myx.init(null, EVENT_MANAGER_NAME);

            // MyxRuntime
            IMyxBrickDescription myxRuntimeDesc = new MyxJavaClassBrickDescription(null,
                    MyxRuntimeComponent.class.getName());
            IMyxInterfaceDescription myxIfaceDesc = new MyxJavaClassInterfaceDescription(
                    new String[] { IMyxRuntime.class.getName() });

            myx.addBrick(null, MYX_RUNTIME_NAME, myxRuntimeDesc);
            myx.addInterface(null, MYX_RUNTIME_NAME, MyxRuntimeComponent.INTERFACE_NAME_IN_MYXRUNTIME, myxIfaceDesc,
                    EMyxInterfaceDirection.IN);
            myx.addInterface(null, MYX_RUNTIME_NAME, MyxRuntimeComponent.INTERFACE_NAME_OUT_EVENTMANAGER,
                    eventManagerIfaceDesc, EMyxInterfaceDirection.OUT);

            // MyxRuntime -> EventManager
            myx.addWeld(myx.createWeld(null, MYX_RUNTIME_NAME, MyxRuntimeComponent.INTERFACE_NAME_OUT_EVENTMANAGER,
                    null, EVENT_MANAGER_NAME, EventManagerComponent.INTERFACE_NAME_IN_EVENTMANAGER));

            myx.init(null, MYX_RUNTIME_NAME);

            // EventDispatcher
            IMyxBrickDescription eventDispatcherDesc = new MyxJavaClassBrickDescription(eventDispatcherProps,
                    EventDispatcherComponent.class.getName());

            myx.addBrick(null, EVENT_DISPATCHER_NAME, eventDispatcherDesc);
            myx.addInterface(null, EVENT_DISPATCHER_NAME, EventDispatcherComponent.INTERFACE_NAME_OUT_EVENTMANAGER,
                    eventManagerIfaceDesc, EMyxInterfaceDirection.OUT);

            // EventDispatcher -> EventManager
            myx.addWeld(myx.createWeld(null, EVENT_DISPATCHER_NAME,
                    EventDispatcherComponent.INTERFACE_NAME_OUT_EVENTMANAGER, null, EVENT_MANAGER_NAME,
                    EventManagerComponent.INTERFACE_NAME_IN_EVENTMANAGER));
            myx.init(null, EVENT_DISPATCHER_NAME);

            // Launcher
            IMyxBrickDescription launcherDesc = new MyxJavaClassBrickDescription(null,
                    LauncherComponent.class.getName());
            IMyxInterfaceDescription launcherIfaceDesc = new MyxJavaClassInterfaceDescription(
                    new String[] { Launcher.class.getName() });

            myx.addBrick(null, LAUNCHER_NAME, launcherDesc);
            myx.addInterface(null, LAUNCHER_NAME, LauncherComponent.INTERFACE_NAME_OUT_MYXRUNTIME, myxIfaceDesc,
                    EMyxInterfaceDirection.OUT);
            myx.addInterface(null, LAUNCHER_NAME, LauncherComponent.INTERFACE_NAME_OUT_MODELROOT, myxIfaceDesc,
                    EMyxInterfaceDirection.OUT);
            myx.addInterface(null, LAUNCHER_NAME, LauncherComponent.INTERFACE_NAME_IN_LAUNCHER, launcherIfaceDesc,
                    EMyxInterfaceDirection.IN);

            // Launcher -> ModelRoot
            myx.addWeld(myx.createWeld(null, LAUNCHER_NAME, LauncherComponent.INTERFACE_NAME_OUT_MODELROOT, null,
                    MODEL_ROOT_NAME, ModelRootComponent.INTERFACE_NAME_IN_MODELROOT));

            // Launcher -> MyxRuntime
            myx.addWeld(myx.createWeld(null, LAUNCHER_NAME, LauncherComponent.INTERFACE_NAME_OUT_MYXRUNTIME, null,
                    MYX_RUNTIME_NAME, MyxRuntimeComponent.INTERFACE_NAME_IN_MYXRUNTIME));

            myx.init(null, LAUNCHER_NAME);

            // Bootstrap
            IMyxBrickDescription bootstrapDesc = new MyxJavaClassBrickDescription(bootstrapProps,
                    BootstrapComponent.class.getName());

            myx.addBrick(null, BOOTSTRAP_NAME, bootstrapDesc);
            myx.addInterface(null, BOOTSTRAP_NAME, BootstrapComponent.INTERFACE_NAME_OUT_LAUNCHER, launcherIfaceDesc,
                    EMyxInterfaceDirection.OUT);
            myx.addInterface(null, BOOTSTRAP_NAME, BootstrapComponent.INTERFACE_NAME_OUT_MODELROOT, modelRootIfaceDesc,
                    EMyxInterfaceDirection.OUT);

            // Bootstrap -> Launcher
            myx.addWeld(myx.createWeld(null, BOOTSTRAP_NAME, BootstrapComponent.INTERFACE_NAME_OUT_LAUNCHER, null,
                    LAUNCHER_NAME, LauncherComponent.INTERFACE_NAME_IN_LAUNCHER));

            // Bootstrap -> ModelRoot
            myx.addWeld(myx.createWeld(null, BOOTSTRAP_NAME, BootstrapComponent.INTERFACE_NAME_OUT_MODELROOT, null,
                    MODEL_ROOT_NAME, ModelRootComponent.INTERFACE_NAME_IN_MODELROOT));

            myx.init(null, BOOTSTRAP_NAME);

            myx.begin(null, MODEL_ROOT_NAME);
            myx.begin(null, EVENT_MANAGER_NAME);
            myx.begin(null, MYX_RUNTIME_NAME);
            myx.begin(null, EVENT_DISPATCHER_NAME);
            myx.begin(null, LAUNCHER_NAME);
            myx.begin(null, BOOTSTRAP_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-3);
        }
    }

}
