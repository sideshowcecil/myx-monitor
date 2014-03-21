package at.ac.tuwien.dsg.myx.util;

import java.util.Properties;

import at.ac.tuwien.dsg.myx.monitor.MyxMonitoringImplementation;
import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.monitor.em.EventManagerImpl;
import edu.uci.isr.myx.fw.IMyxBrick;
import edu.uci.isr.myx.fw.IMyxBrickDescription;
import edu.uci.isr.myx.fw.IMyxContainer;
import edu.uci.isr.myx.fw.IMyxImplementation;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxContainer;
import edu.uci.isr.myx.fw.MyxJavaClassBrickDescription;
import edu.uci.isr.myx.fw.MyxUtils;

/**
 * This class is an extension of {@link MyxUtils} which provides the
 * {@link MyxMonitoringImplementation} as the and additional implementation.
 * 
 * @author bernd.rathmanner
 * 
 */
public final class MyxMonitoringUtils {

    private static IMyxImplementation currentImplementation;
    private static EventManager currentEventManager = new EventManagerImpl();

    protected static final IMyxBrickDescription CONTAINER_BRICK_DESCRIPTION = new MyxJavaClassBrickDescription(
            new Properties(), MyxContainer.class.getName());

    private MyxMonitoringUtils() {
    }

    /**
     * Initialize the current {@link MyxMonitoringImplementation}.
     * 
     * @param architecturRuntimeId
     * @param hostId
     * @param eventManager
     */
    public static void initMontioringImplementation(String architecturRuntimeId, String hostId, EventManager eventManager) {
        currentImplementation = new MyxMonitoringImplementation(architecturRuntimeId, hostId, eventManager);
    }

    /**
     * Get the current {@link MyxMonitoringImplementation}.
     * 
     * @return
     */
    public static IMyxImplementation getMonitoringImplementation() {
        if (currentImplementation == null) {
            throw new NullPointerException("MyxMonitoringImplementation has not been initialized!");
        }
        return currentImplementation;
    }

    /**
     * Get the current {@link EventManager}.
     * 
     * @return
     */
    public static EventManager getEventManager() {
        return currentEventManager;
    }

    public static IMyxImplementation getDefaultImplementation() {
        return MyxUtils.getDefaultImplementation();
    }

    public static IMyxName createName(String name) {
        return MyxUtils.createName(name);
    }

    public static IMyxBrickDescription getContainerBrickDescription() {
        return CONTAINER_BRICK_DESCRIPTION;
    }

    public static IMyxName getName(IMyxBrick brick) {
        return MyxUtils.getName(brick);
    }

    public static boolean nulleq(Object o1, Object o2) {
        return MyxUtils.nulleq(o1, o2);
    }

    public static int hc(Object o) {
        return MyxUtils.hc(o);
    }

    public static boolean classeq(Object o1, Object o2) {
        return MyxUtils.classeq(o1, o2);
    }

    public static String pathToString(IMyxName[] path) {
        return MyxUtils.pathToString(path);
    }

    public static IMyxContainer resolvePath(IMyxContainer rootContainer, IMyxName[] path) {
        return MyxUtils.resolvePath(rootContainer, path);
    }

    public static Class<?> classForName(String name, ClassLoader[] clArray) throws ClassNotFoundException {
        return MyxUtils.classForName(name, clArray);
    }

    public static Object getFirstRequiredServiceObject(IMyxBrick b, IMyxName interfaceName) {
        return MyxUtils.getFirstRequiredServiceObject(b, interfaceName);
    }

    public static Object[] getRequiredServiceObjects(IMyxBrick b, IMyxName interfaceName) {
        return MyxUtils.getRequiredServiceObjects(b, interfaceName);
    }

    public static Properties getInitProperties(IMyxBrick b) {
        return MyxUtils.getInitProperties(b);
    }
}
