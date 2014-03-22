package at.ac.tuwien.dsg.myx.monitor.ed.dispatchers;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import at.ac.tuwien.dsg.myx.monitor.ed.EventDispatcher;
import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostPropertyEvent;

public class MemoryMonitor extends EventDispatcher {

    public MemoryMonitor(String architectureRuntimeId, String hostId, EventManager eventManager) {
        super(architectureRuntimeId, hostId, eventManager);
    }

    @Override
    public void run() {
        MemoryMXBean mb = ManagementFactory.getMemoryMXBean();

        while (true) {
            // create event and set properties
            XADLHostPropertyEvent memoryEvent = initHostPropertyEvent();
            memoryEvent.getHostProperties().put("heap-memory-used", mb.getHeapMemoryUsage().getUsed()); // TODO extract key
            memoryEvent.getHostProperties().put("heap-memory-commited", mb.getHeapMemoryUsage().getCommitted()); // TODO extract key
            if (mb.getHeapMemoryUsage().getMax() != -1) {
                memoryEvent.getHostProperties().put("heap-memory-free",
                        mb.getHeapMemoryUsage().getMax() - mb.getHeapMemoryUsage().getCommitted()); // TODO extract key
            }
            memoryEvent.getHostProperties().put("non-heap-memory-used", mb.getNonHeapMemoryUsage().getUsed()); // TODO extract key
            memoryEvent.getHostProperties().put("non-heap-memory-commited", mb.getNonHeapMemoryUsage().getCommitted()); // TODO extract key
            if (mb.getNonHeapMemoryUsage().getMax() != -1) {
                memoryEvent.getHostProperties().put("non-heap-memory-free",
                        mb.getNonHeapMemoryUsage().getMax() - mb.getNonHeapMemoryUsage().getCommitted()); // TODO extract key
            }

            // dispatch the event
            if (!memoryEvent.getHostProperties().isEmpty()) {
                dispatch(memoryEvent);
            }

            // sleep for some time
            try {
                Thread.sleep(15 * 1000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

}
