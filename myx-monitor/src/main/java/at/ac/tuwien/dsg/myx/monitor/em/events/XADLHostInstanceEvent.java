package at.ac.tuwien.dsg.myx.monitor.em.events;

public class XADLHostInstanceEvent extends XADLHostEvent {

    private static final long serialVersionUID = -9082727013791419926L;
    
    private String description;

    public XADLHostInstanceEvent(XADLEventType xadlEventType) {
        super(xadlEventType);
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

    @Override
    public String toString() {
        return "XADLHostInstanceEvent [base=[" + super.toString() + "], description=" + getDescription() + "]";
    }

}
