package at.ac.tuwien.dsg.myx.monitor.em.events;

public abstract class XADLHostEvent extends Event {

    private final String hostId;
    private final XADLEventType xadlEventType;

    public XADLHostEvent(String architectureRuntimeId, String hostId, XADLEventType xadlEventType) {
        super(architectureRuntimeId);
        this.hostId = hostId;
        this.xadlEventType = xadlEventType;
    }

    public XADLHostEvent(XADLHostEvent copyFrom) {
        super(copyFrom);
        hostId = copyFrom.getHostId();
        xadlEventType = copyFrom.getXadlEventType();
    }

    public String getHostId() {
        return hostId;
    }

    public XADLEventType getXadlEventType() {
        return xadlEventType;
    }

    @Override
    public String toString() {
        return "XADLHostEvent [base=[" + super.toString() + "], hostId=" + getHostId() + ", xadlEventType="
                + getXadlEventType() + "]";
    }
}
