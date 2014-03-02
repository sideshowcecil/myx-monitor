package at.ac.tuwien.dsg.myx.monitor.aim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.dsg.myx.MyxUtils;
import at.ac.tuwien.dsg.myx.DBLUtils;
import at.ac.tuwien.dsg.myx.monitor.model.ModelRoot;
import edu.uci.isr.myx.fw.EMyxInterfaceDirection;
import edu.uci.isr.myx.fw.IMyxBrickDescription;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.IMyxRuntime;
import edu.uci.isr.myx.fw.MyxBrickCreationException;
import edu.uci.isr.myx.fw.MyxBrickLoadException;
import edu.uci.isr.xarch.IXArchElement;
import edu.uci.isr.xarch.instance.IDirection;
import edu.uci.isr.xarch.instance.IPoint;
import edu.uci.isr.xarch.instance.IXMLLink;
import edu.uci.isr.xarch.types.IArchStructure;
import edu.uci.isr.xarch.types.IComponent;
import edu.uci.isr.xarch.types.IConnector;
import edu.uci.isr.xarch.types.IInterface;
import edu.uci.isr.xarch.types.ILink;
import edu.uci.isr.xarch.types.ISignature;

public class LauncherImpl implements Launcher {

    protected IMyxRuntime myx;
    protected ModelRoot modelRoot;

    protected IMyxBrickDescription containerBrickDescription;

    public LauncherImpl(IMyxRuntime myx, ModelRoot modelRoot) {
        this.myx = myx;
        this.modelRoot = modelRoot;
        containerBrickDescription = MyxUtils.getContainerBrickDescription();
    }

    @Override
    public void instantiate(String name, IArchStructure structure) throws ArchitectureInstantiationException {
        IMyxName containerName = MyxUtils.createName(name);

        try {
            myx.addBrick(new IMyxName[0], containerName, containerBrickDescription);
            List<InitializationOrderInfo> order = getBrickInstantiationOrder(structure);
            System.out.println(order);
        } catch (MyxBrickLoadException e) {
            throw new ArchitectureInstantiationException("Myx cannot load brick", e);
        } catch (MyxBrickCreationException e) {
            throw new ArchitectureInstantiationException("Myx cannot create brick", e);
        }
    }

    private List<InitializationOrderInfo> getBrickInstantiationOrder(IArchStructure structure)
            throws ArchitectureInstantiationException {
        Set<IXArchElement> bricks = new HashSet<>();
        bricks.addAll(DBLUtils.getComponents(structure));
        bricks.addAll(DBLUtils.getConnectors(structure));

        Map<IXArchElement, InitializationOrderInfo> dependencyInfos = new HashMap<>();
        for (IXArchElement elem : bricks) {
            dependencyInfos.put(elem, new InitializationOrderInfo(elem));
        }

        List<ILink> unknownLinks = new ArrayList<>();
        for (ILink link : DBLUtils.getLinks(structure)) {
            List<IPoint> points = new ArrayList<>(DBLUtils.getPoints(link));
            if (points.size() != 2) {
                throw new ArchitectureInstantiationException("Link " + link.getDescription().getValue()
                        + " must have exactly two points.");
            }

            IXArchElement[] pBricks = new IXArchElement[2];
            EMyxInterfaceDirection[] pDirections = new EMyxInterfaceDirection[2];
            String[] pServiceTypes = new String[2];
            int i = 0;
            for (IPoint point : points) {
                IXMLLink interfaceLink = point.getAnchorOnInterface();
                if (interfaceLink == null) {
                    throw new ArchitectureInstantiationException("Link " + link.getDescription().getValue()
                            + " has an invalid endpoint link.");
                }
                // we remove the # from the id
                String interfaceId = interfaceLink.getHref().substring(1);
                IInterface iface = DBLUtils.getInterface(structure, interfaceId);
                if (iface == null) {
                    throw new ArchitectureInstantiationException("Interface " + interfaceId + " not found.");
                }
                IDirection direction = iface.getDirection();
                if (direction == null) {
                    throw new ArchitectureInstantiationException("Interface " + iface.getDescription().getValue()
                            + " has an invalid direction.");
                }
                switch (direction.getValue()) {
                case "in":
                    pDirections[i] = EMyxInterfaceDirection.IN;
                    break;
                case "out":
                    pDirections[i] = EMyxInterfaceDirection.OUT;
                    break;
                default:
                    throw new ArchitectureInstantiationException("Interface " + iface.getDescription().getValue()
                            + " has an invalid (non in/out) direction.");
                }

                IXArchElement element = DBLUtils.getParentOfInterface(structure, iface);
                if (!bricks.contains(element)) {
                    throw new ArchitectureInstantiationException("Link " + link.getDescription().getValue()
                            + " points to a brick that is not in structure " + structure.getDescription().getValue()
                            + ".");
                }
                pBricks[i] = element;

                IXMLLink signatureLink = iface.getSignature();
                if (signatureLink == null) {
                    throw new ArchitectureInstantiationException("Missing signature on interface "
                            + iface.getDescription().getValue() + ".");
                }
                // we remove the # from the id
                String signatureId = signatureLink.getHref().substring(1);
                ISignature signature = DBLUtils.getSignature(modelRoot.getArchitectureRoot(), signatureId);
                if (signature == null) {
                    throw new ArchitectureInstantiationException("Signature " + signatureId + " not found.");
                }
                if (signature.getServiceType() != null) {
                    pServiceTypes[i] = signature.getServiceType().getValue();
                }

                i++;
            }

            if ((pServiceTypes[0] != null || pServiceTypes[1] != null) && pServiceTypes[0].equals(pServiceTypes[1])) {
                throw new ArchitectureInstantiationException("Link " + link.getDescription().getValue()
                        + " must have compatible signature service types.");
            }

            for (i = 0; i < 2; i++) {
                InitializationOrderInfo di1 = dependencyInfos.get(pBricks[i]);
                InitializationOrderInfo di2 = dependencyInfos.get(pBricks[1 - i]);

                if (pServiceTypes[i] == "required") {
                    di1.dependencies.add(pBricks[1 - i]);
                    if (pDirections[i] == EMyxInterfaceDirection.OUT) {
                        di1.initLinks.add(link);
                    } else {
                        di1.beginLinks.add(link);
                    }
                    di1.dependents.add(pBricks[i]);
                    di1.laterLinks.add(link);
                    break;
                } else if (pServiceTypes[i] == "provides") {
                    di1.dependents.add(pBricks[1 - i]);
                    di1.laterLinks.add(link);
                    di2.dependencies.add(pBricks[i]);
                    if (pDirections[1 - i] == EMyxInterfaceDirection.OUT) {
                        di2.initLinks.add(link);
                    } else {
                        di2.beginLinks.add(link);
                    }
                    break;
                }

                if (i == 1) {
                    unknownLinks.add(link);
                }
            }
        }

        List<InitializationOrderInfo> sortedObjects = new ArrayList<>();
        // q = independent nodes
        // n = independent node
        // m = a node that n depends on
        Collection<IXArchElement> q = new HashSet<>();
        for (InitializationOrderInfo di : dependencyInfos.values()) {
            if (di.dependencies.isEmpty()) {
                q.add(di.element);
            }
        }
        while (!q.isEmpty()) {
            IXArchElement n = q.iterator().next();
            InitializationOrderInfo ndi = dependencyInfos.remove(n);
            q.remove(n);
            ndi.overallOrder = sortedObjects.size();
            for (IXArchElement m : ndi.dependents) {
                InitializationOrderInfo mdi = dependencyInfos.get(m);
                mdi.dependencies.remove(n);
                if (mdi.dependencies.isEmpty()) {
                    q.add(m);
                }
            }
        }
        for (InitializationOrderInfo di : dependencyInfos.values()) {
            if (!di.dependents.isEmpty() || !di.dependencies.isEmpty()) {
                List<String> cycle = new ArrayList<String>();
                do {
                    if (di.element instanceof IComponent) {
                        cycle.add(((IComponent) di.element).getDescription().getValue());
                    } else {
                        cycle.add(((IConnector) di.element).getDescription().getValue());
                    }
                    if (!di.dependents.isEmpty()) {
                        di = dependencyInfos.get(di.dependents.iterator().next());
                    } else {
                        di = null;
                    }
                } while (di != null);

                throw new ArchitectureInstantiationException("Structure " + structure.getDescription().getValue()
                        + " contains a dependency cycle " + cycle + ".");
            }
        }

        dependencyInfos.clear();
        for (InitializationOrderInfo di : sortedObjects) {
            dependencyInfos.put(di.element, di);
        }

        for (ILink link : unknownLinks) {
            IPoint[] points = DBLUtils.getPoints(link).toArray(new IPoint[0]);
            IXArchElement[] pBricks = new IXArchElement[2];
            InitializationOrderInfo[] pdi = new InitializationOrderInfo[2];
            for (int i = 0; i < points.length; i++) {
                IInterface iface = DBLUtils.getInterface(structure,
                        DBLUtils.getInterface(structure, points[i].getAnchorOnInterface().getHref().substring(1))
                                .getId());
                pBricks[i] = DBLUtils.getParentOfInterface(structure, iface);
                pdi[i] = dependencyInfos.get(pBricks[i]);
            }
            if (pdi[0].overallOrder < pdi[1].overallOrder) {
                pdi[1].beginLinks.add(link);
                pdi[0].laterLinks.add(link);
            } else {
                pdi[0].beginLinks.add(link);
                pdi[1].laterLinks.add(link);
            }
        }

        return sortedObjects;
    }

    private class InitializationOrderInfo {
        public IXArchElement element;
        public Collection<ILink> initLinks = new HashSet<>();
        public Collection<ILink> beginLinks = new HashSet<>();
        public Collection<ILink> laterLinks = new HashSet<>();

        // these are only valid while sorting topologically
        public int overallOrder = 0;
        public Set<IXArchElement> dependents = new HashSet<>();
        public Set<IXArchElement> dependencies = new HashSet<>();

        public InitializationOrderInfo(IXArchElement element) {
            this.element = element;
        }
    }
}
