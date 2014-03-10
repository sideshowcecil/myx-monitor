package at.ac.tuwien.dsg.myx.monitor;

import at.ac.tuwien.dsg.myx.monitor.event.EventManager;
import edu.uci.isr.myx.fw.IMyxBrickDescription;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.IMyxWeld;
import edu.uci.isr.myx.fw.MyxBasicRuntime;
import edu.uci.isr.myx.fw.MyxBrickCreationException;
import edu.uci.isr.myx.fw.MyxBrickLoadException;

public class MyxMonitoringRuntime extends MyxBasicRuntime {
    
    protected EventManager eventManager;
    
    public MyxMonitoringRuntime(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public void addBrick(IMyxName[] path, IMyxName brickName, IMyxBrickDescription brickDescription)
            throws MyxBrickLoadException, MyxBrickCreationException {
        // TODO Auto-generated method stub
        super.addBrick(path, brickName, brickDescription);
    }
    
    @Override
    public void addWeld(IMyxWeld weld) {
        // TODO Auto-generated method stub
        super.addWeld(weld);
    }
    
    @Override
    public void begin(IMyxName[] path, IMyxName brickName) {
        // TODO Auto-generated method stub
        super.begin(path, brickName);
    }
    
}
