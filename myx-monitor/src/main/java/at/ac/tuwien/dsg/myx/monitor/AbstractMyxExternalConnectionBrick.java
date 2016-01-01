package at.ac.tuwien.dsg.myx.monitor;

import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEventType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLExternalLinkEvent;
import at.ac.tuwien.dsg.myx.util.MyxUtils;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;

public abstract class AbstractMyxExternalConnectionBrick extends AbstractMyxSimpleBrick {

    private final EventManager eventManager = MyxUtils.getEventManager();

    private String runtimeId;
    private String blueprintId;

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
        // extract the runtime- and blueprint id if we have not yet got it
        if (runtimeId == null) {
            runtimeId = MyxUtils.getName(this).getName();
            blueprintId = MyxUtils.getInitProperties(this).getProperty(MyxProperties.ARCHITECTURE_BLUEPRINT_ID);
        }
        // we can only send the event if we have a blueprint id
        if (blueprintId == null) {
            return;
        }
        XADLExternalLinkEvent e = new XADLExternalLinkEvent(runtimeId, blueprintId, interfaceType,
                externalConnectionIdentifier, eventType);
        e.setEventSourceId(this.getClass().getName());
        eventManager.handle(e);
    }
}
