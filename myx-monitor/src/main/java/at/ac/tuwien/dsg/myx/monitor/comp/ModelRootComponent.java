package at.ac.tuwien.dsg.myx.monitor.comp;

import at.ac.tuwien.dsg.myx.MyxMonitoringUtils;
import at.ac.tuwien.dsg.myx.monitor.model.ModelRoot;
import at.ac.tuwien.dsg.myx.monitor.model.ModelRootImpl;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;

public class ModelRootComponent extends AbstractMyxSimpleBrick {

    public static final IMyxName INTERFACE_NAME_IN_MODELROOT = MyxMonitoringUtils.createName("model-root");
    
    protected String xadlFile;
    protected ModelRoot modelRoot;

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        if (interfaceName.equals(INTERFACE_NAME_IN_MODELROOT)) {
            return modelRoot;
        }
        return null;
    }
    
    @Override
    public void init() {
        xadlFile = MyxMonitoringUtils.getInitProperties(this).getProperty("file");
        if (xadlFile == null) {
            throw new IllegalArgumentException("File parameter missing for model-root component.");
        }
        
        modelRoot = new ModelRootImpl();
        modelRoot.parse(xadlFile);
    }
    
    @Override
    public void end() {
        String filename = xadlFile.substring(0, xadlFile.lastIndexOf('.')) + ".end.xml";
        modelRoot.save(filename);
    }
}
