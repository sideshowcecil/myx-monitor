package at.ac.tuwien.dsg.myx.monitor.model;

import edu.uci.isr.xarch.IXArch;

public interface ModelRoot {
    /**
     * Parse the given xadl file.
     * 
     * @param xadlFile
     */
    void parse(String xadlFile);

    /**
     * Get the root ({@link IXArch}) of the parsed xadl file.
     * 
     * @return
     */
    IXArch getArchitectureRoot();
}
