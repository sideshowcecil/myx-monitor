package at.ac.tuwien.dsg.myx.monitor.aggregator.events;

public class ModelNoSuchElementResponseEvent extends ModelResponseEvent {

    private static final long serialVersionUID = -1291797031357757022L;

    public ModelNoSuchElementResponseEvent(String runtimeId) {
        super(runtimeId);
    }

    public ModelNoSuchElementResponseEvent(ModelNoSuchElementResponseEvent copyFrom) {
        super(copyFrom);
    }

    @Override
    public String toString() {
        return "ModelNoSuchElementResponseEvent [base=[" + super.toString() + "]]";
    }

}
