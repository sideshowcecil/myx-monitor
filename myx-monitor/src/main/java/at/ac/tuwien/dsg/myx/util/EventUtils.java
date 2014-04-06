package at.ac.tuwien.dsg.myx.util;

import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLExternalLinkEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostPropertyEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostingEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLLinkEvent;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLRuntimeEvent;

/**
 * This class provides convenient methods for the eventing model.
 * 
 * @author bernd.rathmanner
 * 
 */
public final class EventUtils {

    private EventUtils() {
    }

    /**
     * Get the topic name based on the {@link Event}s class.
     * 
     * @param event
     * @return
     */
    public static String getTopic(Event event) {
        return getTopic(event.getClass());
    }

    /**
     * Get the topic name based on the {@link Event} class.
     * 
     * @param event
     * @return
     */
    public static String getTopic(Class<? extends Event> eventClass) {
        StringBuilder topic = new StringBuilder("event");
        if (XADLEvent.class.isAssignableFrom(eventClass)) {
            topic.append(".xadl");
        } else if (XADLExternalLinkEvent.class.isAssignableFrom(eventClass)) {
            topic.append(".xadlexternallink");
        } else if (XADLHostEvent.class.isAssignableFrom(eventClass)) {
            topic.append(".xadlhost");
            if (XADLHostingEvent.class.isAssignableFrom(eventClass)) {
                topic.append(".hosting");
            } else if (XADLHostingEvent.class.isAssignableFrom(eventClass)) {
                topic.append(".instance");
            } else if (XADLHostPropertyEvent.class.isAssignableFrom(eventClass)) {
                topic.append(".property");
            }
        } else if (XADLLinkEvent.class.isAssignableFrom(eventClass)) {
            topic.append(".xadllink");
        } else if (XADLRuntimeEvent.class.isAssignableFrom(eventClass)) {
            topic.append(".xadlruntime");
        }
        return topic.toString();
    }

}
