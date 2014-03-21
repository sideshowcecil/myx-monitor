package at.ac.tuwien.dsg.myx.monitor.em.events;

import java.util.Properties;

public class XADLHostPropertyEvent extends XADLHostEvent {

    private Properties hostProperties;

    public XADLHostPropertyEvent(String architectureRuntimeId, String hostId) {
        super(architectureRuntimeId, hostId);
        hostProperties = new Properties();
    }

    public XADLHostPropertyEvent(String architectureRuntimeId, String hostId, Properties hostProperties) {
        this(architectureRuntimeId, hostId);
        this.hostProperties = hostProperties;
    }

    public XADLHostPropertyEvent(XADLHostPropertyEvent copyFrom) {
        super(copyFrom);
        hostProperties = copyFrom.getHostProperties();
    }

    public Properties getHostProperties() {
        return hostProperties;
    }

    public void setHostProperties(Properties hostProperties) {
        this.hostProperties = hostProperties;
    }

    @Override
    public String toString() {
        return "XADLHostPropertyEvent [base=[" + super.toString() + "], hostProperties=" + getHostProperties() + "]";
    }
}
