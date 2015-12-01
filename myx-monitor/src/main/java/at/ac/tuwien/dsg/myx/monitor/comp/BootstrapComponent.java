package at.ac.tuwien.dsg.myx.monitor.comp;

import java.util.Collection;
import java.util.Iterator;

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
        launcher = MyxUtils.getFirstRequiredServiceObject(this, INTERFACE_NAME_OUT_LAUNCHER);
        modelRoot = MyxUtils.getFirstRequiredServiceObject(this, INTERFACE_NAME_OUT_MODELROOT);

        // structure to be instantiated
        IArchStructure archStructure = null;
        // extract arch structures
        Collection<IArchStructure> archStructures = DBLUtils.getArchStructures(modelRoot.getArchitectureRoot());

        // which archstructe should be instantiated?
        String structureDescription = MyxUtils.getInitProperties(this).getProperty(MyxProperties.STRUCTURE_NAME);
        if (structureDescription != null) {
            // extract a certain archstructure
            for (IArchStructure as : archStructures) {
                if (as.getDescription() != null && as.getDescription().getValue().equals(structureDescription)) {
                    archStructure = as;
                    break;
                }
            }
        } else {
            // use the first one if we got one
            Iterator<IArchStructure> it = archStructures.iterator();
            if (it.hasNext()) {
                archStructure = it.next();
            }
        }

        if (archStructure == null) {
            throw new RuntimeException("No suitable structure found!");
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
