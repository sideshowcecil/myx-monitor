package at.ac.tuwien.dsg.myx.monitor.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchImplementation;
import edu.uci.isr.xarch.XArchParseException;
import edu.uci.isr.xarch.XArchSerializeException;
import edu.uci.isr.xarch.XArchUtils;

public class ModelRootImpl implements ModelRoot {

    protected IXArchImplementation xArchImpl;
    protected IXArch xArchRoot;

    public ModelRootImpl() {
        xArchImpl = XArchUtils.getDefaultXArchImplementation();
    }

    public ModelRootImpl(String xadlFile) {
        super();
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
    public IXArch getArchitectureRoot() {
        return xArchRoot;
    }

}
