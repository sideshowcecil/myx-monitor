package at.ac.tuwien.infosys.pubsub.middleware.arch.myx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import at.ac.tuwien.infosys.pubsub.middleware.arch.component.Dispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.PublisherDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.PublisherEndpoint;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.Registry;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.SubscriberDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.SubscriberEndpoint;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.IRegistry;
import at.ac.tuwien.infosys.pubsub.middleware.arch.interfaces.ISubscriber;
import at.ac.tuwien.infosys.pubsub.util.Tuple;
import edu.uci.isr.myx.conn.SynchronousProxyConnector;
import edu.uci.isr.myx.fw.EMyxInterfaceDirection;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.IMyxRuntime;
import edu.uci.isr.myx.fw.IMyxWeld;
import edu.uci.isr.myx.fw.MyxBrickCreationException;
import edu.uci.isr.myx.fw.MyxBrickLoadException;
import edu.uci.isr.myx.fw.MyxJavaClassBrickDescription;
import edu.uci.isr.myx.fw.MyxJavaClassInterfaceDescription;
import edu.uci.isr.myx.fw.MyxUtils;

public class MyxRuntime {

    // interface class names
    public static final String IDISPATCHER_NAME = IDispatcher.class.getName();
    public static final String IREGISTRY_NAME = IRegistry.class.getName();
    public static final String ISUBSCRIBER_NAME = ISubscriber.class.getName();

    // component class names
    public static final String REGISTRY_NAME = Registry.class.getName();

    // connector class names
    public static final String MESSAGE_DISTRIBUTOR = MessageDistributor.class.getName();
    public static final String SYNCHRONOUS_PROXY = SynchronousProxyConnector.class.getName();

    // interface names
    public static final IMyxName IDISPATCHER = MyxUtils.createName(IDISPATCHER_NAME);
    public static final IMyxName IREGISTRY = MyxUtils.createName(IREGISTRY_NAME);
    public static final IMyxName ISUBSCRIBER = MyxUtils.createName(ISUBSCRIBER_NAME);

    // component names
    public static final IMyxName REGISTRY = MyxUtils.createName(Registry.class.getSimpleName());

    // component name prefixes
    public static final String PUBLISHER_DISPATCHER_PREFIX = PublisherDispatcher.class.getSimpleName();
    public static final String PUBLISHER_ENDPOINT_PREFIX = PublisherEndpoint.class.getSimpleName();
    public static final String SUBSCRIBER_DISPATCHER_PREFIX = SubscriberDispatcher.class.getSimpleName();
    public static final String SUBSCRIBER_ENDPOINT_PREFIX = SubscriberEndpoint.class.getSimpleName();

    // interface descriptions
    public MyxJavaClassInterfaceDescription _iDispatcherDesc;
    public MyxJavaClassInterfaceDescription _iRegistryDesc;
    public MyxJavaClassInterfaceDescription _iSubscriberDesc;

    // counts for naming
    private int _dispatcherCount = 0;
    private int _publisherEndpointCount = 0;
    private int _subscriberEndpointCount = 0;

    /**
     * Track the interfaces and welds of each component. { component name =>
     * (list of interface names, list of welds) }
     */
    private Map<IMyxName, Tuple<List<IMyxName>, List<IMyxWeld>>> _components;

    public static void main(String args[]) { // TODO: remove main from here
        try {
            getInstance().boostrapArchitecture();
        } catch (MyxBrickLoadException | MyxBrickCreationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static MyxRuntime _instance = new MyxRuntime();

    public static MyxRuntime getInstance() {
        return _instance;
    }

    private IMyxRuntime _myx;

    private MyxRuntime() {
        _myx = MyxUtils.getDefaultImplementation().createRuntime();
    }

    public void boostrapArchitecture() throws MyxBrickLoadException, MyxBrickCreationException {
        // interfaces
        _iDispatcherDesc = new MyxJavaClassInterfaceDescription(new String[] { IDISPATCHER_NAME });
        _iRegistryDesc = new MyxJavaClassInterfaceDescription(new String[] { IREGISTRY_NAME });
        _iSubscriberDesc = new MyxJavaClassInterfaceDescription(new String[] { ISUBSCRIBER_NAME });

        // components
        MyxJavaClassBrickDescription registryDesc = new MyxJavaClassBrickDescription(null, REGISTRY_NAME);

        _myx.addBrick(null, REGISTRY, registryDesc);

        // add interfaces to components
        generateComponentInterfaces(REGISTRY, _iRegistryDesc, IREGISTRY_NAME, EMyxInterfaceDirection.IN);

        // connectors
        MyxJavaClassBrickDescription synchronousProxyDispatcherDesc = new MyxJavaClassBrickDescription(null,
                SYNCHRONOUS_PROXY);
        MyxJavaClassBrickDescription synchronousProxyRegistryDesc = new MyxJavaClassBrickDescription(null,
                SYNCHRONOUS_PROXY);
        MyxJavaClassBrickDescription messageDistributorDesc = new MyxJavaClassBrickDescription(null,
                MESSAGE_DISTRIBUTOR);

        _myx.addBrick(null, IDISPATCHER, synchronousProxyDispatcherDesc);
        _myx.addBrick(null, IREGISTRY, synchronousProxyRegistryDesc);
        _myx.addBrick(null, ISUBSCRIBER, messageDistributorDesc);

        // add interfaces to connectors
        generateConnectorInterfaces(IDISPATCHER, _iDispatcherDesc);
        generateConnectorInterfaces(IREGISTRY, _iRegistryDesc);
        generateConnectorInterfaces(ISUBSCRIBER, _iSubscriberDesc);

        // wire up the components and connectors
        addConnector2ComponentWeld(IREGISTRY, IREGISTRY_NAME, REGISTRY);

        // initialize the components map
        _components = new HashMap<>();

        // startup the application
        _myx.init(null, null);
        _myx.begin(null, null);
    }

    public void createDispatcher(String publisherDispatcherClassName, Properties publisherProperties,
            String subscriberDispatcherClassName, Properties subscriberProperties) {
        try {
            Class<?> pubDispClass = Class.forName(publisherDispatcherClassName);
            Class<?> subDispClass = Class.forName(subscriberDispatcherClassName);
            if (!PublisherDispatcher.class.isAssignableFrom(pubDispClass)
                    || !SubscriberDispatcher.class.isAssignableFrom(subDispClass)) {
                // TODO: throw exception?
                return;
            }
        } catch (ClassNotFoundException e1) {
            // TODO: throw exception?
            return;
        }

        // get the class descriptions
        MyxJavaClassBrickDescription pubDispDesc = new MyxJavaClassBrickDescription(publisherProperties,
                publisherDispatcherClassName);
        MyxJavaClassBrickDescription subDispDesc = new MyxJavaClassBrickDescription(subscriberProperties,
                subscriberDispatcherClassName);

        // create the names
        int dispatcherId = ++_dispatcherCount;
        IMyxName pubDispName = MyxUtils.createName(PUBLISHER_DISPATCHER_PREFIX + dispatcherId);
        IMyxName subDispName = MyxUtils.createName(SUBSCRIBER_DISPATCHER_PREFIX + dispatcherId);

        // add the bricks
        try {
            _myx.addBrick(null, pubDispName, pubDispDesc);
            _myx.addBrick(null, subDispName, subDispDesc);
        } catch (MyxBrickLoadException | MyxBrickCreationException e) {
            e.printStackTrace();
            // TODO: throw exception?
            return;
        }

        // add interfaces to the components
        generateComponentInterfaces(pubDispName, _iDispatcherDesc, IDISPATCHER_NAME, EMyxInterfaceDirection.IN);
        generateComponentInterfaces(subDispName, _iDispatcherDesc, IDISPATCHER_NAME, EMyxInterfaceDirection.IN);

        // wire up the components and connectors
        // TODO: do we have to create a new connector each time?? i think so
        addConnector2ComponentWeld(IDISPATCHER, IDISPATCHER_NAME, pubDispName);
        addConnector2ComponentWeld(IDISPATCHER, IDISPATCHER_NAME, subDispName);

        // TODO: do we have to call init and begin??
    }

    public void createPublisherEndpoint(String publisherEndpointClassName, Dispatcher<?> dispatcher) {
        try {
            Class<?> pubEndClass = Class.forName(publisherEndpointClassName);
            if (!PublisherEndpoint.class.isAssignableFrom(pubEndClass)) {
                // TODO: throw exception?
                return;
            }
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
            // TODO: throw exception?
            return;
        }

        IMyxName dispatcherName = MyxUtils.getName(dispatcher);

        // get the class description
        MyxJavaClassBrickDescription pubEndDesc = new MyxJavaClassBrickDescription(null, publisherEndpointClassName);

        // create name
        IMyxName pubEndName = MyxUtils.createName(PUBLISHER_ENDPOINT_PREFIX + ++_publisherEndpointCount);

        // add the bricks
        try {
            _myx.addBrick(null, pubEndName, pubEndDesc);
        } catch (MyxBrickLoadException | MyxBrickCreationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // add interfaces to components
        generateComponentInterfaces(pubEndName, _iDispatcherDesc, IDISPATCHER_NAME, EMyxInterfaceDirection.OUT);
        generateComponentInterfaces(pubEndName, _iRegistryDesc, IREGISTRY_NAME, EMyxInterfaceDirection.OUT);
        generateComponentInterfaces(pubEndName, _iSubscriberDesc, ISUBSCRIBER_NAME, EMyxInterfaceDirection.OUT);

        // wire up the endpoint
        // TODO: do we have to create a new connector each time?? i think so
        addComponent2ConnectorWeld(pubEndName, IDISPATCHER_NAME, dispatcherName);
        addComponent2ConnectorWeld(pubEndName, IREGISTRY_NAME, IREGISTRY);
        addComponent2ConnectorWeld(pubEndName, ISUBSCRIBER_NAME, ISUBSCRIBER);

        // TODO: do we have to call init and begin??
    }

    public void createSubscriberEndpoint(String subscriberEndpointClassName, Dispatcher<?> dispatcher) {
        try {
            Class<?> subEndClass = Class.forName(subscriberEndpointClassName);
            if (!PublisherEndpoint.class.isAssignableFrom(subEndClass)) {
                // TODO: throw exception?
                return;
            }
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
            // TODO: throw exception?
            return;
        }

        IMyxName dispatcherName = MyxUtils.getName(dispatcher);

        // get the class description
        MyxJavaClassBrickDescription subEndDesc = new MyxJavaClassBrickDescription(null, subscriberEndpointClassName);

        // create name
        IMyxName subEndName = MyxUtils.createName(PUBLISHER_ENDPOINT_PREFIX + ++_subscriberEndpointCount);

        // add the bricks
        try {
            _myx.addBrick(null, subEndName, subEndDesc);
        } catch (MyxBrickLoadException | MyxBrickCreationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // add interfaces to components
        generateComponentInterfaces(subEndName, _iDispatcherDesc, IDISPATCHER_NAME, EMyxInterfaceDirection.OUT);
        generateComponentInterfaces(subEndName, _iRegistryDesc, IREGISTRY_NAME, EMyxInterfaceDirection.OUT);
        generateComponentInterfaces(subEndName, _iSubscriberDesc, ISUBSCRIBER_NAME, EMyxInterfaceDirection.IN);

        // wire up the endpoint
        // TODO: do we have to create a new connector each time?? i think so
        addComponent2ConnectorWeld(subEndName, IDISPATCHER_NAME, dispatcherName);
        addComponent2ConnectorWeld(subEndName, IREGISTRY_NAME, IREGISTRY);
        addConnector2ComponentWeld(ISUBSCRIBER, ISUBSCRIBER_NAME, subEndName);

        // TODO: do we have to call init and begin??
    }

    public MessageDistributor createMessageDistributor(PublisherEndpoint<?> endpoint) {

        return null;
    }

    public void removeMessageDistributor(MessageDistributor distributor) {

    }

    public void wireMessageDistributor(SubscriberEndpoint<?> endpoint, MessageDistributor distributor) {
        IMyxName endpointName = MyxUtils.getName(endpoint);
        IMyxName distributorName = MyxUtils.getName(distributor);

        addComponent2ConnectorWeld(endpointName, ISUBSCRIBER_NAME, distributorName);
    }

    public void unwireMessageDistributor(SubscriberEndpoint<?> endpoint, MessageDistributor distributor) {

    }

    /**
     * Generate and add an interface to the myx runtime.
     * 
     * @param comp
     * @param intf
     * @param intfName
     * @param dir
     */
    protected void generateComponentInterfaces(IMyxName comp, MyxJavaClassInterfaceDescription intf, String intfName,
            EMyxInterfaceDirection dir) {
        synchronized (_myx) {
            _myx.addInterface(null, comp, MyxUtils.createName(intfName), intf, dir);
        }
    }

    /**
     * Generate and add both incoming and outgoing interfaces of a connector.
     * 
     * @param conn
     * @param intf
     */
    protected void generateConnectorInterfaces(IMyxName conn, MyxJavaClassInterfaceDescription intf) {
        synchronized (_myx) {
            _myx.addInterface(null, conn, MyxUtils.createName("out"), intf, EMyxInterfaceDirection.OUT);
            _myx.addInterface(null, conn, MyxUtils.createName("in"), intf, EMyxInterfaceDirection.IN);
        }
    }

    /**
     * Connect a component to a connector.
     * 
     * @param comp
     * @param compIntfName
     * @param conn
     */
    protected void addComponent2ConnectorWeld(IMyxName comp, String compIntfName, IMyxName conn) {
        synchronized (_myx) {
            _myx.addWeld(_myx.createWeld(null, conn, MyxUtils.createName("out"), null, comp,
                    MyxUtils.createName(compIntfName)));
        }
    }

    /**
     * Connect a connector to a component.
     * 
     * @param comp
     * @param conn
     * @param compIntfName
     */
    protected void addConnector2ComponentWeld(IMyxName conn, String compIntfName, IMyxName comp) {
        synchronized (_myx) {
            _myx.addWeld(_myx.createWeld(null, comp, MyxUtils.createName(compIntfName), null, conn,
                    MyxUtils.createName("in")));
        }
    }

    protected void removeComponent(IMyxName comp) {
        synchronized (_components) {
            if (_components.containsKey(comp)) {
                synchronized (_myx) {
                    Tuple<List<IMyxName>, List<IMyxWeld>> t = _components.get(comp);
                    // remove the welds
                    for (IMyxWeld weld : t.getSnd()) {
                        _myx.removeWeld(weld);
                    }
                    // remove the interfaces
                    for (IMyxName intfName : t.getFst()) {
                        _myx.removeInterface(null, comp, intfName);
                    }
                    // remove the component itself (brick)
                    _myx.removeBrick(null, comp);
                }

                // remove the key from the map
                _components.remove(comp);
            }
        }
    }
}
