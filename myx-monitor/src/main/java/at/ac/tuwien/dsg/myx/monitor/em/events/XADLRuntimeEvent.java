package at.ac.tuwien.dsg.myx.monitor.em.events;

public class XADLRuntimeEvent extends Event {

    private static final long serialVersionUID = -1471857307231781963L;

    private final String xadlRuntimeId;
    private final String xadlBlueprintId;
    private final XADLRuntimeEventType xadlRuntimeType;

    public XADLRuntimeEvent(String xadlRuntimeId, String xadlBlueprintId, XADLRuntimeEventType xadlRuntimeType) {
        super();
        this.xadlRuntimeId = xadlRuntimeId;
        this.xadlBlueprintId = xadlBlueprintId;
        this.xadlRuntimeType = xadlRuntimeType;
    }

    public XADLRuntimeEvent(XADLRuntimeEvent copyFrom) {
        super(copyFrom);
        xadlRuntimeId = copyFrom.getXadlRuntimeId();
        xadlBlueprintId = copyFrom.getXadlBlueprintId();
        xadlRuntimeType = copyFrom.getXadlRuntimeType();
    }

    public String getXadlRuntimeId() {
        return xadlRuntimeId;
    }

    public String getXadlBlueprintId() {
        return xadlBlueprintId;
    }

    public XADLRuntimeEventType getXadlRuntimeType() {
        return xadlRuntimeType;
    }

    @Override
    public String toString() {
        return "XADLRuntimeEvent [base=[" + super.toString() + "], xadlRuntimeId=" + getXadlRuntimeId()
                + ", xadlBlueprintId=" + getXadlBlueprintId() + ", xadlRuntimeType=" + getXadlRuntimeType() + "]";
    }

}
