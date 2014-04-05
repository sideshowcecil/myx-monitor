package at.ac.tuwien.dsg.myx.monitor.aim.structure;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.dsg.myx.monitor.aim.structure.type.InterfaceType;
import edu.uci.isr.myx.fw.EMyxInterfaceDirection;

public class Interface extends ArchitectureElement {

    private final EMyxInterfaceDirection direction;
    private InterfaceType type;
    private Signature signature;

    public Interface(String id, EMyxInterfaceDirection direction) {
        super(id);
        this.direction = direction;
    }

    public EMyxInterfaceDirection getDirection() {
        return direction;
    }

    public InterfaceType getType() {
        return type;
    }

    public void setType(InterfaceType type) {
        this.type = type;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    /**
     * Get the implementation main class names specified by the type. This
     * method provides simplified access to these.
     * 
     * @return
     */
    public List<String> getImplementationMainClassNames() {
        List<String> classNames = new ArrayList<>();
        if (getType() != null) {
            for (Implementation i : getType().getImplementations()) {
                if (i.getMainClassName() != null) {
                    classNames.add(i.getMainClassName());
                }
                classNames.addAll(i.getAuxClassNames());
            }
        }
        return classNames;
    }

    /**
     * Get the lookup implementation names specified by the signature. This
     * method provides simplified access to these.
     * 
     * @return
     */
    public List<String> getLookupImplementationNames() {
        List<String> lookupNames = new ArrayList<>();
        if (getSignature() != null) {
            lookupNames.addAll(getSignature().getLookupImplementations());
        }
        return lookupNames;
    }

    /**
     * Get the name of the interface.
     * 
     * @return
     */
    public String getName() {
        if (!getLookupImplementationNames().isEmpty()) {
            return getLookupImplementationNames().get(0);
        }
        if (!getImplementationMainClassNames().isEmpty()) {
            return getImplementationMainClassNames().get(0);
        }
        if (getDirection() != null) {
            return getDirection().name();
        }
        return null;
    }

}
