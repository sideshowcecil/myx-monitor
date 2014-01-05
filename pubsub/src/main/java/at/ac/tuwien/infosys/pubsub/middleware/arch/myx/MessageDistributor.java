package at.ac.tuwien.infosys.pubsub.middleware.arch.myx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
        super.interfaceConnected(interfaceName, serviceObject);
        if (interfaceName.equals(REQUIRED_INTERFACE_NAME)) {
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
