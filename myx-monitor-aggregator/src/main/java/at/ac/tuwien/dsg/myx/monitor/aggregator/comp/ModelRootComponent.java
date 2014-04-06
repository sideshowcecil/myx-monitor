package at.ac.tuwien.dsg.myx.monitor.aggregator.comp;

import at.ac.tuwien.dsg.myx.monitor.MyxProperties;
import at.ac.tuwien.dsg.myx.monitor.aggregator.model.ModelRoot;
import at.ac.tuwien.dsg.myx.monitor.aggregator.model.ModelRootImpl;
import at.ac.tuwien.dsg.myx.monitor.aggregator.myx.MyxInterfaceNames;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;

public class ModelRootComponent extends AbstractMyxSimpleBrick {

    public static final IMyxName IN_MODEL_ROOT = MyxInterfaceNames.MODEL_ROOT;

    protected String xadlFile;
    protected ModelRoot modelRoot;

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        if (interfaceName.equals(IN_MODEL_ROOT)) {
            return modelRoot;
        }
        return null;
    }
    
    @Override
    public void init() {
        xadlFile = MyxMonitoringUtils.getInitProperties(this).getProperty(MyxProperties.XADL_FILE);
        if (xadlFile == null) {
            throw new IllegalArgumentException("File parameter missing for model-root component.");
        }
        
        modelRoot = new ModelRootImpl();
        modelRoot.parse(xadlFile);
    }

}
