package at.ac.tuwien.dsg.myx.monitor.em.events;

public class XADLExternalLinkEvent extends Event {

    private static final long serialVersionUID = -1040692031820313185L;

    private final String xadlRuntimeId;
    private final String xadlInterfaceType;
    private final String xadlExternalConnectionIdentifier;
    private final XADLEventType xadlEventType;

    public XADLExternalLinkEvent(String xadlRuntimeId, String xadlInterfaceType,
            String xadlExternalConnectionIdentifier, XADLEventType xadlEventType) {
        super();
        this.xadlRuntimeId = xadlRuntimeId;
        this.xadlInterfaceType = xadlInterfaceType;
        this.xadlExternalConnectionIdentifier = xadlExternalConnectionIdentifier;
        this.xadlEventType = xadlEventType;
    }

    public XADLExternalLinkEvent(XADLExternalLinkEvent copyFrom) {
        super(copyFrom);
        xadlRuntimeId = copyFrom.getXadlRuntimeId();
        xadlInterfaceType = copyFrom.getXadlInterfaceType();
        xadlExternalConnectionIdentifier = copyFrom.getXadlExternalConnectionIdentifier();
        xadlEventType = copyFrom.getXadlEventType();
    }

    public String getXadlRuntimeId() {
        return xadlRuntimeId;
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
                + ", xadlInterfaceType=" + getXadlInterfaceType() + ", xadlExternalConnectionIdentifier="
                + getXadlExternalConnectionIdentifier() + ", xadlEventType=" + getXadlEventType() + "]";
    }

}
