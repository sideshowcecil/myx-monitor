package at.ac.tuwien.dsg.myx.util;

import java.util.Properties;

import at.ac.tuwien.dsg.myx.monitor.MyxMonitoringImplementation;
import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import edu.uci.isr.myx.fw.IMyxBrick;
import edu.uci.isr.myx.fw.IMyxBrickDescription;
import edu.uci.isr.myx.fw.IMyxContainer;
import edu.uci.isr.myx.fw.IMyxImplementation;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxContainer;
import edu.uci.isr.myx.fw.MyxJavaClassBrickDescription;

/**
 * This class is an extension of {@link edu.uci.isr.myx.fw.MyxUtils} which
 * provides the {@link MyxMonitoringImplementation} as the and additional
 * implementation.
 * 
 * @author bernd.rathmanner
 * 
 */
public final class MyxUtils {

    private static IMyxImplementation currentImplementation = getDefaultImplementation();
    private static EventManager currentEventManager;

    protected static final IMyxBrickDescription CONTAINER_BRICK_DESCRIPTION = new MyxJavaClassBrickDescription(
            new Properties(), MyxContainer.class.getName());

    private MyxUtils() {
    }

    /**
     * Initialize the current {@link MyxMonitoringImplementation}.
     * 
     * @param eventManager
     */
    public static void initMontioringImplementation(EventManager eventManager) {
        currentImplementation = new MyxMonitoringImplementation(eventManager);
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
     * Initialize the current {@link EventManager}.
     * 
     * @param eventManager
     */
    public static void initEventManager(EventManager eventManager) {
        currentEventManager = eventManager;
    }

    /**
     * Get the current {@link EventManager}.
     * 
     * @return
     */
    public static EventManager getEventManager() {
        if (currentEventManager == null) {
            throw new NullPointerException("EventManager has not been initialized!");
        }
        return currentEventManager;
    }

    public static IMyxImplementation getDefaultImplementation() {
        return edu.uci.isr.myx.fw.MyxUtils.getDefaultImplementation();
    }

    public static IMyxName createName(String name) {
        return edu.uci.isr.myx.fw.MyxUtils.createName(name);
    }

    public static IMyxBrickDescription getContainerBrickDescription() {
        return CONTAINER_BRICK_DESCRIPTION;
    }

    public static IMyxName getName(IMyxBrick brick) {
        return edu.uci.isr.myx.fw.MyxUtils.getName(brick);
    }

    public static boolean nulleq(Object o1, Object o2) {
        return edu.uci.isr.myx.fw.MyxUtils.nulleq(o1, o2);
    }

    public static int hc(Object o) {
        return edu.uci.isr.myx.fw.MyxUtils.hc(o);
    }

    public static boolean classeq(Object o1, Object o2) {
        return edu.uci.isr.myx.fw.MyxUtils.classeq(o1, o2);
    }

    public static String pathToString(IMyxName[] path) {
        return edu.uci.isr.myx.fw.MyxUtils.pathToString(path);
    }

    public static IMyxContainer resolvePath(IMyxContainer rootContainer, IMyxName[] path) {
        return edu.uci.isr.myx.fw.MyxUtils.resolvePath(rootContainer, path);
    }

    public static Class<?> classForName(String name, ClassLoader[] clArray) throws ClassNotFoundException {
        return edu.uci.isr.myx.fw.MyxUtils.classForName(name, clArray);
    }

    @SuppressWarnings("unchecked")
    public static <E> E getFirstRequiredServiceObject(IMyxBrick brick, IMyxName interfaceName) {
        E object = (E) edu.uci.isr.myx.fw.MyxUtils.getFirstRequiredServiceObject(brick, interfaceName);
        if (object == null) {
            throw new RuntimeException("Interface " + interfaceName + " returned null for " + brick);
        }
        return object;
    }

    public static Object[] getRequiredServiceObjects(IMyxBrick b, IMyxName interfaceName) {
        return edu.uci.isr.myx.fw.MyxUtils.getRequiredServiceObjects(b, interfaceName);
    }

    public static Properties getInitProperties(IMyxBrick b) {
        return edu.uci.isr.myx.fw.MyxUtils.getInitProperties(b);
    }
}
