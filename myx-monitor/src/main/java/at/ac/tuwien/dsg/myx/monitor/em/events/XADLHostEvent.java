package at.ac.tuwien.dsg.myx.monitor.em.events;

public abstract class XADLHostEvent extends Event {

    private final XADLEventType xadlEventType;
    
    private String hostId;

    public XADLHostEvent(XADLEventType xadlEventType) {
        super();
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
    
    public void setHostId(String hostId) {
        this.hostId = hostId;
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
