package at.ac.tuwien.dsg.myx.monitor.aggregator.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import at.ac.tuwien.dsg.myx.util.DBLUtils;
import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchImplementation;
import edu.uci.isr.xarch.XArchParseException;
import edu.uci.isr.xarch.XArchSerializeException;
import edu.uci.isr.xarch.XArchUtils;
import edu.uci.isr.xarch.hostproperty.IHostedArchStructure;
import edu.uci.isr.xarch.hostproperty.IHostpropertyContext;
import edu.uci.isr.xarch.types.IArchStructure;
import edu.uci.isr.xarch.types.ITypesContext;

public class ModelRootImpl implements ModelRoot {

    protected IXArchImplementation xArchImpl;
    protected IXArch xArchRoot;

    protected ITypesContext typesContext;
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
            typesContext = (ITypesContext) xArchImpl.createContext(xArchRoot, "types");
            hostpropertyContext = (IHostpropertyContext) xArchImpl.createContext(xArchRoot, "hostproperty");
        } catch (XArchParseException | FileNotFoundException e) {
            throw new IllegalArgumentException("There was an error while parsing the given xadl file", e);
        }
    }

    @Override
    public void save(String xadlFile) {
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

    @Override
    public ITypesContext getTypesContext() {
        return typesContext;
    }

    @Override
    public IHostpropertyContext getHostpropertyContext() {
        return hostpropertyContext;
    }

    @Override
    public IArchStructure getArchStructure(String id) {
        synchronized (xArchRoot) {
            IArchStructure archStructure = DBLUtils.getArchStructure(xArchRoot, id);
            if (archStructure == null) {
                archStructure = getTypesContext().createArchStructureElement();
                archStructure.setId(id);
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

}
