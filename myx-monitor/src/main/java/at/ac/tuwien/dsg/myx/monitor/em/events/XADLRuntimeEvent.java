package at.ac.tuwien.dsg.myx.monitor.em.events;

public class XADLRuntimeEvent extends Event {

    private final String xadlElementId;
    private final XADLRuntimeEventType xadlRuntimeType;

    public XADLRuntimeEvent(String archInstanceId, String xadlElementId, XADLRuntimeEventType xadlRuntimeType) {
        super(archInstanceId);
        this.xadlElementId = xadlElementId;
        this.xadlRuntimeType = xadlRuntimeType;
    }

    public XADLRuntimeEvent(XADLRuntimeEvent copyFrom) {
        super(copyFrom);
        xadlElementId = copyFrom.getXadlElementId();
        xadlRuntimeType = copyFrom.getXadlRuntimeType();
    }

    public String getXadlElementId() {
        return xadlElementId;
    }

    public XADLRuntimeEventType getXadlRuntimeType() {
        return xadlRuntimeType;
    }

    @Override
    public String toString() {
        return "XADLRuntimeEvent [base=[" + super.toString() + "], xadlRuntimeType=" + getXadlRuntimeType() + "]";
    }

}
