package at.ac.tuwien.dsg.myx.monitor.em.events;

public class XADLLinkEvent extends Event {

    private final String xadlSourceRuntimeId;
    private final String xadlSourceInterfaceName;
    private final String xadlDestinationRuntimeId;
    private final String xadlDestinationInterfaceName;
    private final XADLEventType xadlEventType;

    public XADLLinkEvent(String archInstanceId, String xadlSourceRuntimeId, String xadlSourceInterfaceName,
            String xadlDestinationRuntimeId, String xadlDestinationElementInterfaceName, XADLEventType xadlEventType) {
        super(archInstanceId);
        this.xadlSourceRuntimeId = xadlSourceRuntimeId;
        this.xadlSourceInterfaceName = xadlSourceInterfaceName;
        this.xadlDestinationRuntimeId = xadlDestinationRuntimeId;
        this.xadlDestinationInterfaceName = xadlDestinationElementInterfaceName;
        this.xadlEventType = xadlEventType;
    }

    public XADLLinkEvent(XADLLinkEvent copyFrom) {
        super(copyFrom);
        xadlSourceRuntimeId = copyFrom.getXadlSourceRuntimeId();
        xadlSourceInterfaceName = copyFrom.getXadlSourceInterfaceName();
        xadlDestinationRuntimeId = copyFrom.getXadlDestinationRuntimeId();
        xadlDestinationInterfaceName = copyFrom.getXadlDestinationInterfaceName();
        xadlEventType = copyFrom.getXadlEventType();
    }

    public String getXadlSourceRuntimeId() {
        return xadlSourceRuntimeId;
    }

    public String getXadlSourceInterfaceName() {
        return xadlSourceInterfaceName;
    }

    public String getXadlDestinationRuntimeId() {
        return xadlDestinationRuntimeId;
    }

    public String getXadlDestinationInterfaceName() {
        return xadlDestinationInterfaceName;
    }

    public XADLEventType getXadlEventType() {
        return xadlEventType;
    }

    @Override
    public String toString() {
        return "XADLLinkEvent [base=[" + super.toString() + "], xadlSourceRuntimeId=" + getXadlSourceRuntimeId()
                + ", xadlSourceInterfaceName=" + getXadlSourceInterfaceName() + ", xadlDestinationRuntimeId="
                + getXadlDestinationRuntimeId() + ", xadlDestinationInterfaceName=" + getXadlDestinationInterfaceName()
                + "]";
    }

}
