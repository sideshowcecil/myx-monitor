package at.ac.tuwien.infosys.pubsub.middleware.arch.myx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.ac.tuwien.infosys.pubsub.message.Message;
import at.ac.tuwien.infosys.pubsub.message.Message.Type;
import at.ac.tuwien.infosys.pubsub.util.Tuple;
import edu.uci.isr.myx.conn.EventPumpConnector;
import edu.uci.isr.myx.fw.IMyxName;

/**
 * An extension of the EventPumpConnector that allows to save special calls that
 * are sent to every connected interface.
 * 
 * @author bernd.rathmanner
 * 
 */
public class MessageDistributor extends EventPumpConnector {

    private List<Tuple<Method, Object[]>> initCalls = new ArrayList<>();

    @Override
    public void interfaceConnected(IMyxName interfaceName, Object serviceObject) {
        if (interfaceName.equals(REQUIRED_INTERFACE_NAME)) {
            // for the OUT side --> message sink side, where to forward the
            // invocation to.
            // will not be called for the message source side, thus proxy needs
            // to be set differently
            synchronized (this) {
                List<Object> l = new ArrayList<Object>(Arrays.asList(trueServiceObjects));
                l.add(serviceObject);
                trueServiceObjects = l.toArray(new Object[l.size()]);
            }
            if (initCalls.size() > 0) {
                final Object tso = serviceObject;
                for (Tuple<Method, Object[]> call : initCalls) {
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
                    asyncExecutor.execute(r);
                }
            }
        } else if (interfaceName.equals(PROVIDED_INTERFACE_NAME)) {
            if (proxyObject == null) {
                ClassLoader cl = serviceObject.getClass().getClassLoader();
                Class<?>[] interfaceClasses = serviceObject.getClass().getInterfaces();
                proxyObject = Proxy.newProxyInstance(cl, interfaceClasses, this);
            }
        }
    }

    @Override
    public void interfaceDisconnected(IMyxName interfaceName, Object serviceObject) {
        if (interfaceName.equals(REQUIRED_INTERFACE_NAME)) {
            synchronized (this) {
                List<Object> l = new ArrayList<Object>(Arrays.asList(trueServiceObjects));
                l.remove(serviceObject);
                trueServiceObjects = l.toArray(new Object[l.size()]);
                if (trueServiceObjects.length == 0) {
                    trueServiceObjects = null;
                }
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // if the given arguments contain message objects we search for
        // init-messages
        for (Object object : args) {
            if (object instanceof Message<?>) {
                Message<?> msg = (Message<?>) object;
                if (msg.getType() == Type.INIT) {
                    initCalls.add(new Tuple<Method, Object[]>(method, args));
                }
            }
        }
        return super.invoke(proxy, method, args);
    }

}
