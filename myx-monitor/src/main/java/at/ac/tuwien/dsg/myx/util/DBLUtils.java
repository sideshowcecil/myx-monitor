package at.ac.tuwien.dsg.myx.util;

import java.util.ArrayList;
import java.util.Collection;

import edu.uci.isr.myx.fw.EMyxInterfaceDirection;
import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchElement;
import edu.uci.isr.xarch.implementation.IImplementation;
import edu.uci.isr.xarch.implementation.IInterfaceTypeImpl;
import edu.uci.isr.xarch.implementation.ISignatureImpl;
import edu.uci.isr.xarch.implementation.IVariantConnectorTypeImpl;
import edu.uci.isr.xarch.implementationext.IComponentImpl;
import edu.uci.isr.xarch.implementationext.IConnectorImpl;
import edu.uci.isr.xarch.instance.IPoint;
import edu.uci.isr.xarch.instance.IXMLLink;
import edu.uci.isr.xarch.javaimplementation.IJavaClassFile;
import edu.uci.isr.xarch.javaimplementation.IJavaClassName;
import edu.uci.isr.xarch.javaimplementation.IJavaImplementation;
import edu.uci.isr.xarch.javainitparams.IInitializationParameter;
import edu.uci.isr.xarch.javainitparams.IJavaClassFileParams;
import edu.uci.isr.xarch.lookupimplementation.ILookupImplementation;
import edu.uci.isr.xarch.types.IArchStructure;
import edu.uci.isr.xarch.types.IArchTypes;
import edu.uci.isr.xarch.types.IComponent;
import edu.uci.isr.xarch.types.IComponentType;
import edu.uci.isr.xarch.types.IConnector;
import edu.uci.isr.xarch.types.IConnectorType;
import edu.uci.isr.xarch.types.IInterface;
import edu.uci.isr.xarch.types.IInterfaceType;
import edu.uci.isr.xarch.types.ILink;
import edu.uci.isr.xarch.types.ISignature;
import edu.uci.isr.xarch.types.ISignatureInterfaceMapping;
import edu.uci.isr.xarch.types.ISubArchitecture;

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
     * Get the real id of an href attribute.
     * 
     * @param id
     * @return
     */
    public static String getId(String id) {
        if (id.startsWith("#")) {
            return id.substring(1);
        }
        return id;
    }

    /**
     * Get the real id of an {@link IXMLLink} href.
     * 
     * @param id
     * @return
     */
    public static String getId(IXMLLink l) {
        return getId(l.getHref());
    }

    /**
     * Get the id of an {@link IXArchElement}. This method currently supports
     * {@link IComponent}, {@link IConnector} and {@link IInterface}.
     * 
     * @param e
     * @return
     */
    public static String getId(IXArchElement e) {
        if (e instanceof IConnector) {
            return ((IConnector) e).getId();
        } else if (e instanceof IComponent) {
            return ((IComponent) e).getId();
        } else if (e instanceof IInterface) {
            return ((IInterface) e).getId();
        }
        return null;
    }

    /**
     * Get the description of an {@link IXArchElement}. This method currently
     * supports {@link IComponent} and {@link IConnector}.
     * 
     * @param e
     * @return
     */
    public static String getDescription(IXArchElement e) {
        if (e instanceof IComponentType) {
            return ((IComponentType) e).getDescription().getValue();
        } else if (e instanceof IConnectorType) {
            return ((IConnectorType) e).getDescription().getValue();
        }
        return null;
    }

    /**
     * Get the {@link ISubArchitecture} of an {@link IXArchElement}. This method
     * currently supports {@link IComponent} and {@link IConnector}.
     * 
     * @param e
     * @return
     */
    public static ISubArchitecture getSubArchitecture(IXArchElement e) {
        if (e instanceof IComponentType) {
            return ((IComponentType) e).getSubArchitecture();

        } else if (e instanceof IConnectorType) {
            return ((IConnectorType) e).getSubArchitecture();
        }
        return null;
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
     * Get a specific {@link IArchStructure} element in an {@link IXArch}
     * instance.
     * 
     * @param root
     * @return
     */
    public static IArchStructure getArchStructure(IXArch root, String id) {
        for (IArchStructure archStruc : getArchStructures(root)) {
            if (archStruc.getId() == id) {
                return archStruc;
            }
        }
        return null;
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
     * Get all {@link IComponentType} elements in and {@link IArchTypes}
     * instance.
     * 
     * @param archTypes
     * @return
     */
    public static Collection<IComponentType> getComponentTypes(IArchTypes archTypes) {
        Collection<IComponentType> elements = new ArrayList<>();
        for (Object o : archTypes.getAllComponentTypes()) {
            if (o instanceof IComponentType) {
                elements.add((IComponentType) o);
            }
        }
        return elements;
    }

    /**
     * Get all {@link IConnectorType} elements in and {@link IArchTypes}
     * instance.
     * 
     * @param archTypes
     * @return
     */
    public static Collection<IConnectorType> getConnectorTypes(IArchTypes archTypes) {
        Collection<IConnectorType> elements = new ArrayList<>();
        for (Object o : archTypes.getAllConnectorTypes()) {
            if (o instanceof IConnectorType) {
                elements.add((IConnectorType) o);
            }
        }
        return elements;
    }

    /**
     * Get all {@link IInterfaceType} elements in an {@link IArchTypes}
     * instance.
     * 
     * @param archTypes
     * @return
     */
    public static Collection<IInterfaceType> getInterfaceTypes(IArchTypes archTypes) {
        Collection<IInterfaceType> elements = new ArrayList<>();
        for (Object o : archTypes.getAllInterfaceTypes()) {
            if (o instanceof IInterfaceType) {
                elements.add((IInterfaceType) o);
            }
        }
        return elements;
    }

    /**
     * Get all {@link IComponent}s of an {@link IArchStructure}.
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
     * Get all {@link IConnector}s of an {@link IArchStructure}.
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
     * Get all {@link ILink}s of an {@link IArchStructure} in a type safe
     * collection.
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
     * Get all {@link IInterface}s of an {@link IXArchElement}.
     * 
     * @param e
     * @return
     */
    public static Collection<IInterface> getInterfaces(IXArchElement e) {
        Collection<IInterface> elements = new ArrayList<>();
        if (e instanceof IComponent) {
            elements.addAll(getInterfaces((IComponent) e));
        } else if (e instanceof IConnector) {
            elements.addAll(getInterfaces((IConnector) e));
        }
        return elements;
    }

    /**
     * Get all {@link IInterface}s of an {@link IComponent}.
     * 
     * @param comp
     * @return
     */
    public static Collection<IInterface> getInterfaces(IComponent comp) {
        Collection<IInterface> elements = new ArrayList<>();
        for (Object o : comp.getAllInterfaces()) {
            if (o instanceof IInterface) {
                elements.add((IInterface) o);
            }
        }
        return elements;
    }

    /**
     * Get all {@link IInterface}s of a {@link IConnector}.
     * 
     * @param conn
     * @return
     */
    public static Collection<IInterface> getInterfaces(IConnector conn) {
        Collection<IInterface> elements = new ArrayList<>();
        for (Object o : conn.getAllInterfaces()) {
            if (o instanceof IInterface) {
                elements.add((IInterface) o);
            }
        }
        return elements;
    }

    /**
     * Find a specific component {@link IInterface} in an {@link IArchStructure}
     * .
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
     * Find the {@link IComponent} or {@link IConnector} that the given
     * interface belongs to.
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
     * Find a specific {@link ISignature} in an {@link IXArch} instance.
     * 
     * @param root
     * @param id
     * @return
     */
    public static ISignature getSignature(IXArch root, String id) {
        for (IArchTypes at : getArchTypes(root)) {
            for (Object o : at.getAllComponentTypes()) {
                if (o instanceof IComponentType) {
                    ISignature s = ((IComponentType) o).getSignature(id);
                    if (s != null) {
                        return s;
                    }
                }
            }
            for (Object o : at.getAllConnectorTypes()) {
                if (o instanceof IConnectorType) {
                    ISignature s = ((IConnectorType) o).getSignature(id);
                    if (s != null) {
                        return s;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Get all {@link ISignatureInterfaceMapping}s of an
     * {@link ISubArchitecture}.
     * 
     * @param subArch
     * @return
     */
    public static Collection<ISignatureInterfaceMapping> getSignatureInterfaceMappings(ISubArchitecture subArch) {
        Collection<ISignatureInterfaceMapping> elements = new ArrayList<>();
        for (Object o : subArch.getAllSignatureInterfaceMappings()) {
            if (o instanceof ISignatureInterfaceMapping) {
                elements.add((ISignatureInterfaceMapping) o);
            }
        }
        return elements;
    }

    /**
     * Get the {@link EMyxInterfaceDirection} from an {@link IInterface}.
     * 
     * @param intf
     * @return
     */
    public static EMyxInterfaceDirection getDirection(IInterface intf) {
        switch (intf.getDirection().getValue()) {
        case "in":
            return EMyxInterfaceDirection.IN;
        case "out":
            return EMyxInterfaceDirection.OUT;
        default:
            return null;
        }
    }

    /**
     * Get all {@link IImplementation}s of an {@link IConnectorType}.
     * 
     * @param type
     * @return
     */
    public static Collection<IImplementation> getImplementations(IConnectorType type) {
        Collection<IImplementation> elements = new ArrayList<>();
        if (type instanceof IVariantConnectorTypeImpl) {
            for (Object o : ((IVariantConnectorTypeImpl) type).getAllImplementations()) {
                if (o instanceof IImplementation) {
                    elements.add((IImplementation) o);
                }
            }
        }
        return elements;
    }

    /**
     * Get all {@link IImplementation}s of an {@link IComponentType}.
     * 
     * @param type
     * @return
     */
    public static Collection<IImplementation> getImplementations(IComponentType type) {
        Collection<IImplementation> elements = new ArrayList<>();
        if (type instanceof IVariantConnectorTypeImpl) {
            for (Object o : ((IVariantConnectorTypeImpl) type).getAllImplementations()) {
                if (o instanceof IImplementation) {
                    elements.add((IImplementation) o);
                }
            }
        }
        return elements;
    }

    /**
     * Get all {@link IJavaImplementation}s of an {@link IConnectorType}.
     * 
     * @param type
     * @return
     */
    public static Collection<IJavaImplementation> getJavaImplementations(IConnectorType type) {
        Collection<IJavaImplementation> elements = new ArrayList<>();
        if (type instanceof IVariantConnectorTypeImpl) {
            for (Object o : ((IVariantConnectorTypeImpl) type).getAllImplementations()) {
                if (o instanceof IJavaImplementation) {
                    elements.add((IJavaImplementation) o);
                }
            }
        }
        return elements;
    }

    /**
     * Get all {@link IJavaImplementation}s of an {@link IComponentType}.
     * 
     * @param type
     * @return
     */
    public static Collection<IJavaImplementation> getJavaImplementations(IComponentType type) {
        Collection<IJavaImplementation> elements = new ArrayList<>();
        if (type instanceof IVariantConnectorTypeImpl) {
            for (Object o : ((IVariantConnectorTypeImpl) type).getAllImplementations()) {
                if (o instanceof IJavaImplementation) {
                    elements.add((IJavaImplementation) o);
                }
            }
        }
        return elements;
    }

    /**
     * Get all {@link IJavaImplementation}s of an {@link IInterfaceType}.
     * 
     * @param type
     * @return
     */
    public static Collection<IJavaImplementation> getJavaImplementations(IInterfaceType type) {
        Collection<IJavaImplementation> elements = new ArrayList<>();
        if (type instanceof IInterfaceTypeImpl) {
            for (Object o : ((IInterfaceTypeImpl) type).getAllImplementations()) {
                if (o instanceof IJavaImplementation) {
                    elements.add((IJavaImplementation) o);
                }
            }
        }
        return elements;
    }

    /**
     * Get all {@link IJavaImplementation}s of an {@link IComponent}.
     * 
     * @param c
     * @return
     */
    public static Collection<IJavaImplementation> getJavaImplementations(IComponent c) {
        Collection<IJavaImplementation> elements = new ArrayList<>();
        if (c instanceof IComponentImpl) {
            for (Object o : ((IComponentImpl) c).getAllImplementations()) {
                if (o instanceof IJavaImplementation) {
                    elements.add((IJavaImplementation) o);
                }
            }
        }
        return elements;
    }

    /**
     * Get all {@link IJavaImplementation}s of an {@link IConnector}.
     * 
     * @param c
     * @return
     */
    public static Collection<IJavaImplementation> getJavaImplementations(IConnector c) {
        Collection<IJavaImplementation> elements = new ArrayList<>();
        if (c instanceof IConnectorImpl) {
            for (Object o : ((IConnectorImpl) c).getAllImplementations()) {
                if (o instanceof IJavaImplementation) {
                    elements.add((IJavaImplementation) o);
                }
            }
        }
        return elements;
    }

    /**
     * Get all aux {@link IJavaClassName} from an {@link IJavaImplementation}.
     * 
     * @param impl
     * @return
     */
    public static Collection<IJavaClassFile> getAuxClasses(IJavaImplementation impl) {
        Collection<IJavaClassFile> elements = new ArrayList<>();
        for (Object o : impl.getAllAuxClasss()) {
            if (o instanceof IJavaClassFile) {
                elements.add((IJavaClassFile) o);
            }
        }
        return elements;
    }

    /**
     * Get all {@link IInitializationParameter}s from an
     * {@link IJavaClassFileParams}.
     * 
     * @param classFile
     * @return
     */
    public static Collection<IInitializationParameter> getInitParameters(IJavaClassFileParams classFile) {
        Collection<IInitializationParameter> elements = new ArrayList<>();
        for (Object o : classFile.getAllInitializationParameters()) {
            if (o instanceof IInitializationParameter) {
                elements.add((IInitializationParameter) o);
            }
        }
        return elements;
    }

    /**
     * Get all {@link ISignature}s from an {@link IComponentType}.
     * 
     * @param type
     * @return
     */
    public static Collection<ISignature> getSignatures(IComponentType type) {
        Collection<ISignature> elements = new ArrayList<>();
        for (Object o : type.getAllSignatures()) {
            if (o instanceof ISignature) {
                elements.add((ISignature) o);
            }
        }
        return elements;
    }

    /**
     * Get all {@link ISignature}s from an {@link IConnectorType}.
     * 
     * @param type
     * @return
     */
    public static Collection<ISignature> getSignatures(IConnectorType type) {
        Collection<ISignature> elements = new ArrayList<>();
        for (Object o : type.getAllSignatures()) {
            if (o instanceof ISignature) {
                elements.add((ISignature) o);
            }
        }
        return elements;
    }

    /**
     * Get all {@link ILookupImplementation}s of an {@link ISignature}.
     * 
     * @param s
     * @return
     */
    public static Collection<ILookupImplementation> getLookupImplementations(ISignature s) {
        Collection<ILookupImplementation> elements = new ArrayList<>();
        if (s instanceof ISignatureImpl) {
            for (Object o : ((ISignatureImpl) s).getAllImplementations()) {
                if (o instanceof ILookupImplementation) {
                    elements.add((ILookupImplementation) o);
                }
            }
        }
        return elements;
    }

    /**
     * Get the {@link EMyxInterfaceDirection} from an {@link ISignature}.
     * 
     * @param signature
     * @return
     */
    public static EMyxInterfaceDirection getDirection(ISignature signature) {
        switch (signature.getDirection().getValue()) {
        case "in":
            return EMyxInterfaceDirection.IN;
        case "out":
            return EMyxInterfaceDirection.OUT;
        default:
            return null;
        }
    }
}
