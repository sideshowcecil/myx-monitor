package at.ac.tuwien.dsg.myx.monitor;

import java.util.Properties;

import at.ac.tuwien.dsg.myx.MyxUtils;
import at.ac.tuwien.dsg.myx.monitor.aim.Launcher;
import at.ac.tuwien.dsg.myx.monitor.comp.BootstrapComponent;
import at.ac.tuwien.dsg.myx.monitor.comp.EventManagerComponent;
import at.ac.tuwien.dsg.myx.monitor.comp.LauncherComponent;
import at.ac.tuwien.dsg.myx.monitor.comp.ModelRootComponent;
import at.ac.tuwien.dsg.myx.monitor.comp.MyxRuntimeComponent;
import at.ac.tuwien.dsg.myx.monitor.event.EventManager;
import at.ac.tuwien.dsg.myx.monitor.model.ModelRoot;
import edu.uci.isr.myx.fw.EMyxInterfaceDirection;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.IMyxRuntime;
import edu.uci.isr.myx.fw.MyxJavaClassBrickDescription;
import edu.uci.isr.myx.fw.MyxJavaClassInterfaceDescription;

public class Bootstrap {

    public static final IMyxName BOOTSTRAP_NAME = MyxUtils.createName("bootstrap-comp");
    public static final IMyxName EVENTMANAGER_NAME = MyxUtils.createName("event-manager-comp");
    public static final IMyxName LAUNCHER_NAME = MyxUtils.createName("launcher-comp");
    public static final IMyxName MODEL_ROOT_NAME = MyxUtils.createName("model-root-comp");
    public static final IMyxName MYX_RUNTIME_NAME = MyxUtils.createName("myx-runtime-comp");

    protected IMyxRuntime myx;

    public static void main(String[] args) {
        Properties[] p = parseArgs(args);
        new Bootstrap().doBootstrap(p[0], p[1]);
    }

    private static Properties[] parseArgs(String[] args) {
        String xadlFile = null;
        String structureName = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-s")) {
                if (++i == args.length || structureName != null) {
                    usage();
                }
                structureName = args[i];
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

        Properties[] p = new Properties[2];
        p[0] = new Properties();
        if (structureName != null) {
            p[0].setProperty("structure", structureName);
        }
        p[1] = new Properties();
        p[1].setProperty("file", xadlFile);
        
        return p;
    }

    private static void usage() {
        System.err.println("Usage:");
        System.err.println("  java " + Bootstrap.class.getName() + " file [-s structureName]");
        System.err.println();
        System.err.println("  where:");
        System.err.println("    file: the name of the xADL file to bootstrap");
        System.err.println("    structureName: the name of the structure to bootstrap");
        System.err.println();
        System.exit(-2);
    }

    public Bootstrap() {
        myx = MyxUtils.getDefaultImplementation().createRuntime();
    }

    public void doBootstrap(Properties bootstrapProps, Properties modelRootProps) {
        try {
            MyxJavaClassBrickDescription bootstrapDesc = new MyxJavaClassBrickDescription(bootstrapProps,
                    BootstrapComponent.class.getName());
            MyxJavaClassBrickDescription eventManagerDesc = new MyxJavaClassBrickDescription(null,
                    EventManagerComponent.class.getName());
            MyxJavaClassBrickDescription launcherDesc = new MyxJavaClassBrickDescription(null,
                    LauncherComponent.class.getName());
            MyxJavaClassBrickDescription modelRootDesc = new MyxJavaClassBrickDescription(modelRootProps,
                    ModelRootComponent.class.getName());
            MyxJavaClassBrickDescription myxruntimeDesc = new MyxJavaClassBrickDescription(null,
                    MyxRuntimeComponent.class.getName());

            MyxJavaClassInterfaceDescription eventManagerIfaceDesc = new MyxJavaClassInterfaceDescription(
                    new String[] { EventManager.class.getName() });
            MyxJavaClassInterfaceDescription myxIfaceDesc = new MyxJavaClassInterfaceDescription(
                    new String[] { IMyxRuntime.class.getName() });
            MyxJavaClassInterfaceDescription launcherIfaceDesc = new MyxJavaClassInterfaceDescription(
                    new String[] { Launcher.class.getName() });
            MyxJavaClassInterfaceDescription modelRootIfaceDesc = new MyxJavaClassInterfaceDescription(
                    new String[] { ModelRoot.class.getName() });

            // ModelRoot
            myx.addBrick(null, MODEL_ROOT_NAME, modelRootDesc);
            myx.addInterface(null, MODEL_ROOT_NAME, ModelRootComponent.INTERFACE_NAME_IN_MODELROOT, modelRootIfaceDesc,
                    EMyxInterfaceDirection.IN);

            myx.init(null, MODEL_ROOT_NAME);

            // EventManager
            myx.addBrick(null, EVENTMANAGER_NAME, eventManagerDesc);
            myx.addInterface(null, EVENTMANAGER_NAME, EventManagerComponent.INTERFACE_NAME_IN_EVENTMANAGER,
                    eventManagerIfaceDesc, EMyxInterfaceDirection.IN);

            myx.init(null, EVENTMANAGER_NAME);

            // MyxRuntime
            myx.addBrick(null, MYX_RUNTIME_NAME, myxruntimeDesc);
            myx.addInterface(null, MYX_RUNTIME_NAME, MyxRuntimeComponent.INTERFACE_NAME_IN_MYXRUNTIME, myxIfaceDesc,
                    EMyxInterfaceDirection.IN);
            myx.addInterface(null, MYX_RUNTIME_NAME, MyxRuntimeComponent.INTERFACE_NAME_OUT_EVENTMANAGER,
                    eventManagerIfaceDesc, EMyxInterfaceDirection.OUT);

            // MyxRuntime -> EventManager
            myx.addWeld(myx.createWeld(null, MYX_RUNTIME_NAME, MyxRuntimeComponent.INTERFACE_NAME_OUT_EVENTMANAGER,
                    null, EVENTMANAGER_NAME, EventManagerComponent.INTERFACE_NAME_IN_EVENTMANAGER));

            myx.init(null, MYX_RUNTIME_NAME);
            
            // TODO Create and bind additional event dispatchers

            // Launcher
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
            myx.begin(null, EVENTMANAGER_NAME);
            myx.begin(null, MYX_RUNTIME_NAME);
            myx.begin(null, LAUNCHER_NAME);
            myx.begin(null, BOOTSTRAP_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-3);
        }
    }

}
