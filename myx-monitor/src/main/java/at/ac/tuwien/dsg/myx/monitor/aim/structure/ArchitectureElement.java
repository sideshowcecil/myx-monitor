package at.ac.tuwien.dsg.myx.monitor.aim.structure;

public class ArchitectureElement {

    private final String blueprintId;
    private String description;

    public ArchitectureElement(String blueprintId) {
        this.blueprintId = blueprintId;
    }

    public String getBlueprintId() {
        return blueprintId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "[" + getBlueprintId() + "] " + getDescription();
    }
}
