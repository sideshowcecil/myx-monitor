package at.ac.tuwien.dsg.myx.monitor.comp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import at.ac.tuwien.dsg.myx.monitor.MyxProperties;
import at.ac.tuwien.dsg.myx.monitor.ed.EventDispatcher;
import at.ac.tuwien.dsg.myx.monitor.em.EventManager;
import at.ac.tuwien.dsg.myx.util.MyxUtils;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;

public class EventDispatcherComponent extends AbstractMyxSimpleBrick {

    public static final IMyxName INTERFACE_NAME_OUT_EVENTMANAGER = MyxUtils.createName("event-manager");

    List<EventDispatcher> dispatchers;
    ExecutorService executor;

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        return null;
    }

    @Override
    public void init() {
        Properties initProperties = MyxUtils.getInitProperties(this);
        EventManager eventManager = MyxUtils.getFirstRequiredServiceObject(this, INTERFACE_NAME_OUT_EVENTMANAGER);
        String[] dispatcherClassNames = (String[]) initProperties.get(MyxProperties.EVENT_DISPATCHER_CLASSES);
        dispatchers = getDispatchers(dispatcherClassNames, eventManager);
    }

    /**
     * Get all {@link EventDispatcher} instances.
     * 
     * @param eventManager
     * @param dispatcherClassNames
     */
    private List<EventDispatcher> getDispatchers(String[] dispatcherClassNames, EventManager eventManager) {
        List<EventDispatcher> dispatchers = new ArrayList<>();
        if (dispatcherClassNames != null) {
            for (String dispatcherClassName : dispatcherClassNames) {
                try {
                    Class<?> eventDispatcherClass = Class.forName(dispatcherClassName);
                    if (!EventDispatcher.class.isAssignableFrom(eventDispatcherClass)) {
                        // we ignore non compatible dispatchers
                        continue;
                    }
                    Constructor<?> c = eventDispatcherClass.getConstructor(EventManager.class);
                    dispatchers.add((EventDispatcher) c.newInstance(eventManager));
                } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
                        | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    // we ignore non compatible dispatchers
                }
            }
        }
        return dispatchers;
    }

    @Override
    public void begin() {
        if (!dispatchers.isEmpty()) {
            executor = Executors.newFixedThreadPool(dispatchers.size());
            for (EventDispatcher ed : dispatchers) {
                executor.execute(ed);
            }
        }
    }
    
    @Override
    public void end() {
        if (!dispatchers.isEmpty()) {
            executor.shutdownNow();
        }
    }

}
