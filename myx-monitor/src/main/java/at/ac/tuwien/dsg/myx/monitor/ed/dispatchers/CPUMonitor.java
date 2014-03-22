package at.ac.tuwien.dsg.myx.monitor.ed.dispatchers;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

import at.ac.tuwien.dsg.myx.monitor.ed.EventDispatcher;
import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLEventType;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostPropertyEvent;

@SuppressWarnings("restriction")
public class CPUMonitor extends EventDispatcher {

    public CPUMonitor(String architectureRuntimeId, String hostId, EventManager eventManager) {
        super(architectureRuntimeId, hostId, eventManager);
    }

    @Override
    public void run() {
        OperatingSystemMXBean osb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        while (true) {
            // create event and set properties
            XADLHostPropertyEvent cpuLoadEvent = initHostPropertyEvent();
            if (osb.getSystemCpuLoad() >= 0) {
                cpuLoadEvent.getHostProperties().put("system-cpu-load", osb.getSystemCpuLoad()); // TODO extract key
            }
            if (osb.getProcessCpuLoad() >= 0) {
                cpuLoadEvent.getHostProperties().put("process-cpu-load", osb.getProcessCpuLoad()); // TODO extract key
            }

            // dispatch the event
            if (!cpuLoadEvent.getHostProperties().isEmpty()) {
                dispatch(cpuLoadEvent);
            }

            // sleep for some time
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    /**
     * Initialize a {@link XADLHostPropertyEvent}.
     * 
     * @return
     */
    private XADLHostPropertyEvent initHostPropertyEvent() {
        return new XADLHostPropertyEvent(getArchitectureRuntimeId(), getHostId(), XADLEventType.UPDATE);
    }

}
