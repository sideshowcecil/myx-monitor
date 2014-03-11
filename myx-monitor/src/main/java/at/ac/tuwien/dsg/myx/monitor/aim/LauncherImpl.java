package at.ac.tuwien.dsg.myx.monitor.aim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.dsg.myx.monitor.aim.structure.Component;
import at.ac.tuwien.dsg.myx.monitor.aim.structure.Connector;
import at.ac.tuwien.dsg.myx.monitor.aim.structure.Implementation;
import at.ac.tuwien.dsg.myx.monitor.aim.structure.InstantiationElement;
import at.ac.tuwien.dsg.myx.monitor.aim.structure.Interface;
import at.ac.tuwien.dsg.myx.monitor.aim.structure.Link;
import at.ac.tuwien.dsg.myx.monitor.aim.structure.Signature;
import at.ac.tuwien.dsg.myx.monitor.aim.structure.SubArchitecture;
import at.ac.tuwien.dsg.myx.monitor.aim.structure.type.ArchitectureType;
import at.ac.tuwien.dsg.myx.monitor.aim.structure.type.ComponentType;
import at.ac.tuwien.dsg.myx.monitor.aim.structure.type.ConnectorType;
import at.ac.tuwien.dsg.myx.monitor.aim.structure.type.InterfaceType;
import at.ac.tuwien.dsg.myx.monitor.model.ModelRoot;
import at.ac.tuwien.dsg.myx.util.DBLUtils;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.myx.util.Tuple;
import edu.uci.isr.myx.fw.EMyxInterfaceDirection;
import edu.uci.isr.myx.fw.IMyxBrickDescription;
import edu.uci.isr.myx.fw.IMyxInterfaceDescription;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.IMyxRuntime;
import edu.uci.isr.myx.fw.IMyxWeld;
import edu.uci.isr.myx.fw.MyxBrickCreationException;
import edu.uci.isr.myx.fw.MyxBrickLoadException;
import edu.uci.isr.myx.fw.MyxJavaClassBrickDescription;
import edu.uci.isr.myx.fw.MyxJavaClassInterfaceDescription;
import edu.uci.isr.xarch.IXArchElement;
import edu.uci.isr.xarch.instance.IPoint;
import edu.uci.isr.xarch.instance.IXMLLink;
import edu.uci.isr.xarch.javaimplementation.IJavaClassFile;
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

public class LauncherImpl implements Launcher {

    protected IMyxRuntime myx;
    protected ModelRoot modelRoot;

    protected IMyxBrickDescription containerBrickDescription;

    protected Map<String, InterfaceType> interfaceTypes;
    protected Map<String, ArchitectureType> types;
    protected Map<String, Signature> signatures;

    public LauncherImpl(IMyxRuntime myx, ModelRoot modelRoot) {
        this.myx = myx;
        this.modelRoot = modelRoot;
        containerBrickDescription = MyxMonitoringUtils.getContainerBrickDescription();
    }

    @Override
    public void instantiate(String name, IArchStructure structure) throws ArchitectureInstantiationException {
        if (interfaceTypes == null) {
            interfaceTypes = getInterfaceTypes();
            types = getComponentAndConnectorTypes();
            signatures = new HashMap<>();
            for (ArchitectureType t : types.values()) {
                for (Signature s : t.getSignatures()) {
                    signatures.put(s.getId(), s);
                }
            }
        }
        instantiate(name, structure, new IMyxName[0]);
    }

    /**
     * Get all interface types.
     * 
     * @return
     * @throws ArchitectureInstantiationException
     */
    private Map<String, InterfaceType> getInterfaceTypes() throws ArchitectureInstantiationException {
        Map<String, InterfaceType> types = new HashMap<>();
        for (IArchTypes at : DBLUtils.getArchTypes(modelRoot.getArchitectureRoot())) {
            for (IInterfaceType t : DBLUtils.getInterfaceTypes(at)) {
                InterfaceType it = new InterfaceType(t.getId());
                it.setDescription(t.getDescription().getValue());
                try {
                    it.getImplementations().addAll(getImplementations(DBLUtils.getJavaImplementations(t)));
                } catch (ArchitectureInstantiationException e) {
                    throw new ArchitectureInstantiationException("Java implementation of type "
                            + t.getDescription().getValue() + " lacks main class name");
                }
                types.put(it.getId(), it);
            }
        }
        return types;
    }

    /**
     * Get all component and connector types.
     * 
     * @return
     * @throws ArchitectureInstantiationException
     */
    private Map<String, ArchitectureType> getComponentAndConnectorTypes() throws ArchitectureInstantiationException {
        Map<String, ArchitectureType> types = new HashMap<>();
        for (IArchTypes at : DBLUtils.getArchTypes(modelRoot.getArchitectureRoot())) {
            for (IComponentType t : DBLUtils.getComponentTypes(at)) {
                ComponentType ct = new ComponentType(t.getId());
                ct.setDescription(t.getDescription().getValue());
                try {
                    ct.getImplementations().addAll(getImplementations(DBLUtils.getJavaImplementations(t)));
                } catch (ArchitectureInstantiationException e) {
                    throw new ArchitectureInstantiationException("Java implementation of type "
                            + t.getDescription().getValue() + " lacks main class name");
                }
                List<Signature> signatures = getSignatures(DBLUtils.getSignatures(t));
                ct.getSignatures().addAll(signatures);
                if (t.getSubArchitecture() != null) {
                    ct.setSubArchitecture(getSubArchitecture(t, signatures));
                }
                types.put(ct.getId(), ct);
            }
            for (IConnectorType t : DBLUtils.getConnectorTypes(at)) {
                ConnectorType ct = new ConnectorType(t.getId());
                ct.setDescription(t.getDescription().getValue());
                try {
                    ct.getImplementations().addAll(getImplementations(DBLUtils.getJavaImplementations(t)));
                } catch (ArchitectureInstantiationException e) {
                    throw new ArchitectureInstantiationException("Java implementation of type "
                            + t.getDescription().getValue() + " lacks main class name");
                }
                List<Signature> signatures = getSignatures(DBLUtils.getSignatures(t));
                ct.getSignatures().addAll(signatures);
                if (t.getSubArchitecture() != null) {
                    ct.setSubArchitecture(getSubArchitecture(t, signatures));
                }
                types.put(ct.getId(), ct);
            }
        }
        return types;
    }

    /**
     * Get all implementations of the given java implementations.
     * 
     * @param javaImplementations
     * @return
     * @throws ArchitectureInstantiationException
     */
    private List<Implementation> getImplementations(Collection<IJavaImplementation> javaImplementations)
            throws ArchitectureInstantiationException {
        List<Implementation> elements = new ArrayList<>();
        for (IJavaImplementation impl : javaImplementations) {
            Implementation i = new Implementation();
            if (impl.getMainClass() != null) {
                IJavaClassFile classFile = impl.getMainClass();
                if (classFile.getJavaClassName() == null) {
                    throw new ArchitectureInstantiationException("Java implementation lacks main class name");
                }
                i.setMainClassName(classFile.getJavaClassName().getValue());
                if (classFile instanceof IJavaClassFileParams) {
                    for (IInitializationParameter p : DBLUtils.getInitParameters((IJavaClassFileParams) classFile)) {
                        i.getInitProperties().put(p.getName(), p.getValue());
                    }
                }
            }
            for (IJavaClassFile classFile : DBLUtils.getAuxClasses(impl)) {
                if (classFile.getJavaClassName() != null) {
                    i.getAuxClassNames().add(classFile.getJavaClassName().getValue());
                }
            }
            elements.add(i);
        }
        return elements;
    }

    /**
     * Get all signatures of the given {@link ISignature}s.
     * 
     * @param signatures
     * @return
     * @throws ArchitectureInstantiationException
     */
    private List<Signature> getSignatures(Collection<ISignature> signatures) throws ArchitectureInstantiationException {
        List<Signature> elements = new ArrayList<>();
        for (ISignature sig : signatures) {
            Signature s = new Signature(sig.getId());
            s.setDescription(sig.getDescription().getValue());
            if (s.getDirection() != null) {
                EMyxInterfaceDirection direction = DBLUtils.getDirection(sig);
                if (direction == null) {
                    throw new ArchitectureInstantiationException("Signature " + s
                            + " has an invalid (non in/out) direction.");
                }
                s.setDirection(direction);
            }
            for (ILookupImplementation li : DBLUtils.getLookupImplementations(sig)) {
                if (li.getName() != null) {
                    s.getLookupImplementations().add(li.getName().getValue());
                }
            }
            elements.add(s);
        }
        return elements;
    }

    /**
     * Get the subarchitecture of a connector or component if one exists.
     * 
     * @param element
     * @param signatures
     * @return
     * @throws ArchitectureInstantiationException
     */
    private SubArchitecture getSubArchitecture(IXArchElement element, List<Signature> signatures)
            throws ArchitectureInstantiationException {
        String elementDescription = DBLUtils.getDescription(element);
        ISubArchitecture subArch = DBLUtils.getSubArchitecture(element);
        if (subArch != null) {
            IXMLLink archStructureRef = subArch.getArchStructure();
            if (archStructureRef == null) {
                throw new ArchitectureInstantiationException("No ArchStructure link found for subarchitecture in "
                        + elementDescription);
            }
            String id = DBLUtils.getId(archStructureRef);
            IArchStructure innerArchStructure = DBLUtils.getArchStructure(modelRoot.getArchitectureRoot(), id);
            if (innerArchStructure == null) {
                throw new ArchitectureInstantiationException("ArchStructure " + id
                        + " not found for subarchitecture in " + elementDescription);
            }
            SubArchitecture sa = new SubArchitecture(id, innerArchStructure);
            sa.setDescription(innerArchStructure.getDescription().getValue());
            for (ISignatureInterfaceMapping m : DBLUtils.getSignatureInterfaceMappings(subArch)) {
                String signatureId = DBLUtils.getId(m.getOuterSignature());
                Signature outer = null;
                for (Signature s : signatures) {
                    if (s.getId() == signatureId) {
                        outer = s;
                    }
                }
                if (outer == null) {
                    throw new ArchitectureInstantiationException(
                            "Invalid or missing outer signature link on signature-interface-mapping in "
                                    + elementDescription);
                }

                String interfaceId = DBLUtils.getId(m.getInnerInterface());
                IInterface iInner = DBLUtils.getInterface(innerArchStructure, interfaceId);
                if (iInner == null) {
                    throw new ArchitectureInstantiationException(
                            "Invalid or missing inner interface link on signature-interface-mapping in "
                                    + elementDescription);
                }
                Interface inner = null;
                IXArchElement parent = DBLUtils.getParentOfInterface(innerArchStructure, iInner);
                if (parent == null) {
                    throw new ArchitectureInstantiationException("Invalid or missing parent on interface "
                            + iInner.getDescription().getValue());
                }
                String parentId = DBLUtils.getId(parent);
                if (parentId == DBLUtils.getId(element)) {
                    throw new ArchitectureInstantiationException("Can't apply mapping on duplucate component in "
                            + elementDescription);
                }

                for (Interface i : getInterfaces(DBLUtils.getInterfaces(parent))) {
                    if (i.getId() == interfaceId) {
                        inner = i;
                    }
                }
                if (inner == null) {
                    throw new ArchitectureInstantiationException(
                            "Invalid or missing inner interface link on signature-interface-mapping in "
                                    + elementDescription);
                }

                Tuple<String, Interface> innerInterfaceData = new Tuple<>();
                innerInterfaceData.setFst(parentId);
                innerInterfaceData.setSnd(inner);

                sa.getInterfaceMapping().put(outer, innerInterfaceData);
            }
            return sa;
        }
        return null;
    }

    /**
     * Instantiate an {@link IArchStructure} and run it.
     * 
     * @param structure
     * @param path
     * @throws ArchitectureInstantiationException
     */
    private void instantiate(String name, IArchStructure structure, IMyxName[] path)
            throws ArchitectureInstantiationException {
        IMyxName containerName = MyxMonitoringUtils.createName(name);

        try {
            myx.addBrick(path, containerName, containerBrickDescription);
        } catch (MyxBrickLoadException e) {
            throw new ArchitectureInstantiationException("Myx cannot load brick", e);
        } catch (MyxBrickCreationException e) {
            throw new ArchitectureInstantiationException("Myx cannot create brick", e);
        }
        instantiate(structure, path);
    }

    /**
     * Instantiate an {@link IArchStructure} and run it.
     * 
     * @param structure
     * @param path
     * @throws ArchitectureInstantiationException
     */
    private void instantiate(IArchStructure structure, IMyxName[] path) throws ArchitectureInstantiationException {
        // get all elements
        List<InstantiationElement> bricks = getOrderedBricks(structure);
        // instantiate all elements so that their begin method may be called
        instantiate(bricks, path);
        // weld the links that needed for begin
        weldBeginLinks(bricks, path);
        // call begin on each brick
        begin(bricks, path);
    }

    /**
     * Instantiate the given {@link InstantiationElement}s.
     * 
     * @param bricks
     * @param path
     * @throws ArchitectureInstantiationException
     */
    private void instantiate(List<InstantiationElement> bricks, IMyxName[] path)
            throws ArchitectureInstantiationException {
        for (InstantiationElement brick : bricks) {
            instantiate(brick, path);
        }
    }

    /**
     * Instantiate an {@link InstantiationElement}.
     * 
     * @param element
     * @param path
     * @throws ArchitectureInstantiationException
     */
    private void instantiate(InstantiationElement element, IMyxName[] path) throws ArchitectureInstantiationException {
        IMyxName brickName = MyxMonitoringUtils.createName(element.getId());
        // we first validate if the elements type contains a subarchitecture
        if (element.hasSubArchitecture()) {
            SubArchitecture subArch = element.getSubarchitecture();

            List<IMyxName> innerPath = Arrays.asList(path);
            // add container name
            innerPath.add(brickName);
            // call instantiate for the archstructure for the subarchitecture
            instantiate(element.getId(), subArch.getArchStructure(), innerPath.toArray(new IMyxName[0]));
            // now we have to wire the interface mappings
            for (Interface outerIntf : element.getInterfaces()) {
                if (outerIntf.getSignature() == null
                        || !subArch.getInterfaceMapping().containsKey(outerIntf.getSignature())) {
                    throw new ArchitectureInstantiationException("There exists no interface mapping for interface "
                            + outerIntf.getDescription() + " in subArchitecture " + subArch.getDescription());
                }
                Tuple<String, Interface> m = subArch.getInterfaceMapping().get(outerIntf);

                IMyxName outerIntfName = MyxMonitoringUtils.createName(outerIntf.getName());
                IMyxName innerBrickName = MyxMonitoringUtils.createName(m.getFst());
                IMyxName innerIntfName = MyxMonitoringUtils.createName(m.getSnd().getName());
                IMyxInterfaceDescription intfDesc = new MyxJavaClassInterfaceDescription(outerIntf
                        .getImplementationMainClassNames().toArray(new String[0]));
                myx.addContainerInterface(path, brickName, outerIntfName, intfDesc, outerIntf.getDirection(),
                        innerBrickName, innerIntfName);
            }
        } else {
            // add the brick
            IMyxBrickDescription brickDescription = new MyxJavaClassBrickDescription(
                    element.getImplemenationInitProperties(), element.getImplementationMainClassName());
            try {
                myx.addBrick(path, brickName, brickDescription);
            } catch (MyxBrickLoadException e) {
                throw new ArchitectureInstantiationException("Myx cannot load brick", e);
            } catch (MyxBrickCreationException e) {
                throw new ArchitectureInstantiationException("Myx cannot create brick", e);
            }
            // add the interfaces
            for (Interface intf : element.getInterfaces()) {
                IMyxInterfaceDescription intfDesc = new MyxJavaClassInterfaceDescription(intf
                        .getImplementationMainClassNames().toArray(new String[0]));
                IMyxName intfName = MyxMonitoringUtils.createName(intf.getName());
                myx.addInterface(path, brickName, intfName, intfDesc, intf.getDirection());
            }
            // weld the init links
            weldLinks(element.getInitLinks(), path);
            // call init
            myx.init(path, brickName);
        }
    }

    /**
     * Weld the given links.
     * 
     * @param links
     * @param path
     */
    private void weldLinks(List<Link> links, IMyxName[] path) {
        for (Link link : links) {
            IMyxName fromBrickName = MyxMonitoringUtils.createName(link.getFromElement().getId());
            IMyxName fromInterfaceName = MyxMonitoringUtils.createName(link.getFromInterface().getName());
            IMyxName toBrickName = MyxMonitoringUtils.createName(link.getToElement().getId());
            IMyxName toInterfaceName = MyxMonitoringUtils.createName(link.getToInterface().getName());
            IMyxWeld weld = myx.createWeld(path, fromBrickName, fromInterfaceName, path, toBrickName, toInterfaceName);
            myx.addWeld(weld);
        }
    }

    /**
     * Get the bricks to instantiate in the correct order and with all needed
     * information.
     * 
     * @param structure
     * @return
     * @throws ArchitectureInstantiationException
     */
    private List<InstantiationElement> getOrderedBricks(IArchStructure structure)
            throws ArchitectureInstantiationException {
        Map<IXArchElement, InstantiationElement> bricks = new HashMap<>();
        Map<InstantiationElement, InitializationOrderInfo> dependencyInfos = new HashMap<>();

        // add all components
        for (IComponent ic : DBLUtils.getComponents(structure)) {
            Component c = new Component(ic.getId());
            c.setDescription(ic.getDescription().getValue());
            try {
                c.getImplementations().addAll(getImplementations(DBLUtils.getJavaImplementations(ic)));
            } catch (ArchitectureInstantiationException e) {
                throw new ArchitectureInstantiationException("Java implementation of compontent " + c
                        + " lacks main class name");
            }
            if (ic.getType() != null) {
                String id = DBLUtils.getId(ic.getType());
                if (!types.containsKey(id)) {
                    throw new ArchitectureInstantiationException("The type of component " + c + " does not exist.");
                }
                c.setType(types.get(id));
            }
            // add interfaces
            c.getInterfaces().addAll(getInterfaces(DBLUtils.getInterfaces(ic)));
            // save them for sorting, etc.
            bricks.put(ic, c);
            dependencyInfos.put(c, new InitializationOrderInfo(c));
        }
        // add all connectors
        for (IConnector ic : DBLUtils.getConnectors(structure)) {
            Connector c = new Connector(ic.getId());
            c.setDescription(ic.getDescription().getValue());
            try {
                c.getImplementations().addAll(getImplementations(DBLUtils.getJavaImplementations(ic)));
            } catch (ArchitectureInstantiationException e) {
                throw new ArchitectureInstantiationException("Java implementation of connector " + c
                        + " lacks main class name");
            }
            if (ic.getType() != null) {
                String id = DBLUtils.getId(ic.getType());
                if (!types.containsKey(id)) {
                    throw new ArchitectureInstantiationException("The type of connector " + c + " does not exist.");
                }
                c.setType(types.get(id));
            }
            // add interfaces
            c.getInterfaces().addAll(getInterfaces(DBLUtils.getInterfaces(ic)));
            // save them for sorting, etc.
            bricks.put(ic, c);
            dependencyInfos.put(c, new InitializationOrderInfo(c));
        }

        for (ILink link : DBLUtils.getLinks(structure)) {
            Link l = new Link(link.getId());
            l.setDescription(link.getDescription().getValue());

            // validate the link
            List<IPoint> points = new ArrayList<>(DBLUtils.getPoints(link));
            if (points.size() != 2) {
                throw new ArchitectureInstantiationException("Link " + link.getDescription().getValue()
                        + " must have exactly two points.");
            }

            // validate the points of the link
            InstantiationElement[] pBricks = new InstantiationElement[2];
            EMyxInterfaceDirection[] pDirections = new EMyxInterfaceDirection[2];
            String[] pServiceTypes = new String[2];
            for (int i = 0; i < 2; i++) {
                IPoint point = points.get(i);
                IXMLLink interfaceLink = point.getAnchorOnInterface();
                if (interfaceLink == null) {
                    throw new ArchitectureInstantiationException("Link " + link.getDescription().getValue()
                            + " has an invalid endpoint link.");
                }
                String interfaceId = DBLUtils.getId(interfaceLink);
                IInterface intf = DBLUtils.getInterface(structure, interfaceId);
                if (intf == null) {
                    throw new ArchitectureInstantiationException("Interface " + interfaceId + " not found.");
                }
                if (intf.getDirection() == null) {
                    throw new ArchitectureInstantiationException("Interface " + intf.getDescription().getValue()
                            + " has an invalid direction.");
                }
                pDirections[i] = DBLUtils.getDirection(intf);
                if (pDirections[i] == null) {
                    throw new ArchitectureInstantiationException("Interface " + intf.getDescription().getValue()
                            + " has an invalid (non in/out) direction.");
                }

                IXArchElement element = DBLUtils.getParentOfInterface(structure, intf);
                if (!bricks.containsKey(element)) {
                    throw new ArchitectureInstantiationException("Link " + link.getDescription().getValue()
                            + " points to a brick that is not in structure " + structure.getDescription().getValue()
                            + ".");
                }
                pBricks[i] = bricks.get(element);

                IXMLLink signatureLink = intf.getSignature();
                if (signatureLink != null) {
                    String signatureId = DBLUtils.getId(signatureLink);
                    ISignature signature = DBLUtils.getSignature(modelRoot.getArchitectureRoot(), signatureId);
                    if (signature == null) {
                        throw new ArchitectureInstantiationException("Signature " + signatureId + " on interface "
                                + intf.getDescription().getValue() + " not found.");
                    }
                    if (signature.getServiceType() != null) {
                        pServiceTypes[i] = signature.getServiceType().getValue();
                    }
                }

                if (pDirections[i] == EMyxInterfaceDirection.OUT) {
                    l.setFromElement(pBricks[i]);
                    l.setFromInterface(pBricks[i].getInterface(intf.getId()));
                } else {
                    l.setToElement(pBricks[i]);
                    l.setToInterface(pBricks[i].getInterface(intf.getId()));
                }
            }

            if ((pServiceTypes[0] != null || pServiceTypes[1] != null) && pServiceTypes[0].equals(pServiceTypes[1])) {
                throw new ArchitectureInstantiationException("Link " + link.getDescription().getValue()
                        + " must have compatible signature service types.");
            }

            for (int i = 0; i < 2; i++) {
                InstantiationElement curr = pBricks[i], other = pBricks[1 - i];
                InitializationOrderInfo currInfo = dependencyInfos.get(curr);
                InitializationOrderInfo otherInfo = dependencyInfos.get(other);

                if (pServiceTypes[i] == "required") {
                    if (pDirections[i] == EMyxInterfaceDirection.OUT) {
                        currInfo.dependencies.add(other);
                        otherInfo.dependents.add(curr);
                        curr.getInitLinks().add(l);
                        break;
                    }
                } else if (pServiceTypes[i] == "provides") {
                    if (pDirections[i] == EMyxInterfaceDirection.IN) {
                        currInfo.dependents.add(other);
                        otherInfo.dependencies.add(curr);
                        curr.getInitLinks().add(l);
                        break;
                    }
                } else if (i == 1) {
                    if (pDirections[i] == EMyxInterfaceDirection.OUT) {
                        currInfo.dependencies.add(other);
                        otherInfo.dependents.add(curr);
                        curr.getBeginLinks().add(l);
                    } else {
                        currInfo.dependents.add(other);
                        otherInfo.dependencies.add(curr);
                        other.getBeginLinks().add(l);
                    }
                }
            }
        }

        List<InstantiationElement> sortedObjects = new ArrayList<>();

        Collection<InstantiationElement> independentNodes = new HashSet<>();
        for (InitializationOrderInfo idi : dependencyInfos.values()) {
            if (idi.dependencies.isEmpty()) {
                independentNodes.add(idi.element);
            }
        }

        // get the elements in the correct order
        while (!independentNodes.isEmpty()) {
            InstantiationElement n = independentNodes.iterator().next();
            InitializationOrderInfo nInfo = dependencyInfos.remove(n);
            independentNodes.remove(n);
            sortedObjects.add(n);
            for (InstantiationElement m : nInfo.dependents) {
                InitializationOrderInfo mInfo = dependencyInfos.get(m);
                mInfo.dependencies.remove(n);
                if (mInfo.dependencies.isEmpty()) {
                    independentNodes.add(m);
                }
            }
        }
        for (InitializationOrderInfo di : dependencyInfos.values()) {
            // if we have still dependencies the graph contains a cycle, but we
            // ignore it if we have only
            if ((!di.dependencies.isEmpty() || !di.dependents.isEmpty()) && !di.element.getInitLinks().isEmpty()) {
                throw new ArchitectureInstantiationException("Structure " + structure.getDescription().getValue()
                        + " contains a dependency cycle.");
            } else {
                sortedObjects.add(di.element);
            }
        }

        return sortedObjects;
    }

    /**
     * Get the {@link Interface}s of the given collection.
     * 
     * @param ic
     * @throws ArchitectureInstantiationException
     */
    private List<Interface> getInterfaces(Collection<IInterface> interfaces) throws ArchitectureInstantiationException {
        List<Interface> elements = new ArrayList<>();
        for (IInterface i : interfaces) {
            Interface intf;

            if (i.getDirection() == null) {
                throw new ArchitectureInstantiationException("Interface " + i.getDescription().getValue()
                        + " has an invalid direction.");
            }
            EMyxInterfaceDirection direction = DBLUtils.getDirection(i);
            if (direction == null) {
                throw new ArchitectureInstantiationException("Interface " + i.getDescription().getValue()
                        + " has an invalid (non in/out) direction.");
            }

            intf = new Interface(i.getId(), direction);
            intf.setDescription(i.getDescription().getValue());

            if (i.getType() != null) {
                String id = DBLUtils.getId(i.getType());
                if (!interfaceTypes.containsKey(id)) {
                    throw new ArchitectureInstantiationException("The type of interface " + intf + " does not exist.");
                }
                intf.setType(interfaceTypes.get(id));
            }

            if (i.getSignature() != null) {
                String id = DBLUtils.getId(i.getSignature());
                if (!signatures.containsKey(id)) {
                    throw new ArchitectureInstantiationException("The signature " + id + " of interface " + intf
                            + " does not exist.");
                }
                intf.setSignature(signatures.get(id));
            }

            elements.add(intf);
        }
        return elements;
    }

    /**
     * Connect all bricks so that their begin method might get called.
     * 
     * @param bricks
     * @param path
     */
    private void weldBeginLinks(List<InstantiationElement> bricks, IMyxName[] path) {
        for (InstantiationElement brick : bricks) {
            weldLinks(brick.getBeginLinks(), path);
        }
    }

    /**
     * Call the begin method for each given brick.
     * 
     * @param bricks
     * @param path
     */
    private void begin(List<InstantiationElement> bricks, IMyxName[] path) {
        for (InstantiationElement brick : bricks) {
            IMyxName brickName = MyxMonitoringUtils.createName(brick.getId());
            myx.begin(path, brickName);
        }
    }

    private class InitializationOrderInfo {
        public InstantiationElement element;

        public Set<InstantiationElement> dependents = new HashSet<>();
        public Set<InstantiationElement> dependencies = new HashSet<>();

        public InitializationOrderInfo(InstantiationElement element) {
            this.element = element;
        }

        private List<String> getDescriptions(Collection<InstantiationElement> elements) {
            List<String> s = new ArrayList<String>();
            for (InstantiationElement e : elements) {
                s.add(e.getDescription());
            }
            return s;
        }

        private List<String> getDescriptionsOfLinks(Collection<Link> elements) {
            List<String> s = new ArrayList<String>();
            for (Link e : elements) {
                s.add(e.getDescription());
            }
            return s;
        }

        @Override
        public String toString() {
            String EOL = System.getProperty("line.separator");
            StringBuffer sb = new StringBuffer();
            sb.append("[").append(element.getId()).append("] ").append(element.getDescription());
            sb.append(" <- ").append(getDescriptions(dependents)).append(EOL);
            sb.append(" - init:  ").append(getDescriptionsOfLinks(element.getInitLinks())).append(EOL);
            sb.append(" - begin: ").append(getDescriptionsOfLinks(element.getBeginLinks())).append(EOL);
            return sb.toString();
        }
    }
}
