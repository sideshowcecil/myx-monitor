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
        StringBuilder topic = new StringBuilder();
        topic.append(getTopic(event.getClass()));
        if (event instanceof XADLEvent) {
            topic.append(".").append(((XADLEvent) event).getXadlBlueprintId());
        } else if (event instanceof XADLExternalLinkEvent) {
            topic.append(".").append(((XADLExternalLinkEvent) event).getXadlBlueprintId());
        } else if (event instanceof XADLHostEvent) {
            topic.append(".").append(((XADLHostEvent) event).getHostId());
        } else if (event instanceof XADLLinkEvent) {
            topic.append(".").append(((XADLLinkEvent) event).getXadlSourceBlueprintId()).append(".")
                    .append(((XADLLinkEvent) event).getXadlDestinationBlueprintId());
        } else if (event instanceof XADLRuntimeEvent) {
            topic.append(".").append(((XADLRuntimeEvent) event).getXadlBlueprintId());
        }
        return topic.toString();
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

    /**
     * Get the topic pattern to receive all events of these event class and its
     * subclasses.
     * 
     * @param eventClass
     * @return
     */
    public static String getTopicPattern(Class<? extends Event> eventClass) {
        return getTopic(eventClass) + ".*";
    }

}
