package at.ac.tuwien.dsg.myx.monitor.aggregator.comp;

import java.util.ArrayList;
import java.util.Properties;

import at.ac.tuwien.dsg.myx.fw.MyxJavaClassInitPropertiesInterfaceDescription;
import at.ac.tuwien.dsg.myx.monitor.MyxProperties;
import at.ac.tuwien.dsg.myx.monitor.aggregator.myx.DynamicArchitectureModelProperties;
import at.ac.tuwien.dsg.myx.monitor.aggregator.myx.MyxInterfaceNames;
import at.ac.tuwien.dsg.myx.monitor.em.events.XADLElementType;
import at.ac.tuwien.dsg.myx.util.IdGenerator;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.middleware.comp.Dispatcher;
import at.ac.tuwien.dsg.pubsub.middleware.comp.SubscriberEndpoint;
import edu.uci.isr.myx.fw.EMyxInterfaceDirection;
import edu.uci.isr.myx.fw.IMyxInterfaceDescription;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.IMyxWeld;
import edu.uci.isr.myx.fw.MyxBrickCreationException;
import edu.uci.isr.myx.fw.MyxBrickLoadException;
import edu.uci.isr.myx.fw.MyxJavaClassBrickDescription;
import edu.uci.isr.myx.fw.MyxJavaClassInterfaceDescription;
import edu.uci.isr.myx.fw.MyxUtils;

public class MyxRuntimeAdapter extends at.ac.tuwien.dsg.pubsub.middleware.comp.MyxRuntimeAdapter {

    protected IMyxName modelRootRuntimeId = null;

    protected void fetchModelRootRuntimeId() {
        if (modelRootRuntimeId == null) {
            synchronized (this) {
                for (IMyxName brickName : getMyxRuntime().getAllBrickNames(null)) {
                    if (brickName.getName().startsWith(DynamicArchitectureModelProperties.MODEL_ROOT_BLUEPRINT_ID)) {
                        modelRootRuntimeId = brickName;
                        return;
                    }
                }
                throw new RuntimeException("No ModelRoot found");
            }
        }
    }

    @Override
    public void createSubscriberEndpoint(String subscriberEndpointClassName, Dispatcher<?> dispatcher) {
        fetchMessageDistributorRuntimeId();
        fetchModelRootRuntimeId();

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
        IMyxInterfaceDescription dispatcherDesc = new MyxJavaClassInitPropertiesInterfaceDescription(
                new String[] { MyxInterfaceNames.IDISPATCHER.getName() }, dispatcherInitProps);
        Properties subscriberInitProps = new Properties();
        subscriberInitProps.put(MyxProperties.ARCHITECTURE_INTERFACE_TYPE,
                DynamicArchitectureModelProperties.SUBSCRIBER_INTERFACE_TYPE);
        IMyxInterfaceDescription subscriberDesc = new MyxJavaClassInitPropertiesInterfaceDescription(
                new String[] { MyxInterfaceNames.ISUBSCRIBER.getName() }, subscriberInitProps);
        Properties myxAdapterInitProps = new Properties();
        myxAdapterInitProps.put(MyxProperties.ARCHITECTURE_INTERFACE_TYPE,
                DynamicArchitectureModelProperties.MYX_ADAPTER_INTERFACE_TYPE);
        IMyxInterfaceDescription myxAdapterDesc = new MyxJavaClassInitPropertiesInterfaceDescription(
                new String[] { MyxInterfaceNames.IMYX_ADAPTER.getName() }, myxAdapterInitProps);
        IMyxInterfaceDescription modelRootDesc = new MyxJavaClassInterfaceDescription(
                new String[] { MyxInterfaceNames.MODEL_ROOT.getName() });
        // external interface description
        Properties virtualExternalInterfaceInitProps = new Properties();
        virtualExternalInterfaceInitProps.put(MyxProperties.ARCHITECTURE_INTERFACE_TYPE,
                DynamicArchitectureModelProperties.SUBSCRIBER_ENDPOINT_VIRTUAL_EXTERNAL_INTERFACE_TYPE);
        IMyxInterfaceDescription virtualExternalInterfaceDesc = new MyxJavaClassInitPropertiesInterfaceDescription(
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
        getMyxRuntime().addInterface(PATH, subscriberEndpoint, MyxInterfaceNames.MODEL_ROOT, modelRootDesc,
                EMyxInterfaceDirection.OUT);
        component2Interfaces.get(subscriberEndpoint).add(MyxInterfaceNames.MODEL_ROOT);
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
        IMyxWeld se2mr = getMyxRuntime().createWeld(PATH, subscriberEndpoint, MyxInterfaceNames.MODEL_ROOT, PATH,
                modelRootRuntimeId, MyxInterfaceNames.MODEL_ROOT);
        getMyxRuntime().addWeld(se2mr);
        component2Welds.get(subscriberEndpoint).add(se2mr);

        // begin
        getMyxRuntime().begin(PATH, subscriberEndpoint);
    }
}
