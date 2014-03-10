package at.ac.tuwien.dsg.myx;

import java.util.Properties;

import at.ac.tuwien.dsg.myx.monitor.MyxMonitoringImplementation;
import at.ac.tuwien.dsg.myx.monitor.event.EventManager;
import edu.uci.isr.myx.fw.IMyxBrick;
import edu.uci.isr.myx.fw.IMyxBrickDescription;
import edu.uci.isr.myx.fw.IMyxContainer;
import edu.uci.isr.myx.fw.IMyxImplementation;
import edu.uci.isr.myx.fw.IMyxName;

/**
 * This class is an extension of {@link edu.uci.isr.myx.fw.MyxUtils} which
 * provides the {@link MyxMonitoringImplementation} as the and additional
 * implementation.
 * 
 * @author bernd.rathmanner
 * 
 */
public final class MyxUtils {

    private MyxUtils() {
    }

    public static IMyxImplementation getMonitoringImplementation(EventManager eventManager) {
        return new MyxMonitoringImplementation(eventManager);
    }

    public static IMyxImplementation getDefaultImplementation() {
        return edu.uci.isr.myx.fw.MyxUtils.getDefaultImplementation();
    }

    public static IMyxName createName(String name) {
        return edu.uci.isr.myx.fw.MyxUtils.createName(name);
    }

    public static IMyxBrickDescription getContainerBrickDescription() {
        return edu.uci.isr.myx.fw.MyxUtils.getContainerBrickDescription();
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

    public static Object getFirstRequiredServiceObject(IMyxBrick b, IMyxName interfaceName) {
        return edu.uci.isr.myx.fw.MyxUtils.getFirstRequiredServiceObject(b, interfaceName);
    }

    public static Object[] getRequiredServiceObjects(IMyxBrick b, IMyxName interfaceName) {
        return edu.uci.isr.myx.fw.MyxUtils.getRequiredServiceObjects(b, interfaceName);
    }

    public static Properties getInitProperties(IMyxBrick b) {
        return edu.uci.isr.myx.fw.MyxUtils.getInitProperties(b);
    }
}
