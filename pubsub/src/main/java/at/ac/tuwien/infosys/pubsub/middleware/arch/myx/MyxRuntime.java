package at.ac.tuwien.infosys.pubsub.middleware.arch.myx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import at.ac.tuwien.infosys.pubsub.middleware.arch.component.Dispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.PublisherDispatcher;
import at.ac.tuwien.infosys.pubsub.middleware.arch.component.PublisherEndpoint;
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

    // connector class names
    public static final String MESSAGE_DISTRIBUTOR_NAME = MessageDistributor.class.getName();
    public static final String SYNCHRONOUS_PROXY_NAME = SynchronousProxyConnector.class.getName();

    // interface names
    public static final IMyxName IDISPATCHER = MyxUtils.createName(IDISPATCHER_NAME);
    public static final IMyxName ISUBSCRIBER = MyxUtils.createName(ISUBSCRIBER_NAME);

    // interface name prefixes
    public static final String IDISPATCHER_PUB_PREFIX = "Pub" + IDispatcher.class.getSimpleName();
    public static final String IDISPATCHER_SUB_PREFIX = "Sub" + IDispatcher.class.getSimpleName();
    public static final String ISUBSCRIBER_PREFIX = ISubscriber.class.getSimpleName();

    // connector names
    public static IMyxName MESSAGE_DISTRIBUTOR = MyxUtils.createName(MESSAGE_DISTRIBUTOR_NAME);

    // component name prefixes
    public static final String PUBLISHER_DISPATCHER_PREFIX = PublisherDispatcher.class.getSimpleName();
    public static final String PUBLISHER_ENDPOINT_PREFIX = PublisherEndpoint.class.getSimpleName();
    public static final String SUBSCRIBER_DISPATCHER_PREFIX = SubscriberDispatcher.class.getSimpleName();
    public static final String SUBSCRIBER_ENDPOINT_PREFIX = SubscriberEndpoint.class.getSimpleName();

    // interface descriptions
    private MyxJavaClassInterfaceDescription _iDispatcherDesc;
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
     * (list of connectors, list of welds) }
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
        _iSubscriberDesc = new MyxJavaClassInterfaceDescription(new String[] { ISUBSCRIBER_NAME });

        // connectors
        _synchronousProxyDesc = new MyxJavaClassBrickDescription(null, SYNCHRONOUS_PROXY_NAME);
        _messageDistributorDesc = new MyxJavaClassBrickDescription(null, MESSAGE_DISTRIBUTOR_NAME);

        _myx.addBrick(null, MESSAGE_DISTRIBUTOR, _messageDistributorDesc);

        // add interfaces to connectors
        generateConnectorInterfaces(MESSAGE_DISTRIBUTOR, _iSubscriberDesc);

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
            _components.put(pubDispName, new Tuple<List<IMyxName>, List<IMyxWeld>>(new ArrayList<IMyxName>(),
                    new ArrayList<IMyxWeld>()));
            _components.get(pubDispName).getFst().add(iPubDispName);
            _myx.addBrick(null, subDispName, subDispDesc);
            _myx.addBrick(null, iSubDispName, _synchronousProxyDesc);
            _components.put(subDispName, new Tuple<List<IMyxName>, List<IMyxWeld>>(new ArrayList<IMyxName>(),
                    new ArrayList<IMyxWeld>()));
            _components.get(subDispName).getFst().add(iSubDispName);
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

        // wire up the components and connectors
        addConnector2ComponentWeld(iPubDispName, IDISPATCHER_NAME, pubDispName);
        addConnector2ComponentWeld(iSubDispName, IDISPATCHER_NAME, subDispName);

        // start up the created components

        _myx.init(null, pubDispName);
        _myx.init(null, iPubDispName);
        _myx.init(null, subDispName);
        _myx.init(null, iSubDispName);

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

        // get the class description
        MyxJavaClassBrickDescription pubEndDesc = new MyxJavaClassBrickDescription(null, publisherEndpointClassName);

        // create name
        int endPointId = ++_publisherEndpointCount;
        IMyxName pubEndName = MyxUtils.createName(PUBLISHER_ENDPOINT_PREFIX + endPointId);

        // add the bricks
        try {
            _myx.addBrick(null, pubEndName, pubEndDesc);
            _components.put(pubEndName, new Tuple<List<IMyxName>, List<IMyxWeld>>(new ArrayList<IMyxName>(),
                    new ArrayList<IMyxWeld>()));
        } catch (MyxBrickLoadException | MyxBrickCreationException e) {
            e.printStackTrace();
            // TODO throw exception??
            return;
        }

        // add interfaces to components
        generateComponentInterfaces(pubEndName, _iDispatcherDesc, IDISPATCHER_NAME, EMyxInterfaceDirection.OUT);
        generateComponentInterfaces(pubEndName, _iSubscriberDesc, ISUBSCRIBER_NAME, EMyxInterfaceDirection.OUT);

        // important note: we have to call the init methods before we add
        // the welds for the connectors so the proxy object of the connector (in
        // this case the message distributor) gets created
        // _myx.init(null, pubEndName);

        // wire up the endpoint
        int dispatcherId = extractId(MyxUtils.getName(dispatcher).getName());
        addComponent2ConnectorWeld(pubEndName, IDISPATCHER_NAME,
                MyxUtils.createName(IDISPATCHER_PUB_PREFIX + dispatcherId));
        addComponent2ConnectorWeld(pubEndName, ISUBSCRIBER_NAME, MESSAGE_DISTRIBUTOR);

        // start the created component
        _myx.init(null, pubEndName);
        _myx.begin(null, pubEndName);
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

        // get the class description
        MyxJavaClassBrickDescription subEndDesc = new MyxJavaClassBrickDescription(null, subscriberEndpointClassName);

        // create name
        IMyxName subEndName = MyxUtils.createName(SUBSCRIBER_ENDPOINT_PREFIX + ++_subscriberEndpointCount);

        // add the bricks
        try {
            _myx.addBrick(null, subEndName, subEndDesc);
            _components.put(subEndName, new Tuple<List<IMyxName>, List<IMyxWeld>>(new ArrayList<IMyxName>(),
                    new ArrayList<IMyxWeld>()));
        } catch (MyxBrickLoadException | MyxBrickCreationException e) {
            e.printStackTrace();
            // TODO throw exception??
            return;
        }

        // add interfaces to components
        generateComponentInterfaces(subEndName, _iDispatcherDesc, IDISPATCHER_NAME, EMyxInterfaceDirection.OUT);
        generateComponentInterfaces(subEndName, _iSubscriberDesc, ISUBSCRIBER_NAME, EMyxInterfaceDirection.IN);

        // wire up the endpoint
        int dispatcherId = extractId(MyxUtils.getName(dispatcher).getName());
        addComponent2ConnectorWeld(subEndName, IDISPATCHER_NAME,
                MyxUtils.createName(IDISPATCHER_SUB_PREFIX + dispatcherId));

        // start the created component
        _myx.init(null, subEndName);
        _myx.begin(null, subEndName);
    }

    public void shutdownDispatcher(Dispatcher<?> dispatcher) {
        IMyxName comp = MyxUtils.getName(dispatcher);
        _myx.end(null, comp);
        _myx.destroy(null, comp);
        removeComponent(comp);
    }

    public void shutdownEndpoint(PublisherEndpoint<?> endpoint) {
        IMyxName comp = MyxUtils.getName(endpoint);
        _myx.end(null, comp);
        _myx.destroy(null, comp);
        removeComponent(comp);
    }

    public void shutdownEndpoint(SubscriberEndpoint<?> endpoint) {
        IMyxName comp = MyxUtils.getName(endpoint);
        _myx.end(null, comp);
        _myx.destroy(null, comp);
        removeComponent(comp);
    }

    public void wireEndpoint(SubscriberEndpoint<?> subscriber) {
        addConnector2ComponentWeld(MESSAGE_DISTRIBUTOR, ISUBSCRIBER_NAME, MyxUtils.getName(subscriber));
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
     * Connect a connector (out) to a component (in).
     * 
     * @param conn
     * @param intfName
     * @param comp
     */
    protected void addConnector2ComponentWeld(IMyxName conn, String intfName, IMyxName comp) {
        IMyxWeld weld = null;
        synchronized (_myx) {
            weld = _myx.createWeld(null, conn, MyxUtils.createName("out"), null, comp, MyxUtils.createName(intfName));
            _myx.addWeld(weld);
        }
        _components.get(comp).getSnd().add(weld);
    }

    /**
     * Connect a component (out) to a connector (in).
     * 
     * @param conn
     * @param comp
     * @param intfName
     */
    protected void addComponent2ConnectorWeld(IMyxName comp, String intfName, IMyxName conn) {
        IMyxWeld weld = null;
        synchronized (_myx) {
            weld = _myx.createWeld(null, comp, MyxUtils.createName(intfName), null, conn, MyxUtils.createName("in"));
            _myx.addWeld(weld);
        }
        _components.get(comp).getSnd().add(weld);
    }

    protected void removeComponent(IMyxName comp) {
        synchronized (_components) {
            if (_components.containsKey(comp)) {
                synchronized (_myx) {
                    Tuple<List<IMyxName>, List<IMyxWeld>> t = _components.get(comp);
                    // remove the welds
                    for (IMyxWeld weld : t.getSnd()) {
                        try {
                            _myx.removeWeld(weld);
                        } catch (NullPointerException e) {
                        }
                    }
                    // remove the interfaces
                    for (IMyxName connector : t.getFst()) {
                        _myx.removeBrick(null, connector);
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
