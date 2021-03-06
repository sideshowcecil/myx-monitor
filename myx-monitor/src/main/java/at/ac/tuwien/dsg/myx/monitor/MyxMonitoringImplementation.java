package at.ac.tuwien.dsg.myx.monitor;

import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import edu.uci.isr.myx.fw.IMyxRuntime;
import edu.uci.isr.myx.fw.MyxBasicImplementation;

public class MyxMonitoringImplementation extends MyxBasicImplementation {
    
    protected IMyxRuntime runtime;
    
    public MyxMonitoringImplementation(EventManager eventManager) {
        runtime = new MyxMonitoringRuntime(eventManager);
    }
    
    @Override
    public IMyxRuntime createRuntime() {
        return runtime;
    }
}
