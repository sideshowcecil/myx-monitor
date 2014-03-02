package at.ac.tuwien.dsg.myx.monitor.aim;

import edu.uci.isr.xarch.types.IArchStructure;

public interface Launcher {
    public void instantiate(String name, IArchStructure structure) throws ArchitectureInstantiationException;
}
