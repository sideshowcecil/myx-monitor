package at.ac.tuwien.dsg.myx.monitor.aim.structure;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.dsg.myx.util.Tuple;
import edu.uci.isr.xarch.types.IArchStructure;

public class SubArchitecture extends ArchitectureElement {

    private final IArchStructure archStructure;
    private final Map<Signature, Tuple<String, Interface>> interfaceMapping = new HashMap<>();

    public SubArchitecture(String id, IArchStructure archStructure) {
        super(id);
        this.archStructure = archStructure;
    }

    public IArchStructure getArchStructure() {
        return archStructure;
    }

    public Map<Signature, Tuple<String, Interface>> getInterfaceMapping() {
        return interfaceMapping;
    }
}
