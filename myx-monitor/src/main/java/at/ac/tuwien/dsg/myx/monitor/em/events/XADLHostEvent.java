package at.ac.tuwien.dsg.myx.monitor.em.events;

public abstract class XADLHostEvent extends Event {

    private final String hostId;

    public XADLHostEvent(String archInstanceId, String hostId) {
        super(archInstanceId);
        this.hostId = hostId;
    }

    public XADLHostEvent(XADLHostEvent copyFrom) {
        super(copyFrom);
        hostId = copyFrom.getHostId();
    }

    public String getHostId() {
        return hostId;
    }
}
