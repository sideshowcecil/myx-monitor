package at.ac.tuwien.dsg.myx.monitor.aim.structure;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.dsg.myx.monitor.aim.structure.type.ArchitectureType;
import edu.uci.isr.myx.fw.EMyxInterfaceDirection;

public class Signature extends ArchitectureElement {

    private EMyxInterfaceDirection direction;
    private String serviceType;
    private ArchitectureType type;
    private final List<String> lookupImplementations = new ArrayList<>();

    public Signature(String id) {
        super(id);
    }

    public EMyxInterfaceDirection getDirection() {
        return direction;
    }

    public void setDirection(EMyxInterfaceDirection direction) {
        this.direction = direction;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public ArchitectureType getType() {
        return type;
    }

    public void setType(ArchitectureType type) {
        this.type = type;
    }

    public List<String> getLookupImplementations() {
        return lookupImplementations;
    }
}
