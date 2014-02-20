package at.ac.tuwien.infosys.pubsub.middleware.arch.myx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

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

    // interface name prefixes
    public static final String IDISPATCHER_PUB_PREFIX = "Pub" + IDispatcher.class.getSimpleName();
    public static final String IDISPATCHER_SUB_PREFIX = "Sub" + IDispatcher.class.getSimpleName();
    public static final String ISUBSCRIBER_PREFIX = ISubscriber.class.getSimpleName();

    // component names
    public static final IMyxName REGISTRY = MyxUtils.createName(Registry.class.getSimpleName());

    // component name prefixes
    public static final String PUBLISHER_DISPATCHER_PREFIX = PublisherDispatcher.class.getSimpleName();
    public static final String PUBLISHER_ENDPOINT_PREFIX = PublisherEndpoint.class.getSimpleName();
    public static final String SUBSCRIBER_DISPATCHER_PREFIX = SubscriberDispatcher.class.getSimpleName();
    public static final String SUBSCRIBER_ENDPOINT_PREFIX = SubscriberEndpoint.class.getSimpleName();

    // interface descriptions
    private MyxJavaClassInterfaceDescription _iDispatcherDesc;
    private MyxJavaClassInterfaceDescription _iRegistryDesc;
    private MyxJavaClassInterfaceDescription _iSubscriberDesc;

    // connector descriptions
    private MyxJavaClassBrickDescription _synchronousProxyDesc;
    private MyxJavaClassBrickDescription _messageDistributorDesc;

    // counts for naming
    private int _dispatcherCount = 0;
    private int _publisherEndpointCount = 0;
    private int _subscriberEndpointCount = 0;

    /**
     * Track the interfaces and welds of each component. { component name =>
     * (list of interface names, list of welds) }
     */
    private Map<IMyxName, Tuple<List<IMyxName>, List<IMyxWeld>>> _components;

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
        _synchronousProxyDesc = new MyxJavaClassBrickDescription(null, SYNCHRONOUS_PROXY);
        _messageDistributorDesc = new MyxJavaClassBrickDescription(null, MESSAGE_DISTRIBUTOR);

        _myx.addBrick(null, IREGISTRY, _synchronousProxyDesc);

        // add interfaces to connectors
        generateConnectorInterfaces(IREGISTRY, _iRegistryDesc);

        // wire up the components and connectors
        addComponent2ConnectorWeld(REGISTRY, IREGISTRY_NAME, IREGISTRY);

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
        IMyxName iPubDispName = MyxUtils.createName(IDISPATCHER_PUB_PREFIX + dispatcherId);
        IMyxName subDispName = MyxUtils.createName(SUBSCRIBER_DISPATCHER_PREFIX + dispatcherId);
        IMyxName iSubDispName = MyxUtils.createName(IDISPATCHER_SUB_PREFIX + dispatcherId);

        // add the bricks
        try {
            _myx.addBrick(null, pubDispName, pubDispDesc);
            _myx.addBrick(null, iPubDispName, _synchronousProxyDesc);
            _myx.addBrick(null, subDispName, subDispDesc);
            _myx.addBrick(null, iSubDispName, _synchronousProxyDesc);
        } catch (MyxBrickLoadException | MyxBrickCreationException e) {
            e.printStackTrace();
            // TODO: throw exception?
            return;
        }

        // add interfaces to connectors
        generateConnectorInterfaces(iPubDispName, _iDispatcherDesc);
        generateConnectorInterfaces(iSubDispName, _iDispatcherDesc);

        // add interfaces to the components
        generateComponentInterfaces(pubDispName, _iDispatcherDesc, IDISPATCHER_NAME, EMyxInterfaceDirection.IN);
        generateComponentInterfaces(subDispName, _iDispatcherDesc, IDISPATCHER_NAME, EMyxInterfaceDirection.IN);

        _myx.init(null, pubDispName);
        _myx.init(null, iPubDispName);
        _myx.init(null, subDispName);
        _myx.init(null, iSubDispName);

        // wire up the components and connectors
        addComponent2ConnectorWeld(pubDispName, IDISPATCHER_NAME, iPubDispName);
        addComponent2ConnectorWeld(subDispName, IDISPATCHER_NAME, iSubDispName);

        // start up the created components
        _myx.begin(null, pubDispName);
        _myx.begin(null, iPubDispName);
        _myx.begin(null, subDispName);
        _myx.begin(null, iSubDispName);
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
        int endPointId = ++_publisherEndpointCount;
        IMyxName pubEndName = MyxUtils.createName(PUBLISHER_ENDPOINT_PREFIX + endPointId);
        IMyxName iSubscriberName = MyxUtils.createName(ISUBSCRIBER_PREFIX + endPointId);

        // add the bricks
        try {
            _myx.addBrick(null, pubEndName, pubEndDesc);
            _myx.addBrick(null, iSubscriberName, _messageDistributorDesc);
        } catch (MyxBrickLoadException | MyxBrickCreationException e) {
            e.printStackTrace();
            // TODO throw exception??
            return;
        }

        // add interfaces to connectors
        generateConnectorInterfaces(iSubscriberName, _iSubscriberDesc);

        // add interfaces to components
        generateComponentInterfaces(pubEndName, _iDispatcherDesc, IDISPATCHER_NAME, EMyxInterfaceDirection.OUT);
        generateComponentInterfaces(pubEndName, _iRegistryDesc, IREGISTRY_NAME, EMyxInterfaceDirection.OUT);
        generateComponentInterfaces(pubEndName, _iSubscriberDesc, ISUBSCRIBER_NAME, EMyxInterfaceDirection.OUT);

        // important note: we have to call the init methods before we add
        // the welds for the connectors so the proxy object of the connector (in
        // this case the message distributor) gets created
        _myx.init(null, pubEndName);
        _myx.init(null, iSubscriberName);

        // wire up the endpoint
        int dispatcherId = extractId(dispatcherName.getName());
        addConnector2ComponentWeld(MyxUtils.createName(IDISPATCHER_PUB_PREFIX + dispatcherId), IDISPATCHER_NAME,
                pubEndName);
        addConnector2ComponentWeld(IREGISTRY, IREGISTRY_NAME, pubEndName);
        addConnector2ComponentWeld(iSubscriberName, ISUBSCRIBER_NAME, pubEndName);

        // start the created component
        _myx.begin(null, pubEndName);
        _myx.begin(null, iSubscriberName);
    }

    public void createSubscriberEndpoint(String subscriberEndpointClassName, Dispatcher<?> dispatcher) {
        try {
            Class<?> subEndClass = Class.forName(subscriberEndpointClassName);
            if (!SubscriberEndpoint.class.isAssignableFrom(subEndClass)) {
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
        IMyxName subEndName = MyxUtils.createName(SUBSCRIBER_ENDPOINT_PREFIX + ++_subscriberEndpointCount);

        // add the bricks
        try {
            _myx.addBrick(null, subEndName, subEndDesc);
        } catch (MyxBrickLoadException | MyxBrickCreationException e) {
            e.printStackTrace();
            // TODO throw exception??
            return;
        }

        // add interfaces to components
        generateComponentInterfaces(subEndName, _iDispatcherDesc, IDISPATCHER_NAME, EMyxInterfaceDirection.OUT);
        generateComponentInterfaces(subEndName, _iRegistryDesc, IREGISTRY_NAME, EMyxInterfaceDirection.OUT);
        generateComponentInterfaces(subEndName, _iSubscriberDesc, ISUBSCRIBER_NAME, EMyxInterfaceDirection.IN);

        // wire up the endpoint
        int dispatcherId = extractId(dispatcherName.getName());
        addConnector2ComponentWeld(MyxUtils.createName(IDISPATCHER_SUB_PREFIX + dispatcherId), IDISPATCHER_NAME,
                subEndName);
        addConnector2ComponentWeld(IREGISTRY, IREGISTRY_NAME, subEndName);

        // start the created component
        _myx.init(null, subEndName);
        _myx.begin(null, subEndName);
    }

    public void shutdownEndpoint(PublisherEndpoint<?> endpoint) {

    }

    public void shutdownEndpoint(SubscriberEndpoint<?> endpoint) {

    }

    public void wireEndpoint(SubscriberEndpoint<?> subscriber, PublisherEndpoint<?> publisher) {
        IMyxName endpointName = MyxUtils.getName(subscriber);
        int pubEndpointId = extractId(endpointName.getName());
        IMyxName iSubscriberName = MyxUtils.createName(ISUBSCRIBER_PREFIX + pubEndpointId);

        addComponent2ConnectorWeld(endpointName, ISUBSCRIBER_NAME, iSubscriberName);
    }

    public void unwireEndpoint(SubscriberEndpoint<?> subscriber, PublisherEndpoint<?> publisher) {

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

    /**
     * Extract an integer from the given string.
     * 
     * @param name
     * @return
     */
    protected int extractId(String name) {
        Scanner sc = new Scanner(name);
        sc.useDelimiter("[^0-9]+");
        int id = sc.nextInt();
        sc.close();
        return id;
    }
}
