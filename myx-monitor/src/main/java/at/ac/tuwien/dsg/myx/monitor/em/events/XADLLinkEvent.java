package at.ac.tuwien.dsg.myx.monitor.em.events;

public class XADLLinkEvent extends Event {

    private static final long serialVersionUID = -8468106459116728579L;

    private final String xadlSourceRuntimeId;
    private final String xadlSourceInterfaceType;
    private final String xadlDestinationRuntimeId;
    private final String xadlDestinationInterfaceType;
    private final XADLEventType xadlEventType;

    public XADLLinkEvent(String xadlSourceRuntimeId, String xadlSourceInterfaceType, String xadlDestinationRuntimeId,
            String xadlDestinationInterfaceType, XADLEventType xadlEventType) {
        super();
        this.xadlSourceRuntimeId = xadlSourceRuntimeId;
        this.xadlSourceInterfaceType = xadlSourceInterfaceType;
        this.xadlDestinationRuntimeId = xadlDestinationRuntimeId;
        this.xadlDestinationInterfaceType = xadlDestinationInterfaceType;
        this.xadlEventType = xadlEventType;
    }

    public XADLLinkEvent(XADLLinkEvent copyFrom) {
        super(copyFrom);
        xadlSourceRuntimeId = copyFrom.getXadlSourceRuntimeId();
        xadlSourceInterfaceType = copyFrom.getXadlSourceInterfaceType();
        xadlDestinationRuntimeId = copyFrom.getXadlDestinationRuntimeId();
        xadlDestinationInterfaceType = copyFrom.getXadlDestinationInterfaceType();
        xadlEventType = copyFrom.getXadlEventType();
    }

    public String getXadlSourceRuntimeId() {
        return xadlSourceRuntimeId;
    }

    public String getXadlSourceInterfaceType() {
        return xadlSourceInterfaceType;
    }

    public String getXadlDestinationRuntimeId() {
        return xadlDestinationRuntimeId;
    }

    public String getXadlDestinationInterfaceType() {
        return xadlDestinationInterfaceType;
    }

    public XADLEventType getXadlEventType() {
        return xadlEventType;
    }

    @Override
    public String toString() {
        return "XADLLinkEvent [base=[" + super.toString() + "], xadlSourceRuntimeId=" + getXadlSourceRuntimeId()
                + ", xadlSourceInterfaceType=" + getXadlSourceInterfaceType() + ", xadlDestinationRuntimeId="
                + getXadlDestinationRuntimeId() + ", xadlDestinationInterfaceType=" + getXadlDestinationInterfaceType()
                + ", xadlEventType=" + getXadlEventType() + "]";
    }

}
