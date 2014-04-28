package at.ac.tuwien.dsg.myx.monitor.em.events;

public class XADLExternalLinkEvent extends Event {

    private static final long serialVersionUID = -1040692031820313185L;

    private final String xadlRuntimeId;
    private final String xadlBlueprintId;
    private final String xadlInterfaceType;
    private final String xadlExternalConnectionIdentifier;
    private final XADLEventType xadlEventType;

    public XADLExternalLinkEvent(String xadlRuntimeId, String xadlBlueprintId, String xadlInterfaceType,
            String xadlExternalConnectionIdentifier, XADLEventType xadlEventType) {
        super();
        this.xadlRuntimeId = xadlRuntimeId;
        this.xadlBlueprintId = xadlBlueprintId;
        this.xadlInterfaceType = xadlInterfaceType;
        this.xadlExternalConnectionIdentifier = xadlExternalConnectionIdentifier;
        this.xadlEventType = xadlEventType;
    }

    public XADLExternalLinkEvent(XADLExternalLinkEvent copyFrom) {
        super(copyFrom);
        xadlRuntimeId = copyFrom.getXadlRuntimeId();
        xadlBlueprintId = copyFrom.getXadlBlueprintId();
        xadlInterfaceType = copyFrom.getXadlInterfaceType();
        xadlExternalConnectionIdentifier = copyFrom.getXadlExternalConnectionIdentifier();
        xadlEventType = copyFrom.getXadlEventType();
    }

    public String getXadlRuntimeId() {
        return xadlRuntimeId;
    }

    public String getXadlBlueprintId() {
        return xadlBlueprintId;
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
                + ", xadlBlueprintId=" + getXadlBlueprintId() + ", xadlInterfaceType=" + getXadlInterfaceType()
                + ", xadlExternalConnectionIdentifier=" + getXadlExternalConnectionIdentifier() + ", xadlEventType="
                + getXadlEventType() + "]";
    }

}
