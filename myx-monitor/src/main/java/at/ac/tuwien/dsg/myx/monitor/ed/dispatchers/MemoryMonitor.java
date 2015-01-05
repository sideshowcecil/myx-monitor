package at.ac.tuwien.dsg.myx.monitor.ed.dispatchers;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import at.ac.tuwien.dsg.myx.monitor.ed.EventDispatcher;
import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostProperties;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostPropertyEvent;

public class MemoryMonitor extends EventDispatcher {

    public MemoryMonitor(EventManager eventManager) {
        super(eventManager);
    }

    @Override
    public void run() {
        MemoryMXBean mb = ManagementFactory.getMemoryMXBean();

        while (true) {
            // create event and set properties
            XADLHostPropertyEvent memoryEvent = initHostPropertyEvent();
            memoryEvent.getHostProperties().put(XADLHostProperties.MEMORY_HEAP_USED, mb.getHeapMemoryUsage().getUsed());
            if (mb.getHeapMemoryUsage().getMax() != -1) {
                memoryEvent.getHostProperties().put(XADLHostProperties.MEMORY_HEAP_FREE,
                        mb.getHeapMemoryUsage().getMax() - mb.getHeapMemoryUsage().getUsed());
            }
            memoryEvent.getHostProperties().put(XADLHostProperties.MEMORY_NON_HEAP_USED,
                    mb.getNonHeapMemoryUsage().getUsed());
            if (mb.getNonHeapMemoryUsage().getMax() != -1) {
                memoryEvent.getHostProperties().put(XADLHostProperties.MEMORY_NON_HEAP_FREE,
                        mb.getNonHeapMemoryUsage().getMax() - mb.getNonHeapMemoryUsage().getUsed());
            }

            // dispatch the event
            if (!memoryEvent.getHostProperties().isEmpty()) {
                dispatch(memoryEvent);
            }

            // sleep for some time
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

}
