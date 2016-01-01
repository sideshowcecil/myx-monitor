package at.ac.tuwien.dsg.myx.monitor.aggregator.events;

import at.ac.tuwien.dsg.myx.monitor.em.events.Event;

public abstract class ModelRequestEvent extends Event {

    private static final long serialVersionUID = -5455418247149963777L;

    private final String runtimeId;

    public ModelRequestEvent(String runtimeId) {
        super();
        this.runtimeId = runtimeId;
    }
    
    public ModelRequestEvent(ModelRequestEvent copyFrom) {
        super(copyFrom);
        runtimeId = copyFrom.getRuntimeId();
    }

    public String getRuntimeId() {
        return runtimeId;
    }

    @Override
    public String toString() {
        return "ModelRequestEvent [base=[" + super.toString() + "], runtimeId=" + getRuntimeId() + "]";
    }

}
