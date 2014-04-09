package at.ac.tuwien.dsg.pubsub.middleware.comp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import at.ac.tuwien.dsg.myx.fw.MyxJavaClassInitPropertiesInterfaceDescription;
import at.ac.tuwien.dsg.myx.monitor.AbstractMyxMonitoringRuntimeAdapter;
import at.ac.tuwien.dsg.myx.monitor.MyxProperties;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLElementType;
import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.IMyxRuntimeAdapter;
import at.ac.tuwien.dsg.pubsub.middleware.myx.DynamicArchitectureModelProperties;
import at.ac.tuwien.dsg.pubsub.middleware.myx.MessageDistributor;
import at.ac.tuwien.dsg.pubsub.middleware.myx.MyxInterfaceNames;
import edu.uci.isr.myx.fw.EMyxInterfaceDirection;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.IMyxWeld;
import edu.uci.isr.myx.fw.MyxBrickCreationException;
import edu.uci.isr.myx.fw.MyxBrickLoadException;
import edu.uci.isr.myx.fw.MyxJavaClassBrickDescription;
import edu.uci.isr.myx.fw.MyxJavaClassInterfaceDescription;
import edu.uci.isr.myx.fw.MyxUtils;

public class MyxRuntimeAdapter extends AbstractMyxMonitoringRuntimeAdapter implements IMyxRuntimeAdapter {

    public static final IMyxName IN_IMYX_ADAPTER = MyxInterfaceNames.IMYX_ADAPTER;

    public static final IMyxName[] PATH = null;

    private IMyxName messageDistributorRuntimeId = null;

    private Map<IMyxName, List<IMyxName>> component2Interfaces = new HashMap<>();
    private Map<IMyxName, List<IMyxWeld>> component2Welds = new HashMap<>();

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        if (interfaceName.equals(IN_IMYX_ADAPTER)) {
            return this;
        }
        return null;
    }

    /**
     * Get the runtime id of the {@link MessageDistributor}.
     * 
     * @return
     */
    private void fetchMessageDistributorRuntimeId() {
        if (messageDistributorRuntimeId == null) {
            synchronized (this) {
                for (IMyxName brickName : getMyxRuntime().getAllBrickNames(null)) {
                    if (brickName.getName().startsWith(
                            DynamicArchitectureModelProperties.MESSAGE_DISTRIBUTOR_BLUEPRINT_ID)) {
                        messageDistributorRuntimeId = brickName;
                        return;
                    }
                }
                throw new RuntimeException("No MessageDistributor found");
            }
        }
    }

    /**
     * Remove a component from the architecture.
     * 
     * @param brickName
     */
    private void removeComponent(IMyxName brickName) {
        synchronized (this) {
            if (component2Interfaces.containsKey(brickName)) {
                // call the specific myx methods
                getMyxRuntime().end(PATH, brickName);
                getMyxRuntime().destroy(PATH, brickName);

                // remove welds
                for (IMyxWeld weld : component2Welds.get(brickName)) {
                    try {
                        getMyxRuntime().removeWeld(weld);
                    } catch (Exception e) {
                    }
                }

                // remove interfaces
                for (IMyxName interfaceName : component2Interfaces.get(brickName)) {
                    try {
                        getMyxRuntime().removeInterface(PATH, brickName, interfaceName);
                    } catch (Exception e) {
                    }
                }

                // remove the brick itself
                getMyxRuntime().removeBrick(PATH, brickName);

                // remove the brick from all collections
                component2Interfaces.remove(brickName);
                component2Welds.remove(brickName);
            }
        }
    }

    @Override
    public void createPublisherEndpoint(String publisherEndpointClassName, Dispatcher<?> dispatcher) {
        fetchMessageDistributorRuntimeId();

        try {
            Class<?> pubEndClass = Class.forName(publisherEndpointClassName);
            if (!PublisherEndpoint.class.isAssignableFrom(pubEndClass)) {
                throw new RuntimeException("Class " + publisherEndpointClassName + " is not a subclass of "
                        + PublisherEndpoint.class.getName());
            }
        } catch (ClassNotFoundException e1) {
            throw new RuntimeException("Class " + publisherEndpointClassName + " does not exist", e1);
        }

        // class description
        Properties initProps = new Properties();
        initProps.put(MyxProperties.ARCHITECTURE_BLUEPRINT_ID,
                DynamicArchitectureModelProperties.PUBLISHER_ENDPOINT_BLUEPRINT_ID);
        initProps.put(MyxProperties.ARCHITECTURE_BRICK_TYPE, XADLElementType.CONNECTOR);
        MyxJavaClassBrickDescription publisherEndpointDesc = new MyxJavaClassBrickDescription(initProps,
                publisherEndpointClassName);

        // interface descriptions
        Properties dispatcherInitProps = new Properties();
        dispatcherInitProps.put(MyxProperties.ARCHITECTURE_INTERFACE_TYPE,
                DynamicArchitectureModelProperties.DISPATCHER_INTERFACE_TYPE);
        MyxJavaClassInterfaceDescription dispatcherDesc = new MyxJavaClassInitPropertiesInterfaceDescription(
                new String[] { MyxInterfaceNames.IDISPATCHER.getName() }, dispatcherInitProps);
        Properties subscriberInitProps = new Properties();
        subscriberInitProps.put(MyxProperties.ARCHITECTURE_INTERFACE_TYPE,
                DynamicArchitectureModelProperties.SUBSCRIBER_INTERFACE_TYPE);
        MyxJavaClassInterfaceDescription subscriberDesc = new MyxJavaClassInitPropertiesInterfaceDescription(
                new String[] { MyxInterfaceNames.ISUBSCRIBER.getName() }, subscriberInitProps);
        Properties myxAdapterInitProps = new Properties();
        myxAdapterInitProps.put(MyxProperties.ARCHITECTURE_INTERFACE_TYPE,
                DynamicArchitectureModelProperties.MYX_ADAPTER_INTERFACE_TYPE);
        MyxJavaClassInterfaceDescription myxAdapterDesc = new MyxJavaClassInitPropertiesInterfaceDescription(
                new String[] { MyxInterfaceNames.IMYX_ADAPTER.getName() }, myxAdapterInitProps);
        // external interface description
        Properties virtualExternalInterfaceInitProps = new Properties();
        virtualExternalInterfaceInitProps.put(MyxProperties.ARCHITECTURE_INTERFACE_TYPE,
                DynamicArchitectureModelProperties.PUBLISHER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE);
        MyxJavaClassInterfaceDescription virtualExternalInterfaceDesc = new MyxJavaClassInitPropertiesInterfaceDescription(
                new String[0], virtualExternalInterfaceInitProps);

        // create name
        IMyxName publisherEndpoint = MyxMonitoringUtils.createName(IdGenerator
                .generateRuntimeInstantiationId(DynamicArchitectureModelProperties.PUBLISHER_ENDPOINT_BLUEPRINT_ID));

        // add the bricks
        try {
            getMyxRuntime().addBrick(PATH, publisherEndpoint, publisherEndpointDesc);
            component2Interfaces.put(publisherEndpoint, new ArrayList<IMyxName>());
            component2Welds.put(publisherEndpoint, new ArrayList<IMyxWeld>());
        } catch (MyxBrickLoadException | MyxBrickCreationException e) {
            throw new RuntimeException("Could not load brick", e);
        }

        // add interfaces to the component
        getMyxRuntime().addInterface(PATH, publisherEndpoint, MyxInterfaceNames.IDISPATCHER, dispatcherDesc,
                EMyxInterfaceDirection.OUT);
        component2Interfaces.get(publisherEndpoint).add(MyxInterfaceNames.IDISPATCHER);
        getMyxRuntime().addInterface(PATH, publisherEndpoint, MyxInterfaceNames.ISUBSCRIBER, subscriberDesc,
                EMyxInterfaceDirection.OUT);
        component2Interfaces.get(publisherEndpoint).add(MyxInterfaceNames.ISUBSCRIBER);
        getMyxRuntime().addInterface(PATH, publisherEndpoint, MyxInterfaceNames.IMYX_ADAPTER, myxAdapterDesc,
                EMyxInterfaceDirection.OUT);
        component2Interfaces.get(publisherEndpoint).add(MyxInterfaceNames.IMYX_ADAPTER);
        // external interface
        getMyxRuntime().addInterface(PATH, publisherEndpoint, MyxInterfaceNames.VIRTUAL_PUBLISHER_ENDPOINT,
                virtualExternalInterfaceDesc, EMyxInterfaceDirection.IN);
        component2Interfaces.get(publisherEndpoint).add(MyxInterfaceNames.VIRTUAL_PUBLISHER_ENDPOINT);

        // init
        getMyxRuntime().init(PATH, publisherEndpoint);

        // wire up the endpoint
        IMyxWeld pe2d = getMyxRuntime().createWeld(PATH, publisherEndpoint, MyxInterfaceNames.IDISPATCHER, PATH,
                MyxMonitoringUtils.getName(dispatcher), MyxInterfaceNames.IDISPATCHER);
        getMyxRuntime().addWeld(pe2d);
        component2Welds.get(publisherEndpoint).add(pe2d);
        IMyxWeld pe2md = getMyxRuntime().createWeld(PATH, publisherEndpoint, MyxInterfaceNames.ISUBSCRIBER, PATH,
                messageDistributorRuntimeId, MyxMonitoringUtils.createName("in"));
        getMyxRuntime().addWeld(pe2md);
        component2Welds.get(publisherEndpoint).add(pe2md);
        IMyxWeld pe2myx = getMyxRuntime().createWeld(PATH, publisherEndpoint, MyxInterfaceNames.IMYX_ADAPTER, PATH,
                MyxUtils.getName(this), MyxInterfaceNames.IMYX_ADAPTER);
        getMyxRuntime().addWeld(pe2myx);
        component2Welds.get(publisherEndpoint).add(pe2myx);

        // begin
        getMyxRuntime().begin(PATH, publisherEndpoint);
    }

    @Override
    public void shutdownPublisherEndpoint(PublisherEndpoint<?> endpoint) {
        removeComponent(MyxMonitoringUtils.getName(endpoint));
    }

    @Override
    public void createSubscriberEndpoint(String subscriberEndpointClassName, Dispatcher<?> dispatcher) {
        fetchMessageDistributorRuntimeId();

        try {
            Class<?> subEndClass = Class.forName(subscriberEndpointClassName);
            if (!SubscriberEndpoint.class.isAssignableFrom(subEndClass)) {
                throw new RuntimeException("Class " + subscriberEndpointClassName + " is not a subclass of "
                        + SubscriberEndpoint.class.getName());
            }
        } catch (ClassNotFoundException e1) {
            throw new RuntimeException("Class " + subscriberEndpointClassName + " does not exist", e1);
        }

        // class description
        Properties initProps = new Properties();
        initProps.put(MyxProperties.ARCHITECTURE_BLUEPRINT_ID,
                DynamicArchitectureModelProperties.SUBSCRIBER_ENDPOINT_BLUEPRINT_ID);
        initProps.put(MyxProperties.ARCHITECTURE_BRICK_TYPE, XADLElementType.CONNECTOR);
        MyxJavaClassBrickDescription subscriberEndpointDesc = new MyxJavaClassBrickDescription(initProps,
                subscriberEndpointClassName);

        // interface descriptions
        Properties dispatcherInitProps = new Properties();
        dispatcherInitProps.put(MyxProperties.ARCHITECTURE_INTERFACE_TYPE,
                DynamicArchitectureModelProperties.DISPATCHER_INTERFACE_TYPE);
        MyxJavaClassInterfaceDescription dispatcherDesc = new MyxJavaClassInitPropertiesInterfaceDescription(
                new String[] { MyxInterfaceNames.IDISPATCHER.getName() }, dispatcherInitProps);
        Properties subscriberInitProps = new Properties();
        subscriberInitProps.put(MyxProperties.ARCHITECTURE_INTERFACE_TYPE,
                DynamicArchitectureModelProperties.SUBSCRIBER_INTERFACE_TYPE);
        MyxJavaClassInterfaceDescription subscriberDesc = new MyxJavaClassInitPropertiesInterfaceDescription(
                new String[] { MyxInterfaceNames.ISUBSCRIBER.getName() }, subscriberInitProps);
        Properties myxAdapterInitProps = new Properties();
        myxAdapterInitProps.put(MyxProperties.ARCHITECTURE_INTERFACE_TYPE,
                DynamicArchitectureModelProperties.MYX_ADAPTER_INTERFACE_TYPE);
        MyxJavaClassInterfaceDescription myxAdapterDesc = new MyxJavaClassInitPropertiesInterfaceDescription(
                new String[] { MyxInterfaceNames.IMYX_ADAPTER.getName() }, myxAdapterInitProps);
        // external interface description
        Properties virtualExternalInterfaceInitProps = new Properties();
        virtualExternalInterfaceInitProps.put(MyxProperties.ARCHITECTURE_INTERFACE_TYPE,
                DynamicArchitectureModelProperties.SUBSCRIBER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE);
        MyxJavaClassInterfaceDescription virtualExternalInterfaceDesc = new MyxJavaClassInitPropertiesInterfaceDescription(
                new String[0], virtualExternalInterfaceInitProps);

        // create name
        IMyxName subscriberEndpoint = MyxMonitoringUtils.createName(IdGenerator
                .generateRuntimeInstantiationId(DynamicArchitectureModelProperties.SUBSCRIBER_ENDPOINT_BLUEPRINT_ID));

        // add the bricks
        try {
            getMyxRuntime().addBrick(PATH, subscriberEndpoint, subscriberEndpointDesc);
            component2Interfaces.put(subscriberEndpoint, new ArrayList<IMyxName>());
            component2Welds.put(subscriberEndpoint, new ArrayList<IMyxWeld>());
        } catch (MyxBrickLoadException | MyxBrickCreationException e) {
            throw new RuntimeException("Could not load brick", e);
        }

        // add interfaces to components
        getMyxRuntime().addInterface(PATH, subscriberEndpoint, MyxInterfaceNames.IDISPATCHER, dispatcherDesc,
                EMyxInterfaceDirection.OUT);
        component2Interfaces.get(subscriberEndpoint).add(MyxInterfaceNames.IDISPATCHER);
        getMyxRuntime().addInterface(PATH, subscriberEndpoint, MyxInterfaceNames.ISUBSCRIBER, subscriberDesc,
                EMyxInterfaceDirection.IN);
        component2Interfaces.get(subscriberEndpoint).add(MyxInterfaceNames.ISUBSCRIBER);
        getMyxRuntime().addInterface(PATH, subscriberEndpoint, MyxInterfaceNames.IMYX_ADAPTER, myxAdapterDesc,
                EMyxInterfaceDirection.OUT);
        component2Interfaces.get(subscriberEndpoint).add(MyxInterfaceNames.IMYX_ADAPTER);
        // external interface
        getMyxRuntime().addInterface(PATH, subscriberEndpoint, MyxInterfaceNames.VIRTUAL_SUBSCRIBER_ENDPOINT,
                virtualExternalInterfaceDesc, EMyxInterfaceDirection.OUT);
        component2Interfaces.get(subscriberEndpoint).add(MyxInterfaceNames.VIRTUAL_SUBSCRIBER_ENDPOINT);

        // init
        getMyxRuntime().init(PATH, subscriberEndpoint);

        // wire up the endpoint
        IMyxWeld se2d = getMyxRuntime().createWeld(PATH, subscriberEndpoint, MyxInterfaceNames.IDISPATCHER, PATH,
                MyxMonitoringUtils.getName(dispatcher), MyxInterfaceNames.IDISPATCHER);
        getMyxRuntime().addWeld(se2d);
        component2Welds.get(subscriberEndpoint).add(se2d);
        IMyxWeld se2myx = getMyxRuntime().createWeld(PATH, subscriberEndpoint, MyxInterfaceNames.IMYX_ADAPTER, PATH,
                MyxUtils.getName(this), MyxInterfaceNames.IMYX_ADAPTER);
        getMyxRuntime().addWeld(se2myx);
        component2Welds.get(subscriberEndpoint).add(se2myx);

        // begin
        getMyxRuntime().begin(PATH, subscriberEndpoint);
    }

    @Override
    public void wireSubscriberEndpoint(SubscriberEndpoint<?> subscriber) {
        IMyxName subscriberEndpoint = MyxMonitoringUtils.getName(subscriber);

        IMyxWeld md2se = getMyxRuntime().createWeld(PATH, messageDistributorRuntimeId,
                MyxMonitoringUtils.createName("out"), PATH, subscriberEndpoint, MyxInterfaceNames.ISUBSCRIBER);
        getMyxRuntime().addWeld(md2se);
        component2Welds.get(subscriberEndpoint).add(md2se);
    }

    @Override
    public void shutdownSubscriberEndpoint(SubscriberEndpoint<?> endpoint) {
        removeComponent(MyxMonitoringUtils.getName(endpoint));
    }

}
