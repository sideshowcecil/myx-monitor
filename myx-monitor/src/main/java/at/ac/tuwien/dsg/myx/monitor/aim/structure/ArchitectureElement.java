package at.ac.tuwien.dsg.myx.monitor.aim.structure;

public class ArchitectureElement {

    private final String id;
    private String description;
    
    public ArchitectureElement(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "[" + getId() + "] " + getDescription();
    }
}
