package at.ac.tuwien.dsg.myx.monitor;

import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEventType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLExternalLinkEvent;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;

public abstract class AbstractVirtualExternalMyxSimpleBrick extends AbstractMyxSimpleBrick {

    private EventManager eventManager = MyxMonitoringUtils.getEventManager();

    public AbstractVirtualExternalMyxSimpleBrick() {
    }

    /**
     * Dispatch a event that an external link has been connected.
     * 
     * @param interfaceName
     * @param interfaceType
     * @param externalConnectionIdentifier
     */
    protected void dispatchExternalLinkConnectedEvent(String interfaceType, String externalConnectionIdentifier) {
        dispatchExternalLinkEvent(interfaceType, externalConnectionIdentifier, XADLEventType.ADD);
    }

    /**
     * Dispatch a event that an external link has been disconnected.
     * 
     * @param interfaceName
     * @param interfaceType
     * @param externalConnectionIdentifier
     */
    protected void dispatchExternalLinkDisconnectedEvent(String interfaceType, String externalConnectionIdentifier) {
        dispatchExternalLinkEvent(interfaceType, externalConnectionIdentifier, XADLEventType.REMOVE);
    }

    /**
     * Dispatch the {@link XADLExternalLinkEvent}.
     * 
     * @param interfaceName
     * @param interfaceType
     * @param externalConnectionIdentifier
     * @param eventType
     */
    private void dispatchExternalLinkEvent(String interfaceType, String externalConnectionIdentifier,
            XADLEventType eventType) {
        String runtimeId = MyxMonitoringUtils.getName(this).getName();

        XADLExternalLinkEvent e = new XADLExternalLinkEvent(runtimeId, interfaceType, externalConnectionIdentifier,
                eventType);
        e.setEventSourceId(this.getClass().getName());
        eventManager.handle(e);
    }
}
