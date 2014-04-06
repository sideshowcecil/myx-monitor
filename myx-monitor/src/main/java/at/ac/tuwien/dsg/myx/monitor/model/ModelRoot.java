package at.ac.tuwien.dsg.myx.monitor.model;

import edu.uci.isr.xarch.IXArch;

public interface ModelRoot {
    /**
     * Parse the given xadl file.
     * 
     * @param xadlFile
     */
    public void parse(String xadlFile);

    /**
     * Save the current {@link ModelRoot} to the given xadl file.
     * 
     * @param xadlFile
     */
    public void save(String xadlFile);

    /**
     * Get the root ({@link IXArch}) of the parsed xadl file.
     * 
     * @return
     */
    public IXArch getArchitectureRoot();
}
