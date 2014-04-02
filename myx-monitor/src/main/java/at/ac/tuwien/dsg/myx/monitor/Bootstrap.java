package at.ac.tuwien.dsg.myx.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import at.ac.tuwien.dsg.myx.monitor.aim.Launcher;
import at.ac.tuwien.dsg.myx.monitor.comp.BootstrapComponent;
import at.ac.tuwien.dsg.myx.monitor.comp.EventDispatcherComponent;
import at.ac.tuwien.dsg.myx.monitor.comp.EventManagerComponent;
import at.ac.tuwien.dsg.myx.monitor.comp.LauncherComponent;
import at.ac.tuwien.dsg.myx.monitor.comp.ModelRootComponent;
import at.ac.tuwien.dsg.myx.monitor.comp.MyxRuntimeComponent;
import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.model.ModelRoot;
import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import edu.uci.isr.myx.fw.EMyxInterfaceDirection;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.IMyxRuntime;
import edu.uci.isr.myx.fw.MyxJavaClassBrickDescription;
import edu.uci.isr.myx.fw.MyxJavaClassInterfaceDescription;

public class Bootstrap {

    public static final IMyxName BOOTSTRAP_NAME = MyxMonitoringUtils.createName("bootstrap-comp");
    public static final IMyxName EVENT_DISPATCHER_NAME = MyxMonitoringUtils.createName("event-dispatcher-comp");
    public static final IMyxName EVENT_MANAGER_NAME = MyxMonitoringUtils.createName("event-manager-comp");
    public static final IMyxName LAUNCHER_NAME = MyxMonitoringUtils.createName("launcher-comp");
    public static final IMyxName MODEL_ROOT_NAME = MyxMonitoringUtils.createName("model-root-comp");
    public static final IMyxName MYX_RUNTIME_NAME = MyxMonitoringUtils.createName("myx-runtime-comp");

    protected IMyxRuntime myx;

    public static void main(String[] args) {
        Properties[] p = parseArgs(args);
        new Bootstrap().doBootstrap(p[0], p[1], p[2], p[3]);
    }

    private static Properties[] parseArgs(String[] args) {
        String xadlFile = null;
        String structureName = null;
        String architectureInstanceId = null;
        List<String> eventDispatcherClasses = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-s")) {
                if (++i == args.length || structureName != null) {
                    usage();
                }
                structureName = args[i];
            } else if (args[i].equals("--id")) {
                if (++i == args.length || architectureInstanceId != null) {
                    usage();
                }
                architectureInstanceId = args[i];
            } else if (args[i].equals("--event-dispatcher")) {
                if (++i == args.length) {
                    usage();
                }
                eventDispatcherClasses.add(args[i]);
            } else {
                if (xadlFile != null) {
                    usage();
                }
                xadlFile = args[i];
            }
        }
        if (xadlFile == null) {
            usage();
        }

        if (architectureInstanceId == null) {
            architectureInstanceId = IdGenerator.generateArchitectureRuntimeId();
        }

        Properties[] p = new Properties[4];

        p[0] = new Properties();
        p[0].setProperty(MyxProperties.XADL_FILE, xadlFile);

        p[1] = new Properties();
        if (structureName != null) {
            p[1].setProperty(MyxProperties.STRUCTURE_NAME, structureName);
        }

        p[2] = new Properties();
        p[2].setProperty(MyxProperties.ARCHITECTURE_RUNTIME_ID, architectureInstanceId);
        p[2].setProperty(MyxProperties.ARCHITECTURE_HOST_ID, IdGenerator.getHostId());

        p[3] = new Properties();
        p[3].setProperty(MyxProperties.ARCHITECTURE_RUNTIME_ID, architectureInstanceId);
        p[3].setProperty(MyxProperties.ARCHITECTURE_HOST_ID, IdGenerator.getHostId());
        p[3].put(MyxProperties.EVENT_DISPATCHER_CLASSES, eventDispatcherClasses.toArray(new String[0]));

        return p;
    }

    private static void usage() {
        System.err.println("Usage:");
        System.err.println("  java " + Bootstrap.class.getName()
                + " file [-s structureName] [--id architectureInstanceId]");
        System.err.println();
        System.err.println("  where:");
        System.err.println("    file: the name of the xADL file to bootstrap");
        System.err.println("    structureName: the name of the structure to bootstrap");
        System.err.println("    architectureInstanceId: the architecture instance id");
        System.err.println();
        System.exit(-2);
    }

    public Bootstrap() {
        myx = MyxMonitoringUtils.getDefaultImplementation().createRuntime();
    }

    public void doBootstrap(Properties modelRootProps, Properties bootstrapProps, Properties myxProps,
            Properties eventDispatcherProps) {
        try {
            // ModelRoot
            MyxJavaClassBrickDescription modelRootDesc = new MyxJavaClassBrickDescription(modelRootProps,
                    ModelRootComponent.class.getName());
            MyxJavaClassInterfaceDescription modelRootIfaceDesc = new MyxJavaClassInterfaceDescription(
                    new String[] { ModelRoot.class.getName() });

            myx.addBrick(null, MODEL_ROOT_NAME, modelRootDesc);
            myx.addInterface(null, MODEL_ROOT_NAME, ModelRootComponent.INTERFACE_NAME_IN_MODELROOT, modelRootIfaceDesc,
                    EMyxInterfaceDirection.IN);

            myx.init(null, MODEL_ROOT_NAME);

            // EventManager
            MyxJavaClassBrickDescription eventManagerDesc = new MyxJavaClassBrickDescription(null,
                    EventManagerComponent.class.getName());
            MyxJavaClassInterfaceDescription eventManagerIfaceDesc = new MyxJavaClassInterfaceDescription(
                    new String[] { EventManager.class.getName() });

            myx.addBrick(null, EVENT_MANAGER_NAME, eventManagerDesc);
            myx.addInterface(null, EVENT_MANAGER_NAME, EventManagerComponent.INTERFACE_NAME_IN_EVENTMANAGER,
                    eventManagerIfaceDesc, EMyxInterfaceDirection.IN);

            myx.init(null, EVENT_MANAGER_NAME);

            // MyxRuntime
            MyxJavaClassBrickDescription myxRuntimeDesc = new MyxJavaClassBrickDescription(myxProps,
                    MyxRuntimeComponent.class.getName());
            MyxJavaClassInterfaceDescription myxIfaceDesc = new MyxJavaClassInterfaceDescription(
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
            MyxJavaClassBrickDescription eventDispatcherDesc = new MyxJavaClassBrickDescription(eventDispatcherProps,
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
            MyxJavaClassBrickDescription launcherDesc = new MyxJavaClassBrickDescription(null,
                    LauncherComponent.class.getName());
            MyxJavaClassInterfaceDescription launcherIfaceDesc = new MyxJavaClassInterfaceDescription(
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
            MyxJavaClassBrickDescription bootstrapDesc = new MyxJavaClassBrickDescription(bootstrapProps,
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

            // TODO: add weld EventManager -> ModelRoot
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-3);
        }
    }

}
