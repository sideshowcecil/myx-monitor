package at.ac.tuwien.dsg.myx.monitor.em.events;

public class XADLLinkEvent extends Event {

    private static final long serialVersionUID = -8468106459116728579L;

    private final String xadlSourceRuntimeId;
    private final String xadlSourceBlueprintId;
    private final String xadlSourceInterfaceType;
    private final String xadlDestinationRuntimeId;
    private final String xadlDestinationBlueprintId;
    private final String xadlDestinationInterfaceType;
    private final XADLEventType xadlEventType;

    public XADLLinkEvent(String xadlSourceRuntimeId, String xadlSourceBlueprintId, String xadlSourceInterfaceType,
            String xadlDestinationRuntimeId, String xadlDestinationBlueprintId, String xadlDestinationInterfaceType,
            XADLEventType xadlEventType) {
        super();
        this.xadlSourceRuntimeId = xadlSourceRuntimeId;
        this.xadlSourceBlueprintId = xadlSourceBlueprintId;
        this.xadlSourceInterfaceType = xadlSourceInterfaceType;
        this.xadlDestinationRuntimeId = xadlDestinationRuntimeId;
        this.xadlDestinationBlueprintId = xadlDestinationBlueprintId;
        this.xadlDestinationInterfaceType = xadlDestinationInterfaceType;
        this.xadlEventType = xadlEventType;
    }

    public XADLLinkEvent(XADLLinkEvent copyFrom) {
        super(copyFrom);
        xadlSourceRuntimeId = copyFrom.getXadlSourceRuntimeId();
        xadlSourceBlueprintId = copyFrom.getXadlSourceBlueprintId();
        xadlSourceInterfaceType = copyFrom.getXadlSourceInterfaceType();
        xadlDestinationRuntimeId = copyFrom.getXadlDestinationRuntimeId();
        xadlDestinationBlueprintId = copyFrom.getXadlDestinationBlueprintId();
        xadlDestinationInterfaceType = copyFrom.getXadlDestinationInterfaceType();
        xadlEventType = copyFrom.getXadlEventType();
    }

    public String getXadlSourceRuntimeId() {
        return xadlSourceRuntimeId;
    }

    public String getXadlSourceBlueprintId() {
        return xadlSourceBlueprintId;
    }

    public String getXadlSourceInterfaceType() {
        return xadlSourceInterfaceType;
    }

    public String getXadlDestinationRuntimeId() {
        return xadlDestinationRuntimeId;
    }

    public String getXadlDestinationBlueprintId() {
        return xadlDestinationBlueprintId;
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
                + ", xadlSourceBlueprintId=" + getXadlSourceBlueprintId() + ", xadlSourceInterfaceType="
                + getXadlSourceInterfaceType() + ", xadlDestinationRuntimeId=" + getXadlDestinationRuntimeId()
                + ", xadlDestinationBlueprintId=" + getXadlDestinationBlueprintId() + ", xadlDestinationInterfaceType="
                + getXadlDestinationInterfaceType() + ", xadlEventType=" + getXadlEventType() + "]";
    }

}
