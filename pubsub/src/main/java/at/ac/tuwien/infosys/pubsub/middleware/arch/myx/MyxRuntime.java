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
    private MyxJavaClassInterfaceDescription iDispatcherDesc;
    private MyxJavaClassInterfaceDescription iSubscriberDesc;

    // connector descriptions
    private MyxJavaClassBrickDescription synchronousProxyDesc;
    private MyxJavaClassBrickDescription messageDistributorDesc;

    // counts for naming
    private int dispatcherCount = 0;
    private int publisherEndpointCount = 0;
    private int subscriberEndpointCount = 0;

    /**
     * Track the interfaces and welds of each component. { component name =>
     * (list of connectors, list of welds) }
     */
    private Map<IMyxName, Tuple<List<IMyxName>, List<IMyxWeld>>> components;

    private static MyxRuntime instance = new MyxRuntime();

    public static MyxRuntime getInstance() {
        return instance;
    }

    private IMyxRuntime myx;

    private MyxRuntime() {
        myx = MyxUtils.getDefaultImplementation().createRuntime();
    }

    public void boostrapArchitecture() throws MyxBrickLoadException, MyxBrickCreationException {
        // interfaces
        iDispatcherDesc = new MyxJavaClassInterfaceDescription(new String[] { IDISPATCHER_NAME });
        iSubscriberDesc = new MyxJavaClassInterfaceDescription(new String[] { ISUBSCRIBER_NAME });

        // connectors
        synchronousProxyDesc = new MyxJavaClassBrickDescription(null, SYNCHRONOUS_PROXY_NAME);
        messageDistributorDesc = new MyxJavaClassBrickDescription(null, MESSAGE_DISTRIBUTOR_NAME);

        myx.addBrick(null, MESSAGE_DISTRIBUTOR, messageDistributorDesc);

        // add interfaces to connectors
        generateConnectorInterfaces(MESSAGE_DISTRIBUTOR, iSubscriberDesc);

        // initialize the components map
        components = new HashMap<>();

        // startup the application
        myx.init(null, null);
        myx.begin(null, null);
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
        int dispatcherId = ++dispatcherCount;
        IMyxName pubDispName = MyxUtils.createName(PUBLISHER_DISPATCHER_PREFIX + dispatcherId);
        IMyxName iPubDispName = MyxUtils.createName(IDISPATCHER_PUB_PREFIX + dispatcherId);
        IMyxName subDispName = MyxUtils.createName(SUBSCRIBER_DISPATCHER_PREFIX + dispatcherId);
        IMyxName iSubDispName = MyxUtils.createName(IDISPATCHER_SUB_PREFIX + dispatcherId);

        // add the bricks
        try {
            myx.addBrick(null, pubDispName, pubDispDesc);
            myx.addBrick(null, iPubDispName, synchronousProxyDesc);
            components.put(pubDispName, new Tuple<List<IMyxName>, List<IMyxWeld>>(new ArrayList<IMyxName>(),
                    new ArrayList<IMyxWeld>()));
            components.get(pubDispName).getFst().add(iPubDispName);
            myx.addBrick(null, subDispName, subDispDesc);
            myx.addBrick(null, iSubDispName, synchronousProxyDesc);
            components.put(subDispName, new Tuple<List<IMyxName>, List<IMyxWeld>>(new ArrayList<IMyxName>(),
                    new ArrayList<IMyxWeld>()));
            components.get(subDispName).getFst().add(iSubDispName);
        } catch (MyxBrickLoadException | MyxBrickCreationException e) {
            e.printStackTrace();
            // TODO: throw exception?
            return;
        }

        // add interfaces to connectors
        generateConnectorInterfaces(iPubDispName, iDispatcherDesc);
        generateConnectorInterfaces(iSubDispName, iDispatcherDesc);

        // add interfaces to the components
        generateComponentInterfaces(pubDispName, iDispatcherDesc, IDISPATCHER_NAME, EMyxInterfaceDirection.IN);
        generateComponentInterfaces(subDispName, iDispatcherDesc, IDISPATCHER_NAME, EMyxInterfaceDirection.IN);

        // wire up the components and connectors
        addConnector2ComponentWeld(iPubDispName, IDISPATCHER_NAME, pubDispName);
        addConnector2ComponentWeld(iSubDispName, IDISPATCHER_NAME, subDispName);

        // start up the created components

        myx.init(null, pubDispName);
        myx.init(null, iPubDispName);
        myx.init(null, subDispName);
        myx.init(null, iSubDispName);

        myx.begin(null, pubDispName);
        myx.begin(null, iPubDispName);
        myx.begin(null, subDispName);
        myx.begin(null, iSubDispName);
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
        int endPointId = ++publisherEndpointCount;
        IMyxName pubEndName = MyxUtils.createName(PUBLISHER_ENDPOINT_PREFIX + endPointId);

        // add the bricks
        try {
            myx.addBrick(null, pubEndName, pubEndDesc);
            components.put(pubEndName, new Tuple<List<IMyxName>, List<IMyxWeld>>(new ArrayList<IMyxName>(),
                    new ArrayList<IMyxWeld>()));
        } catch (MyxBrickLoadException | MyxBrickCreationException e) {
            e.printStackTrace();
            // TODO throw exception??
            return;
        }

        // add interfaces to components
        generateComponentInterfaces(pubEndName, iDispatcherDesc, IDISPATCHER_NAME, EMyxInterfaceDirection.OUT);
        generateComponentInterfaces(pubEndName, iSubscriberDesc, ISUBSCRIBER_NAME, EMyxInterfaceDirection.OUT);

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
        myx.init(null, pubEndName);
        myx.begin(null, pubEndName);
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
        IMyxName subEndName = MyxUtils.createName(SUBSCRIBER_ENDPOINT_PREFIX + ++subscriberEndpointCount);

        // add the bricks
        try {
            myx.addBrick(null, subEndName, subEndDesc);
            components.put(subEndName, new Tuple<List<IMyxName>, List<IMyxWeld>>(new ArrayList<IMyxName>(),
                    new ArrayList<IMyxWeld>()));
        } catch (MyxBrickLoadException | MyxBrickCreationException e) {
            e.printStackTrace();
            // TODO throw exception??
            return;
        }

        // add interfaces to components
        generateComponentInterfaces(subEndName, iDispatcherDesc, IDISPATCHER_NAME, EMyxInterfaceDirection.OUT);
        generateComponentInterfaces(subEndName, iSubscriberDesc, ISUBSCRIBER_NAME, EMyxInterfaceDirection.IN);

        // wire up the endpoint
        int dispatcherId = extractId(MyxUtils.getName(dispatcher).getName());
        addComponent2ConnectorWeld(subEndName, IDISPATCHER_NAME,
                MyxUtils.createName(IDISPATCHER_SUB_PREFIX + dispatcherId));

        // start the created component
        myx.init(null, subEndName);
        myx.begin(null, subEndName);
    }

    public void shutdownDispatcher(Dispatcher<?> dispatcher) {
        IMyxName comp = MyxUtils.getName(dispatcher);
        myx.end(null, comp);
        myx.destroy(null, comp);
        removeComponent(comp);
    }

    public void shutdownEndpoint(PublisherEndpoint<?> endpoint) {
        IMyxName comp = MyxUtils.getName(endpoint);
        myx.end(null, comp);
        myx.destroy(null, comp);
        removeComponent(comp);
    }

    public void shutdownEndpoint(SubscriberEndpoint<?> endpoint) {
        IMyxName comp = MyxUtils.getName(endpoint);
        myx.end(null, comp);
        myx.destroy(null, comp);
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
        synchronized (myx) {
            myx.addInterface(null, comp, MyxUtils.createName(intfName), intf, dir);
        }
    }

    /**
     * Generate and add both incoming and outgoing interfaces of a connector.
     * 
     * @param conn
     * @param intf
     */
    protected void generateConnectorInterfaces(IMyxName conn, MyxJavaClassInterfaceDescription intf) {
        synchronized (myx) {
            myx.addInterface(null, conn, MyxUtils.createName("out"), intf, EMyxInterfaceDirection.OUT);
            myx.addInterface(null, conn, MyxUtils.createName("in"), intf, EMyxInterfaceDirection.IN);
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
        synchronized (myx) {
            weld = myx.createWeld(null, conn, MyxUtils.createName("out"), null, comp, MyxUtils.createName(intfName));
            myx.addWeld(weld);
        }
        components.get(comp).getSnd().add(weld);
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
        synchronized (myx) {
            weld = myx.createWeld(null, comp, MyxUtils.createName(intfName), null, conn, MyxUtils.createName("in"));
            myx.addWeld(weld);
        }
        components.get(comp).getSnd().add(weld);
    }

    protected void removeComponent(IMyxName comp) {
        synchronized (components) {
            if (components.containsKey(comp)) {
                synchronized (myx) {
                    Tuple<List<IMyxName>, List<IMyxWeld>> t = components.get(comp);
                    // remove the welds
                    for (IMyxWeld weld : t.getSnd()) {
                        try {
                            myx.removeWeld(weld);
                        } catch (NullPointerException e) {
                        }
                    }
                    // remove the interfaces
                    for (IMyxName connector : t.getFst()) {
                        myx.removeBrick(null, connector);
                    }
                    // remove the component itself (brick)
                    myx.removeBrick(null, comp);
                }

                // remove the key from the map
                components.remove(comp);
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
