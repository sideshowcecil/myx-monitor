package at.ac.tuwien.dsg.myx.monitor.comp;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.dsg.myx.DBLUtils;
import at.ac.tuwien.dsg.myx.MyxMonitoringUtils;
import at.ac.tuwien.dsg.myx.monitor.aim.ArchitectureInstantiationException;
import at.ac.tuwien.dsg.myx.monitor.aim.Launcher;
import at.ac.tuwien.dsg.myx.monitor.model.ModelRoot;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.xarch.types.IArchStructure;

public class BootstrapComponent extends AbstractMyxSimpleBrick {

    public static final IMyxName INTERFACE_NAME_OUT_LAUNCHER = MyxMonitoringUtils.createName("launcher");
    public static final IMyxName INTERFACE_NAME_OUT_MODELROOT = MyxMonitoringUtils.createName("model-root");

    public static final String ARCHITECTURE_NAME = "main";

    protected Launcher launcher = null;
    protected ModelRoot modelRoot = null;

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        return null;
    }

    @Override
    public void begin() {
        launcher = (Launcher) MyxMonitoringUtils.getFirstRequiredServiceObject(this, INTERFACE_NAME_OUT_LAUNCHER);
        modelRoot = (ModelRoot) MyxMonitoringUtils.getFirstRequiredServiceObject(this, INTERFACE_NAME_OUT_MODELROOT);

        String structureDescription = MyxMonitoringUtils.getInitProperties(this).getProperty("structure");
        if (structureDescription == null) {
            structureDescription = "main";
        }

        IArchStructure archStructure = null;

        List<IArchStructure> archStructures = new ArrayList<>();
        for (IArchStructure as : DBLUtils.getArchStructures(modelRoot.getArchitectureRoot())) {
            archStructures.add(as);
            if (as.getDescription() != null && as.getDescription().getValue().equals(structureDescription)) {
                archStructure = as;
                break;
            }
        }
        if (archStructures.size() == 0) {
            throw new RuntimeException("Architecture has no structures to instantiate");
        }
        if (archStructure == null) {
            archStructure = archStructures.get(0);
        }

        // TODO instantiate via launcher
        try {
            launcher.instantiate(ARCHITECTURE_NAME, archStructure);
        } catch (ArchitectureInstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
