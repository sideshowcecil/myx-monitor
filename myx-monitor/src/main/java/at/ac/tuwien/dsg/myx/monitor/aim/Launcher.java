package at.ac.tuwien.dsg.myx.monitor.aim;

import edu.uci.isr.xarch.types.IArchStructure;

public interface Launcher {
    /**
     * Instantiate the given {@link IArchStructure} and run it.
     * 
     * @param name
     * @param structure
     * @throws ArchitectureInstantiationException
     */
    public void instantiate(String name, IArchStructure structure) throws ArchitectureInstantiationException;
}
