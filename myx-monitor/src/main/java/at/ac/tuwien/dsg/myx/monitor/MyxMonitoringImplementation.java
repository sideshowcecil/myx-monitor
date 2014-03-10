package at.ac.tuwien.dsg.myx.monitor;

import at.ac.tuwien.dsg.myx.monitor.event.EventManager;
import edu.uci.isr.myx.fw.IMyxRuntime;
import edu.uci.isr.myx.fw.MyxBasicImplementation;

public class MyxMonitoringImplementation extends MyxBasicImplementation {
    
    protected EventManager eventManager;
    
    public MyxMonitoringImplementation(EventManager eventManager) {
        this.eventManager = eventManager;
    }
    
    @Override
    public IMyxRuntime createRuntime() {
        return new MyxMonitoringRuntime(eventManager);
    }
}
