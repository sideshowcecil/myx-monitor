package at.ac.tuwien.dsg.myx.monitor.em.events;

import java.util.ArrayList;
import java.util.List;

public class XADLHostingEvent extends XADLHostEvent {

    private final List<String> hostedComponentIds;
    private final List<String> hostedConnectorIds;
    private final List<String> hostedGroupIds;
    private final List<String> hostedSubHostIds;

    public XADLHostingEvent(String archInstanceId, String hostId) {
        super(archInstanceId, hostId);
        hostedComponentIds = new ArrayList<>();
        hostedConnectorIds = new ArrayList<>();
        hostedGroupIds = new ArrayList<>();
        hostedSubHostIds = new ArrayList<>();
    }

    public XADLHostingEvent(XADLHostingEvent copyFrom) {
        super(copyFrom);
        hostedComponentIds = copyFrom.getHostedComponentIds();
        hostedConnectorIds = copyFrom.getHostedConnectorIds();
        hostedGroupIds = copyFrom.getHostedGroupIds();
        hostedSubHostIds = copyFrom.getHostedSubHostIds();
    }

    public List<String> getHostedComponentIds() {
        return hostedComponentIds;
    }

    public List<String> getHostedConnectorIds() {
        return hostedConnectorIds;
    }

    public List<String> getHostedGroupIds() {
        return hostedGroupIds;
    }

    public List<String> getHostedSubHostIds() {
        return hostedSubHostIds;
    }
}
