package at.ac.tuwien.dsg.pubsub.middleware.myx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.ac.tuwien.dsg.myx.util.Tuple;
import at.ac.tuwien.dsg.pubsub.message.Message;
import at.ac.tuwien.dsg.pubsub.message.Message.Type;
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

    private Map<String, List<Tuple<Method, Object[]>>> initCalls = new HashMap<>();

    @Override
    public void interfaceConnected(IMyxName interfaceName, Object serviceObject) {
        if (interfaceName.equals(REQUIRED_INTERFACE_NAME)) {
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
                        asyncExecutor.execute(r);
                    }
                }
            }
        }
        super.interfaceConnected(interfaceName, serviceObject);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // if the given arguments contain message objects we search for
        // init-messages
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
        return super.invoke(proxy, method, args);
    }

}
