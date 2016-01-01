package at.ac.tuwien.dsg.myx.monitor.ed;

/**
 * EventDispatcher interface specification.
 * 
 * @author bernd.rathmanner
 * 
 */
public interface EventDispatcher extends Runnable {
    /**
     * Execute the monitoring.
     * 
     * @see Runnable
     */
    @Override
    public void run();
}
