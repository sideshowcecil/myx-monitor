package at.ac.tuwien.dsg.myx.monitor.aggregator.model;

import java.util.Collection;

import edu.uci.isr.xarch.hostproperty.IHostedArchInstance;
import edu.uci.isr.xarch.hostproperty.IHostedArchStructure;
import edu.uci.isr.xarch.hostproperty.IHostpropertyContext;
import edu.uci.isr.xarch.instance.IArchInstance;
import edu.uci.isr.xarch.instance.IInstanceContext;
import edu.uci.isr.xarch.instancemapping.IInstancemappingContext;
import edu.uci.isr.xarch.types.IArchStructure;
import edu.uci.isr.xarch.types.ITypesContext;
import edu.uci.isr.xarch.typesmapping.ITypesmappingContext;

public interface ModelRoot {
    /**
     * Parse the given xadl file.
     * 
     * @param xadlFile
     */
    public void parse(String xadlFile);

    /**
     * Save the current {@link ModelRoot} to the given xadl file.
     * 
     * @param xadlFile
     */
    public void save(String xadlFile);

    /**
     * Create an {@link IInstanceContext}.
     * 
     * @return
     */
    public IInstanceContext getInstanceContext();

    /**
     * Create an {@link IInstancemappingContext}.
     * 
     * @return
     */
    public IInstancemappingContext getInstanceMappingContext();

    /**
     * Create an {@link ITypesContext}.
     * 
     * @return
     */
    public ITypesContext getTypesContext();

    /**
     * Create an {@link ITypesmappingContext}.
     * 
     * @return
     */
    public ITypesmappingContext getTypesMappingContext();

    /**
     * Create an {@link IHostpropertyContext}.
     * 
     * @return
     */
    public IHostpropertyContext getHostpropertyContext();

    /**
     * Get all {@link IArchStructure}s.
     * 
     * @return
     */
    public Collection<IArchStructure> getArchStructures();

    /**
     * Get the {@link IArchStructure} with the given id.
     * 
     * @param id
     * @return
     */
    public IArchStructure getArchStructure(String id);

    /**
     * Get the {@link IHostedArchStructure} with the given id.
     * 
     * @param id
     * @return
     */
    public IHostedArchStructure getHostedArchStructure(String id);

    /**
     * Get the {@link IArchInstance} with the given id.
     * 
     * @param id
     * @return
     */
    public IArchInstance getArchInstance(String id);

    /**
     * Get the {@link IHostedArchInstance} with the given id.
     * 
     * @param id
     * @return
     */
    public IHostedArchInstance getHostedArchInstance(String id);
}
