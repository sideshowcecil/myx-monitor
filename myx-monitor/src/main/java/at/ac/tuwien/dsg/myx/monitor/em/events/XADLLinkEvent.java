package at.ac.tuwien.dsg.myx.monitor.em.events;

public class XADLLinkEvent extends Event {

    private static final long serialVersionUID = -8468106459116728579L;
    
    private final String xadlSourceRuntimeId;
    private final String xadlSourceInterfaceName;
    private final String xadlSourceInterfaceType;
    private final String xadlDestinationRuntimeId;
    private final String xadlDestinationInterfaceName;
    private final String xadlDestinationInterfaceType;
    private final XADLEventType xadlEventType;

    // TODO do we need a blueprint id here?

    public XADLLinkEvent(String xadlSourceRuntimeId, String xadlSourceInterfaceName, String xadlSourceInterfaceType,
            String xadlDestinationRuntimeId, String xadlDestinationElementInterfaceName,
            String xadlDestinationInterfaceType, XADLEventType xadlEventType) {
        super();
        this.xadlSourceRuntimeId = xadlSourceRuntimeId;
        this.xadlSourceInterfaceName = xadlSourceInterfaceName;
        this.xadlSourceInterfaceType = xadlSourceInterfaceType;
        this.xadlDestinationRuntimeId = xadlDestinationRuntimeId;
        this.xadlDestinationInterfaceName = xadlDestinationElementInterfaceName;
        this.xadlDestinationInterfaceType = xadlDestinationInterfaceType;
        this.xadlEventType = xadlEventType;
    }

    public XADLLinkEvent(XADLLinkEvent copyFrom) {
        super(copyFrom);
        xadlSourceRuntimeId = copyFrom.getXadlSourceRuntimeId();
        xadlSourceInterfaceName = copyFrom.getXadlSourceInterfaceName();
        xadlSourceInterfaceType = copyFrom.getXadlSourceInterfaceType();
        xadlDestinationRuntimeId = copyFrom.getXadlDestinationRuntimeId();
        xadlDestinationInterfaceName = copyFrom.getXadlDestinationInterfaceName();
        xadlDestinationInterfaceType = copyFrom.getXadlDestinationInterfaceType();
        xadlEventType = copyFrom.getXadlEventType();
    }

    public String getXadlSourceRuntimeId() {
        return xadlSourceRuntimeId;
    }

    public String getXadlSourceInterfaceName() {
        return xadlSourceInterfaceName;
    }

    public String getXadlSourceInterfaceType() {
        return xadlSourceInterfaceType;
    }

    public String getXadlDestinationRuntimeId() {
        return xadlDestinationRuntimeId;
    }

    public String getXadlDestinationInterfaceName() {
        return xadlDestinationInterfaceName;
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
                + ", xadlSourceInterfaceName=" + getXadlSourceInterfaceName() + ", xadlSourceInterfaceType="
                + getXadlSourceInterfaceType() + ", xadlDestinationRuntimeId=" + getXadlDestinationRuntimeId()
                + ", xadlDestinationInterfaceName=" + getXadlDestinationInterfaceName()
                + ", xadlDestinationInterfaceType=" + getXadlDestinationInterfaceType() + ", xadlEventType="
                + getXadlEventType() + "]";
    }

}
