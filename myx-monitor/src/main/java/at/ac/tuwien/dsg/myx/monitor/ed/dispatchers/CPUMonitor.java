package at.ac.tuwien.dsg.myx.monitor.ed.dispatchers;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

import at.ac.tuwien.dsg.myx.monitor.ed.EventDispatcher;
import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostProperties;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLHostPropertyEvent;

@SuppressWarnings("restriction")
public class CPUMonitor extends EventDispatcher {

    public CPUMonitor(EventManager eventManager) {
        super(eventManager);
    }

    @Override
    public void run() {
        OperatingSystemMXBean osb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        while (true) {
            // create event and set properties
            XADLHostPropertyEvent cpuLoadEvent = initHostPropertyEvent();
            if (osb.getSystemCpuLoad() >= 0) {
                cpuLoadEvent.getHostProperties().put(XADLHostProperties.CPU_SYSTEM_LOAD, osb.getSystemCpuLoad());
            }
            if (osb.getProcessCpuLoad() >= 0) {
                cpuLoadEvent.getHostProperties().put(XADLHostProperties.CPU_PROCESS_LOAD, osb.getProcessCpuLoad());
            }

            // dispatch the event
            if (!cpuLoadEvent.getHostProperties().isEmpty()) {
                dispatch(cpuLoadEvent);
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
