package at.ac.tuwien.dsg.myx.monitor.em.events;

public class XADLExternalLinkEvent extends Event {

    private static final long serialVersionUID = -1040692031820313185L;
    
    private final String xadlRuntimeId;
    private final String xadlInterfaceName;
    private final String xadlInterfaceType;
    private final String xadlExternalConnectionIdentifier;
    private final XADLEventType xadlEventType;

    // TODO do we need a blueprint id here?

    public XADLExternalLinkEvent(String xadlRuntimeId, String xadlInterfaceName,
            String xadlInterfaceType, String xadlExternalConnectionIdentifier, XADLEventType xadlEventType) {
        super();
        this.xadlRuntimeId = xadlRuntimeId;
        this.xadlInterfaceName = xadlInterfaceName;
        this.xadlInterfaceType = xadlInterfaceType;
        this.xadlExternalConnectionIdentifier = xadlExternalConnectionIdentifier;
        this.xadlEventType = xadlEventType;
    }

    public XADLExternalLinkEvent(XADLExternalLinkEvent copyFrom) {
        super(copyFrom);
        xadlRuntimeId = copyFrom.getXadlRuntimeId();
        xadlInterfaceName = copyFrom.getXadlInterfaceName();
        xadlInterfaceType = copyFrom.getXadlInterfaceType();
        xadlExternalConnectionIdentifier = copyFrom.getXadlExternalConnectionIdentifier();
        xadlEventType = copyFrom.getXadlEventType();
    }

    public String getXadlRuntimeId() {
        return xadlRuntimeId;
    }

    public String getXadlInterfaceName() {
        return xadlInterfaceName;
    }

    public String getXadlInterfaceType() {
        return xadlInterfaceType;
    }

    public String getXadlExternalConnectionIdentifier() {
        return xadlExternalConnectionIdentifier;
    }

    public XADLEventType getXadlEventType() {
        return xadlEventType;
    }

    @Override
    public String toString() {
        return "XADLExternalLinkEvent [base=[" + super.toString() + "], xadlRuntimeId=" + getXadlRuntimeId()
                + ", xadlInterfaceName=" + getXadlInterfaceName() + ", xadlInterfaceType=" + getXadlInterfaceType()
                + ", xadlExternalConnectionIdentifier=" + getXadlExternalConnectionIdentifier() + ", xadlEventType="
                + getXadlEventType() + "]";
    }

}
