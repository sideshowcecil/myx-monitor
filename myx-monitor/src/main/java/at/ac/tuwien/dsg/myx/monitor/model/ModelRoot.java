package at.ac.tuwien.dsg.myx.monitor.model;

import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchContext;

public interface ModelRoot {
    public void parse(String xadlFile);
    
    public void save(String xadlFile);
    
    public IXArch getArchitectureRoot();

    public IXArchContext createArchitectureContext(String ctxTypeIdentifier);
}
