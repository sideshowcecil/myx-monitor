package at.ac.tuwien.dsg.myx;

import java.util.ArrayList;
import java.util.Collection;

import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchElement;
import edu.uci.isr.xarch.instance.IPoint;
import edu.uci.isr.xarch.types.IArchStructure;
import edu.uci.isr.xarch.types.IArchTypes;
import edu.uci.isr.xarch.types.IComponent;
import edu.uci.isr.xarch.types.IComponentType;
import edu.uci.isr.xarch.types.IConnector;
import edu.uci.isr.xarch.types.IConnectorType;
import edu.uci.isr.xarch.types.IInterface;
import edu.uci.isr.xarch.types.ILink;
import edu.uci.isr.xarch.types.ISignature;

/**
 * This class provides convenient methods to handle the shortcomings of the <a
 * href="http://isr.uci.edu/projects/archstudio-4/www/xarchuci/tools-dbl.html">
 * Data Binding Library</a>.
 * 
 * @author bernd.rathmanner
 * 
 */
public final class DBLUtils {

    private DBLUtils() {
    }

    /**
     * Get all {@link IArchStructure} elements in an {@link IXArch} instance.
     * 
     * @param root
     * @return
     */
    public static Collection<IArchStructure> getArchStructures(IXArch root) {
        Collection<IArchStructure> elements = new ArrayList<>();
        for (Object o : root.getAllObjects()) {
            if (o instanceof IArchStructure) {
                elements.add((IArchStructure) o);
            }
        }
        return elements;
    }

    /**
     * Get all {@link IArchTypes} elements in an {@link IXArch} instance.
     * 
     * @param root
     * @return
     */
    public static Collection<IArchTypes> getArchTypes(IXArch root) {
        Collection<IArchTypes> elements = new ArrayList<>();
        for (Object o : root.getAllObjects()) {
            if (o instanceof IArchTypes) {
                elements.add((IArchTypes) o);
            }
        }
        return elements;
    }

    /**
     * Get all components of a structure in a type safe collection.
     * 
     * @param structure
     * @return
     */
    public static Collection<IComponent> getComponents(IArchStructure structure) {
        Collection<IComponent> elements = new ArrayList<>();
        for (Object o : structure.getAllComponents()) {
            if (o instanceof IComponent) {
                elements.add((IComponent) o);
            }
        }
        return elements;
    }

    /**
     * Get all connectors of a structure in a type safe collection.
     * 
     * @param structure
     * @return
     */
    public static Collection<IConnector> getConnectors(IArchStructure structure) {
        Collection<IConnector> elements = new ArrayList<>();
        for (Object o : structure.getAllConnectors()) {
            if (o instanceof IConnector) {
                elements.add((IConnector) o);
            }
        }
        return elements;
    }

    /**
     * Get all links of a structure in a type safe collection.
     * 
     * @param structure
     * @return
     */
    public static Collection<ILink> getLinks(IArchStructure structure) {
        Collection<ILink> elements = new ArrayList<>();
        for (Object o : structure.getAllLinks()) {
            if (o instanceof ILink) {
                elements.add((ILink) o);
            }
        }
        return elements;
    }

    /**
     * Get all points of a link in a type safe collection.
     * 
     * @param link
     * @return
     */
    public static Collection<IPoint> getPoints(ILink link) {
        Collection<IPoint> elements = new ArrayList<>();
        for (Object o : link.getAllPoints()) {
            if (o instanceof IPoint) {
                elements.add((IPoint) o);
            }
        }
        return elements;
    }

    /**
     * Find a specific component interface in a structure.
     * 
     * @param structure
     * @param id
     * @return
     */
    public static IInterface getInterface(IArchStructure structure, String id) {
        for (IComponent c : getComponents(structure)) {
            IInterface i = c.getInterface(id);
            if (i != null) {
                return i;
            }
        }
        for (IConnector c : getConnectors(structure)) {
            IInterface i = c.getInterface(id);
            if (i != null) {
                return i;
            }
        }
        return null;
    }

    /**
     * Find the component that the given interface belongs to.
     * 
     * @param iface
     * @return
     */
    public static IXArchElement getParentOfInterface(IArchStructure structure, IInterface iface) {
        for (IComponent c : getComponents(structure)) {
            if (c.getInterface(iface.getId()) != null) {
                return c;
            }
        }
        for (IConnector c : getConnectors(structure)) {
            if (c.getInterface(iface.getId()) != null) {
                return c;
            }
        }
        return null;
    }

    /**
     * Find a specific signature in a {@link IXArch} instance.
     * @param root
     * @param id
     * @return
     */
    public static ISignature getSignature(IXArch root, String id) {
        for (IArchTypes at : getArchTypes(root)) {
            for (Object o : at.getAllComponentTypes()) {
                if (o instanceof IComponentType) {
                    ISignature s = ((IComponentType)o).getSignature(id);
                    if (s != null) {
                        return s;
                    }
                }
            }
            for (Object o : at.getAllConnectorTypes()) {
                if (o instanceof IConnectorType) {
                    ISignature s = ((IConnectorType)o).getSignature(id);
                    if (s != null) {
                        return s;
                    }
                }
            }
        }
        return null;
    }
}
