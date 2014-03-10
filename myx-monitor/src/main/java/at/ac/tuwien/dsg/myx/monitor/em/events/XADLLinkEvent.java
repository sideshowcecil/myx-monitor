package at.ac.tuwien.dsg.myx.monitor.em.events;

public class XADLLinkEvent extends XADLEvent {

    private final String xadlElementInterfaceName;
    private final String xadlDestinationElementId;
    private final String xadlDestinationElementInterfaceName;

    public XADLLinkEvent(String archInstanceId, String xadlElementId, String xadlElementInterfaceName,
            String xadlDestinationElementId, String xadlDestinationElementInterfaceName, XADLEventType xadlEventType) {
        super(archInstanceId, xadlElementId, xadlEventType);
        this.xadlElementInterfaceName = xadlElementInterfaceName;
        this.xadlDestinationElementId = xadlDestinationElementId;
        this.xadlDestinationElementInterfaceName = xadlDestinationElementInterfaceName;
        setXadlElementType(XADLElementType.LINK);
    }

    public XADLLinkEvent(XADLLinkEvent copyFrom) {
        super(copyFrom);
        xadlElementInterfaceName = copyFrom.getXadlElementInterfaceName();
        xadlDestinationElementId = copyFrom.getXadlDestinationElementId();
        xadlDestinationElementInterfaceName = copyFrom.getXadlDestinationElementInterfaceName();
        setXadlElementType(XADLElementType.LINK);
    }

    @Override
    public String getXadlElementId() {
        // force the element type to be link
        if (super.getXadlElementType() != XADLElementType.LINK) {
            setXadlElementType(XADLElementType.LINK);
        }
        return super.getXadlElementId();
    }

    public String getXadlElementInterfaceName() {
        return xadlElementInterfaceName;
    }

    public String getXadlDestinationElementId() {
        return xadlDestinationElementId;
    }

    public String getXadlDestinationElementInterfaceName() {
        return xadlDestinationElementInterfaceName;
    }

    @Override
    public String toString() {
        return "XADLLinkEvent [base=[" + super.toString() + "], xadlElementInterfaceName="
                + getXadlElementInterfaceName() + ", xadlDestinationElementId=" + getXadlDestinationElementId()
                + ", xadlDestinationElementInterfaceName=" + getXadlDestinationElementInterfaceName() + "]";
    }

}
