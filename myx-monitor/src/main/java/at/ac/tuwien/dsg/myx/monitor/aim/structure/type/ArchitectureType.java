package at.ac.tuwien.dsg.myx.monitor.aim.structure.type;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.dsg.myx.monitor.aim.structure.ArchitectureElement;
import at.ac.tuwien.dsg.myx.monitor.aim.structure.Implementation;
import at.ac.tuwien.dsg.myx.monitor.aim.structure.Signature;
import at.ac.tuwien.dsg.myx.monitor.aim.structure.SubArchitecture;

public class ArchitectureType extends ArchitectureElement {

    private final List<Implementation> implementations = new ArrayList<>();
    private final List<Signature> signatures = new ArrayList<>();
    private SubArchitecture subArchitecture;

    public ArchitectureType(String blueprintId) {
        super(blueprintId);
    }

    public List<Implementation> getImplementations() {
        return implementations;
    }

    public List<Signature> getSignatures() {
        return signatures;
    }

    public SubArchitecture getSubArchitecture() {
        return subArchitecture;
    }

    public void setSubArchitecture(SubArchitecture subArchitecture) {
        this.subArchitecture = subArchitecture;
    }

}
