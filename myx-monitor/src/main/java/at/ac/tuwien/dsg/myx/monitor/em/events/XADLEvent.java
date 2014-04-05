package at.ac.tuwien.dsg.myx.monitor.em.events;

public class XADLEvent extends Event {

    private static final long serialVersionUID = -3017572596996481L;

    private final String xadlRuntimeId;
    private final String xadlBlueprintId;
    private final XADLEventType xadlEventType;

    private XADLElementType xadlElementType;

    public XADLEvent(String xadlRuntimeId, String xadlElementId, XADLEventType xadlEventType) {
        super();
        this.xadlRuntimeId = xadlRuntimeId;
        this.xadlBlueprintId = xadlElementId;
        this.xadlEventType = xadlEventType;
    }

    public XADLEvent(XADLEvent copyFrom) {
        super(copyFrom);
        xadlRuntimeId = copyFrom.getXadlRuntimeId();
        xadlBlueprintId = copyFrom.getXadlBlueprintId();
        xadlElementType = copyFrom.getXadlElementType();
        xadlEventType = copyFrom.getXadlEventType();
    }

    public String getXadlRuntimeId() {
        return xadlRuntimeId;
    }

    public String getXadlBlueprintId() {
        return xadlBlueprintId;
    }

    public XADLEventType getXadlEventType() {
        return xadlEventType;
    }

    public XADLElementType getXadlElementType() {
        return xadlElementType;
    }

    public void setXadlElementType(XADLElementType xadlType) {
        this.xadlElementType = xadlType;
    }

    @Override
    public String toString() {
        return "XADLEvent [base=[" + super.toString() + "], xadlRuntimeId=" + getXadlRuntimeId() + ", xadlBlueprintId="
                + getXadlBlueprintId() + ", xadlElementType=" + getXadlElementType() + ", xadlEventType="
                + getXadlEventType() + "]";
    }

}
