package at.ac.tuwien.dsg.pubsub.middleware.myx;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import at.ac.tuwien.dsg.concurrent.IdentifiableExecutorService;
import at.ac.tuwien.dsg.concurrent.IdentifiableThreadPoolExecutor;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.myx.util.Tuple;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Message.Type;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxClassManager;
import edu.uci.isr.myx.fw.IMyxDynamicBrick;
import edu.uci.isr.myx.fw.IMyxInterfaceDescription;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxJavaClassInterfaceDescription;
import edu.uci.isr.myx.fw.MyxUtils;

/**
 * An alternative implementation of the EventPumpConnector that allows to save
 * special calls that are sent to every connected interface.
 * 
 * @author bernd.rathmanner
 * 
 */
public class MessageDistributor extends AbstractMyxSimpleBrick implements IMyxDynamicBrick, InvocationHandler {

    private Map<String, List<Tuple<Method, Object[]>>> initCalls = new HashMap<>();

    public static final IMyxName REQUIRED_INTERFACE_NAME = MyxMonitoringUtils.createName("out");
    public static final IMyxName PROVIDED_INTERFACE_NAME = MyxMonitoringUtils.createName("in");

    protected List<Object> trueServiceObjects = new CopyOnWriteArrayList<>();
    protected Object proxyObject = null;

    protected IdentifiableExecutorService executor;
    
    public MessageDistributor() {
        executor = new IdentifiableThreadPoolExecutor();
    }

    @Override
    public void init() {
        Set<String> interfaceClassNames = new HashSet<String>();

        IMyxInterfaceDescription miDesc = getMyxBrickItems().getInterfaceManager().getInterfaceDescription(
                PROVIDED_INTERFACE_NAME);
        if (miDesc instanceof MyxJavaClassInterfaceDescription) {
            MyxJavaClassInterfaceDescription jmiDesc = (MyxJavaClassInterfaceDescription) miDesc;
            interfaceClassNames.addAll(Arrays.asList(jmiDesc.getServiceObjectInterfaceNames()));
        }

        int i = 0;
        while (true) {
            String interfaceClassName = MyxUtils.getInitProperties(this).getProperty("interfaceClassName" + i);
            if (interfaceClassName == null)
                break;
            interfaceClassNames.add(interfaceClassName);
            i++;
        }

        List<Class<?>> interfaceClassList = new ArrayList<Class<?>>();
        IMyxClassManager classManager = getMyxBrickItems().getClassManager();
        for (String interfaceClassName : interfaceClassNames) {
            try {
                Class<?> interfaceClass = classManager.classForName(interfaceClassName);
                interfaceClassList.add(interfaceClass);
            } catch (ClassNotFoundException cnfe) {
                throw new IllegalArgumentException("Can't find interface class: " + cnfe.getMessage());
            }
        }

        Class<?>[] interfaceClasses = interfaceClassList.toArray(new Class[interfaceClassList.size()]);
        if (interfaceClasses.length > 0) {
            proxyObject = Proxy.newProxyInstance(interfaceClasses[0].getClassLoader(), interfaceClasses, this);
        }
    }

    @Override
    public void end() {
        executor.shutdown();
        try {
            executor.awaitTermination(5L, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
        }
    }

    @Override
    public void interfaceConnected(IMyxName interfaceName, Object serviceObject) {
        if (interfaceName.equals(REQUIRED_INTERFACE_NAME)) {
            trueServiceObjects.add(serviceObject);

            if (proxyObject == null) {
                ClassLoader cl = serviceObject.getClass().getClassLoader();
                Class<?>[] interfaceClasses = serviceObject.getClass().getInterfaces();
                proxyObject = Proxy.newProxyInstance(cl, interfaceClasses, this);
            }

            if (initCalls.size() > 0) {
                final Object tso = serviceObject;
                for (List<Tuple<Method, Object[]>> calls : initCalls.values()) {
                    for (Tuple<Method, Object[]> call : calls) {
                        final Method m = call.getFst();
                        final Object[] a = call.getSnd();
                        Runnable r = new Runnable() {
                            public void run() {
                                try {
                                    m.invoke(tso, a);
                                } catch (IllegalAccessException iae) {
                                    iae.printStackTrace();
                                    return;
                                } catch (InvocationTargetException ite) {
                                    ite.printStackTrace();
                                    return;
                                }
                            }
                        };
                        executor.execute(r, tso.hashCode());
                    }
                }
            }
        }
    }

    @Override
    public void interfaceDisconnecting(IMyxName interfaceName, Object serviceObject) {
        if (interfaceName.equals(REQUIRED_INTERFACE_NAME)) {
            trueServiceObjects.remove(serviceObject);
        }
    }

    @SuppressWarnings("unused")
    public void interfaceDisconnected(IMyxName interfaceName, Object serviceObject) {
    }

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        if (interfaceName.equals(PROVIDED_INTERFACE_NAME)) {
            return proxyObject;
        }
        return null;
    }

    @Override
    public Object invoke(@SuppressWarnings("unused") Object proxy, Method method, Object[] args) throws Throwable {
        if (proxyObject == null) {
            // asynchronous messages do not have to get delivered.
            return null;
        }
        if (trueServiceObjects.isEmpty()) {
            // asynchronous messages do not have to get delivered.
            return null;
        }

        // if the given arguments contain message objects we search for
        // application specific init-messages
        for (Object object : args) {
            if (object instanceof Message<?>) {
                Message<?> msg = (Message<?>) object;
                if (msg.getType() == Type.INIT) {
                    if (!initCalls.containsKey(msg.getTopic())) {
                        initCalls.put(msg.getTopic(), new ArrayList<Tuple<Method, Object[]>>());
                    }
                    initCalls.get(msg.getTopic()).add(new Tuple<Method, Object[]>(method, args));
                } else if (msg.getType() == Type.CLOSE) {
                    initCalls.remove(msg.getTopic());
                }
            }
        }

        for (final Object serviceObject : trueServiceObjects) {
            final Method m = method;
            final Object[] a = args;
            Runnable r = new Runnable() {
                public void run() {
                    try {
                        m.invoke(serviceObject, a);
                    } catch (IllegalAccessException iae) {
                        iae.printStackTrace();
                        return;
                    } catch (InvocationTargetException ite) {
                        ite.printStackTrace();
                        return;
                    }
                }
            };

            executor.execute(r, serviceObject.hashCode());
        }
        // we don't return values from asynchronous calls
        return null;
    }
}
