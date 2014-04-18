package at.ac.tuwien.dsg.myx.monitor.aggregator.events;

import java.util.HashMap;
import java.util.Map;

import at.ac.tuwien.dsg.myx.monitor.em.events.XADLElementType;

public class ModelElementResponseEvent extends ModelResponseEvent {

    private static final long serialVersionUID = 8580723642229018701L;

    private final XADLElementType elementType;
    private String description;
    private final Map<String, String> interfaces;

    public ModelElementResponseEvent(String runtimeId, XADLElementType elementType) {
        super(runtimeId);
        this.elementType = elementType;
        this.interfaces = new HashMap<>();
    }

    public ModelElementResponseEvent(ModelElementResponseEvent copyFrom) {
        super(copyFrom);
        elementType = copyFrom.getElementType();
        description = copyFrom.getDescription();
        interfaces = copyFrom.getInterfaces();
    }

    public XADLElementType getElementType() {
        return elementType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getInterfaces() {
        return interfaces;
    }

    @Override
    public String toString() {
        return "ModelElementResponseEvent [base=[" + super.toString() + "], elementType=" + getElementType()
                + ", description=" + getDescription() + ", interfaces=" + getInterfaces() + "]";
    }

}
