package at.ac.tuwien.dsg.myx.monitor.aggregator.events;

import at.ac.tuwien.dsg.myx.monitor.em.events.Event;

public abstract class ModelResponseEvent extends Event {

    private static final long serialVersionUID = 8580723642229018701L;

    private final String runtimeId;

    public ModelResponseEvent(String runtimeId) {
        super();
        this.runtimeId = runtimeId;
    }

    public ModelResponseEvent(ModelResponseEvent copyFrom) {
        super(copyFrom);
        runtimeId = copyFrom.getRuntimeId();
    }

    public String getRuntimeId() {
        return runtimeId;
    }

    @Override
    public String toString() {
        return "ModelResponseEvent [base=[" + super.toString() + "], runtimeId=" + getRuntimeId() + "]";
    }

}
