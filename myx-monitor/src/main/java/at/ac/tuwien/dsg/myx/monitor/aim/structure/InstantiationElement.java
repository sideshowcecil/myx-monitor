package at.ac.tuwien.dsg.myx.monitor.aim.structure;

import at.ac.tuwien.dsg.myx.monitor.aim.structure.type.ArchitectureType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class InstantiationElement extends ArchitectureElement {

    private final String runtimeId;

    private final List<Interface> interfaces = new ArrayList<>();

    private final List<Implementation> implementations = new ArrayList<>();
    private ArchitectureType type;

    private final List<Link> initLinks = new ArrayList<>();
    private final List<Link> beginLinks = new ArrayList<>();

    public InstantiationElement(String blueprintId, String runtimeId) {
        super(blueprintId);
        this.runtimeId = runtimeId;
    }

    public String getRuntimeId() {
        return runtimeId;
    }

    public List<Interface> getInterfaces() {
        return interfaces;
    }

    public List<Implementation> getImplementations() {
        return implementations;
    }

    public ArchitectureType getType() {
        return type;
    }

    public void setType(ArchitectureType type) {
        this.type = type;
    }

    public List<Link> getInitLinks() {
        return initLinks;
    }

    public List<Link> getBeginLinks() {
        return beginLinks;
    }

    /**
     * Get a specific interface.
     * 
     * @param id
     * @return
     */
    public Interface getInterface(String id) {
        for (Interface intf : getInterfaces()) {
            if (Objects.equals(intf.getBlueprintId(), id)) {
                return intf;
            }
        }
        return null;
    }

    /**
     * Get the java main class name of the object. The class may be specified
     * directly or by a type.
     * 
     * @return
     */
    public String getImplementationMainClassName() {
        if (!getImplementations().isEmpty()) {
            return getImplementations().get(0).getMainClassName();
        }
        if (getType() != null && !getType().getImplementations().isEmpty()) {
            return getType().getImplementations().get(0).getMainClassName();
        }
        return null;
    }

    /**
     * Get the java init properties of the object. The properties may be
     * specified directly or by a type.
     * 
     * @return
     */
    public Properties getImplemenationInitProperties() {
        if (!getImplementations().isEmpty()) {
            return getImplementations().get(0).getInitProperties();
        }
        if (type != null && !type.getImplementations().isEmpty()) {
            return type.getImplementations().get(0).getInitProperties();
        }
        return null;
    }

    /**
     * Return if the object contains an implementation.
     * 
     * @return
     */
    public boolean hasImplementation() {
        return getImplementationMainClassName() != null;
    }

    /**
     * Return the subarchitecture of the object.
     * 
     * @return
     */
    public SubArchitecture getSubarchitecture() {
        if (getType() != null && getType().getSubArchitecture() != null) {
            return getType().getSubArchitecture();
        }
        return null;
    }

    /**
     * Return if this object contains a subarchitecture.
     * 
     * @return
     */
    public boolean hasSubArchitecture() {
        return getSubarchitecture() != null;
    }
}
