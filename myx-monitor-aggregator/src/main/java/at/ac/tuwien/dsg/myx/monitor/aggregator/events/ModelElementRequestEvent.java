package at.ac.tuwien.dsg.myx.monitor.aggregator.events;

public class ModelElementRequestEvent extends ModelRequestEvent {

    private static final long serialVersionUID = 7408473254375270739L;

    public ModelElementRequestEvent(String runtimeId) {
        super(runtimeId);
    }

    @Override
    public String toString() {
        return "ModelElementRequestEvent [base=[" + super.toString() + "]]";
    }

}
