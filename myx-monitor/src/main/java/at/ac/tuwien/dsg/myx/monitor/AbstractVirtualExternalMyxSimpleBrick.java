package at.ac.tuwien.dsg.myx.monitor;

import java.util.Properties;

import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEventType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLExternalLinkEvent;
import at.ac.tuwien.dsg.myx.util.MyxUtils;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;

public abstract class AbstractVirtualExternalMyxSimpleBrick extends AbstractMyxSimpleBrick {

    private EventManager eventManager = MyxUtils.getEventManager();

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
        Properties initProperties = MyxUtils.getInitProperties(this);
        if (initProperties.containsKey(MyxProperties.ARCHITECTURE_BLUEPRINT_ID)) {
            // we can only send the event if we have a blueprint id
            String runtimeId = MyxUtils.getName(this).getName();
            String blueprintId = initProperties.getProperty(MyxProperties.ARCHITECTURE_BLUEPRINT_ID);

            XADLExternalLinkEvent e = new XADLExternalLinkEvent(runtimeId, blueprintId, interfaceType,
                    externalConnectionIdentifier, eventType);
            e.setEventSourceId(this.getClass().getName());
            eventManager.handle(e);
        }
    }
}
