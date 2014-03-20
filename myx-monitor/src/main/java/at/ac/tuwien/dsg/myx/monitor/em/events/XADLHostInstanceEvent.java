package at.ac.tuwien.dsg.myx.monitor.em.events;

public class XADLHostInstanceEvent extends XADLHostEvent {

    private String description;

    public XADLHostInstanceEvent(String archInstanceId, String hostId) {
        super(archInstanceId, hostId);
    }

    public XADLHostInstanceEvent(XADLHostInstanceEvent copyFrom) {
        super(copyFrom);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
