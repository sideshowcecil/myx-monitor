package at.ac.tuwien.dsg.myx.monitor.aggregator.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import at.ac.tuwien.dsg.myx.util.DBLUtils;
import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchImplementation;
import edu.uci.isr.xarch.XArchParseException;
import edu.uci.isr.xarch.XArchSerializeException;
import edu.uci.isr.xarch.XArchUtils;
import edu.uci.isr.xarch.hostproperty.IHostedArchInstance;
import edu.uci.isr.xarch.hostproperty.IHostedArchStructure;
import edu.uci.isr.xarch.hostproperty.IHostpropertyContext;
import edu.uci.isr.xarch.instance.IArchInstance;
import edu.uci.isr.xarch.instance.IInstanceContext;
import edu.uci.isr.xarch.instancemapping.IInstancemappingContext;
import edu.uci.isr.xarch.types.IArchStructure;
import edu.uci.isr.xarch.types.ITypesContext;
import edu.uci.isr.xarch.typesmapping.ITypesmappingContext;

public class ModelRootImpl implements ModelRoot {

    protected IXArchImplementation xArchImpl;
    protected IXArch xArchRoot;

    protected IInstanceContext instanceContext;
    protected IInstancemappingContext instanceMappingContext;
    protected ITypesContext typesContext;
    protected ITypesmappingContext typesMappingContext;
    protected IHostpropertyContext hostpropertyContext;

    public ModelRootImpl() {
        xArchImpl = XArchUtils.getDefaultXArchImplementation();
    }

    public ModelRootImpl(String xadlFile) {
        this();
        parse(xadlFile);
    }

    @Override
    public void parse(String xadlFile) {
        try {
            FileReader documentSource = new FileReader(new File(xadlFile));
            xArchRoot = xArchImpl.parse(documentSource);
            instanceContext = (IInstanceContext) xArchImpl.createContext(xArchRoot, "instance");
            instanceMappingContext = (IInstancemappingContext) xArchImpl.createContext(xArchRoot, "instancemapping");
            typesContext = (ITypesContext) xArchImpl.createContext(xArchRoot, "types");
            typesMappingContext = (ITypesmappingContext) xArchImpl.createContext(xArchRoot, "typesmapping");
            hostpropertyContext = (IHostpropertyContext) xArchImpl.createContext(xArchRoot, "hostproperty");
        } catch (XArchParseException | FileNotFoundException e) {
            throw new IllegalArgumentException("There was an error while parsing the given xadl file", e);
        }
    }

    @Override
    public void save(String xadlFile) {
        if (xArchRoot != null) {
            synchronized (xArchRoot) {
                FileWriter fr = null;
                try {
                    fr = new FileWriter(new File(xadlFile));
                    fr.write(xArchImpl.serialize(xArchRoot, null));
                    fr.close();
                } catch (IOException | XArchSerializeException e) {
                    throw new IllegalArgumentException("There was an error while saving the architecture", e);
                } finally {
                    if (fr != null) {
                        try {
                            fr.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }
    }

    @Override
    public IInstanceContext getInstanceContext() {
        return instanceContext;
    }

    @Override
    public IInstancemappingContext getInstanceMappingContext() {
        return instanceMappingContext;
    }

    @Override
    public ITypesContext getTypesContext() {
        return typesContext;
    }

    @Override
    public ITypesmappingContext getTypesMappingContext() {
        return typesMappingContext;
    }

    @Override
    public IHostpropertyContext getHostpropertyContext() {
        return hostpropertyContext;
    }

    @Override
    public Collection<IArchStructure> getArchStructures() {
        return DBLUtils.getArchStructures(xArchRoot);
    }

    @Override
    public IArchStructure getArchStructure(String id) {
        synchronized (xArchRoot) {
            IArchStructure archStructure = DBLUtils.getArchStructure(xArchRoot, id);
            if (archStructure == null) {
                archStructure = getTypesContext().createArchStructureElement();
                archStructure.setId(id);
                archStructure.setDescription(DBLUtils.createDescription(id, getTypesContext()));
                xArchRoot.addObject(archStructure);
            }
            return archStructure;
        }
    }

    @Override
    public IHostedArchStructure getHostedArchStructure(String id) {
        synchronized (xArchRoot) {
            IArchStructure archStructure = getArchStructure(id);
            if (archStructure instanceof IHostedArchStructure) {
                return (IHostedArchStructure) archStructure;
            }
            return getHostpropertyContext().promoteToHostedArchStructure(archStructure);
        }
    }

    @Override
    public IArchInstance getArchInstance(String id) {
        synchronized (xArchRoot) {
            IArchInstance archInstance = DBLUtils.getArchInstance(xArchRoot, id);
            if (archInstance == null) {
                archInstance = getInstanceContext().createArchInstanceElement();;
                archInstance.setId(id);
                archInstance.setDescription(DBLUtils.createDescription(id, getInstanceContext()));
                xArchRoot.addObject(archInstance);
            }
            return archInstance;
        }
    }

    @Override
    public IHostedArchInstance getHostedArchInstance(String id) {
        synchronized (xArchRoot) {
            IArchInstance archInstance = getArchInstance(id);
            if (archInstance instanceof IHostedArchInstance) {
                return (IHostedArchInstance) archInstance;
            }
            return getHostpropertyContext().promoteToHostedArchInstance(archInstance);
        }
    }

}
