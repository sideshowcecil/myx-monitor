package at.ac.tuwien.dsg.myx.monitor;

import edu.uci.isr.myx.fw.IMyxRuntime;
import edu.uci.isr.myx.fw.MyxBasicImplementation;

public class MyxMonitoringImplementation extends MyxBasicImplementation {
    @Override
    public IMyxRuntime createRuntime() {
        return new MyxMonitoringRuntime();
    }
}
