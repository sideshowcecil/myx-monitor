package at.ac.tuwien.dsg.myx.monitor.em.events;

public class XADLEvent extends Event {

    private final String xadlElementId;
    private final XADLEventType xadlEventType;

    private XADLElementType xadlElementType;

    public XADLEvent(String archInstanceId, String xadlElementId, XADLEventType xadlEventType) {
        super(archInstanceId);
        this.xadlElementId = xadlElementId;
        this.xadlEventType = xadlEventType;
    }

    public XADLEvent(XADLEvent copyFrom) {
        super(copyFrom);
        xadlEventType = copyFrom.getXadlEventType();
        xadlElementId = copyFrom.getXadlElementId();
        xadlElementType = copyFrom.getXadlElementType();
    }

    public String getXadlElementId() {
        return xadlElementId;
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
        return "XADLEvent [base=[" + super.toString() + "], xadlEventType=" + getXadlEventType() + ", xadlElementId="
                + getXadlElementId() + ", xadlElementType=" + getXadlElementType() + "]";
    }

}
