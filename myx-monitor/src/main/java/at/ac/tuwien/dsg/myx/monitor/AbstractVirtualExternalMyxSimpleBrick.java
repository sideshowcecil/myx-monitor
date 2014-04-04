package at.ac.tuwien.dsg.myx.monitor;

import java.util.Properties;

import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEventType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLExternalLinkEvent;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;

public abstract class AbstractVirtualExternalMyxSimpleBrick extends AbstractMyxSimpleBrick {

    private String architectureRuntimeId;
    private EventManager eventManager = MyxMonitoringUtils.getEventManager();

    public AbstractVirtualExternalMyxSimpleBrick() {
    }

    @Override
    public void init() {
        Properties initProps = MyxMonitoringUtils.getInitProperties(this);
        if (initProps != null) {
            architectureRuntimeId = initProps.getProperty(MyxProperties.ARCHITECTURE_RUNTIME_ID);
        }
    }

    /**
     * Dispatch a event that an external link has been connected.
     * 
     * @param interfaceName
     * @param interfaceType
     * @param externalConnectionIdentifier
     */
    protected void dispatchExternalLinkConnectedEvent(String interfaceName, String interfaceType,
            String externalConnectionIdentifier) {
        dispatchExternalLinkEvent(interfaceName, interfaceType, externalConnectionIdentifier, XADLEventType.ADD);
    }

    /**
     * Dispatch a event that an external link has been disconnected.
     * 
     * @param interfaceName
     * @param interfaceType
     * @param externalConnectionIdentifier
     */
    protected void dispatchExternalLinkDisconnectedEvent(String interfaceName, String interfaceType,
            String externalConnectionIdentifier) {
        dispatchExternalLinkEvent(interfaceName, interfaceType, externalConnectionIdentifier, XADLEventType.REMOVE);
    }

    /**
     * Dispatch the {@link XADLExternalLinkEvent}.
     * 
     * @param interfaceName
     * @param interfaceType
     * @param externalConnectionIdentifier
     * @param eventType
     */
    private void dispatchExternalLinkEvent(String interfaceName, String interfaceType,
            String externalConnectionIdentifier, XADLEventType eventType) {
        if (architectureRuntimeId != null) {
            String runtimeId = MyxMonitoringUtils.getName(this).getName();

            XADLExternalLinkEvent e = new XADLExternalLinkEvent(architectureRuntimeId, runtimeId, interfaceName,
                    interfaceType, externalConnectionIdentifier, eventType);
            e.setEventSourceId(this.getClass().getName());
            eventManager.handle(e);
        }
    }
}
