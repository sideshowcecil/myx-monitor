package at.ac.tuwien.dsg.myx.monitor.comp;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.dsg.myx.monitor.MyxProperties;
import at.ac.tuwien.dsg.myx.monitor.aim.ArchitectureInstantiationException;
import at.ac.tuwien.dsg.myx.monitor.aim.Launcher;
import at.ac.tuwien.dsg.myx.monitor.model.ModelRoot;
import at.ac.tuwien.dsg.myx.util.DBLUtils;
import at.ac.tuwien.dsg.myx.util.MyxUtils;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.xarch.types.IArchStructure;

public class BootstrapComponent extends AbstractMyxSimpleBrick {

    public static final IMyxName INTERFACE_NAME_OUT_LAUNCHER = MyxUtils.createName("launcher");
    public static final IMyxName INTERFACE_NAME_OUT_MODELROOT = MyxUtils.createName("model-root");

    public static final String ARCHITECTURE_NAME = "main";

    protected Launcher launcher = null;
    protected ModelRoot modelRoot = null;

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        return null;
    }

    @Override
    public void begin() {
        launcher = MyxUtils.<Launcher> getFirstRequiredServiceObject(this, INTERFACE_NAME_OUT_LAUNCHER);
        modelRoot = MyxUtils.<ModelRoot> getFirstRequiredServiceObject(this, INTERFACE_NAME_OUT_MODELROOT);

        String structureDescription = MyxUtils.getInitProperties(this).getProperty(
                MyxProperties.STRUCTURE_NAME, MyxProperties.DEFAULT_STRUCTURE_NAME);

        IArchStructure archStructure = null;

        List<IArchStructure> archStructures = new ArrayList<>();
        for (IArchStructure as : DBLUtils.getArchStructures(modelRoot.getArchitectureRoot())) {
            archStructures.add(as);
            if (as.getDescription() != null && as.getDescription().getValue().equals(structureDescription)) {
                archStructure = as;
                break;
            }
        }
        if (archStructures.isEmpty()) {
            throw new RuntimeException("Architecture has no structures to instantiate!");
        }
        if (archStructure == null) {
            archStructure = archStructures.get(0);
        }

        try {
            launcher.instantiate(ARCHITECTURE_NAME, archStructure);
        } catch (ArchitectureInstantiationException e) {
            System.err.println("Architecture could not be instantiated! See error below:");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
