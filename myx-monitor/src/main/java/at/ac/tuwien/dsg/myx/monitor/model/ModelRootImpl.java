package at.ac.tuwien.dsg.myx.monitor.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchImplementation;
import edu.uci.isr.xarch.XArchParseException;
import edu.uci.isr.xarch.XArchUtils;

public class ModelRootImpl implements ModelRoot {

    protected IXArchImplementation xArchImpl;
    protected IXArch xArchRoot;

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
        } catch (XArchParseException | FileNotFoundException e) {
            throw new IllegalArgumentException("There was an error while parsing the given xadl file", e);
        }
    }

    @Override
    public IXArch getArchitectureRoot() {
        return xArchRoot;
    }

}
