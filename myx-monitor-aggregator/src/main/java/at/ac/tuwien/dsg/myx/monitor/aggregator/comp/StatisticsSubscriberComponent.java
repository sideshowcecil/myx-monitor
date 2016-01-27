package at.ac.tuwien.dsg.myx.monitor.aggregator.comp;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import at.ac.tuwien.dsg.myx.monitor.aggregator.evaluation.StatisticsSubscriber;
import at.ac.tuwien.dsg.myx.monitor.aggregator.model.ModelRoot;
import at.ac.tuwien.dsg.myx.monitor.aggregator.myx.MyxInterfaceNames;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.util.MyxUtils;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.ISubscriber;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;

public class StatisticsSubscriberComponent extends AbstractMyxSimpleBrick {

    public static final IMyxName IN_ISUBSCRIBER = MyxInterfaceNames.ISUBSCRIBER;
    public static final IMyxName OUT_MODEL_ROOT = MyxInterfaceNames.MODEL_ROOT;

    private ISubscriber<Event> statisticsSubscriber;

    @Override
    public Object getServiceObject(IMyxName interfaceName) {
        if (interfaceName.equals(IN_ISUBSCRIBER)) {
            return statisticsSubscriber;
        }
        return null;
    }

    @Override
    public void init() {
        Properties initProps = MyxUtils.getInitProperties(this);
        String brickCountStatisticsFile = initProps.getProperty("brickCountStatisticsFile", null);
        String externalConnectionCountStatisticsFile = initProps.getProperty("externalConnectionCountStatisticsFile",
                null);
        String watchedBricksStatisticsFile = initProps.getProperty("watchedBricksStatisticsFile", null);
        Set<String> watchedBricks = new HashSet<String>();
        for (String key : initProps.stringPropertyNames()) {
            if (key.startsWith("watchedBrick")) {
                watchedBricks.add(initProps.getProperty(key));
            }
        }
        String hostStatisticsFile = initProps.getProperty("hostStatisticsFile", null);
        String eventStatisticsFile = initProps.getProperty("eventStatisticsFile", null);
        String hostCPUStatisticsFile = initProps.getProperty("hostCPUStatisticsFile", null);
        String hostMemoryStatisticsFile = initProps.getProperty("hostMemoryStatisticsFile", null);
        ModelRoot modelRoot = MyxUtils.getFirstRequiredServiceObject(this, OUT_MODEL_ROOT);
        String runtimeArchFile = initProps.getProperty("runtimeArchFile", null);

        statisticsSubscriber = new StatisticsSubscriber(modelRoot, runtimeArchFile, brickCountStatisticsFile,
                externalConnectionCountStatisticsFile, watchedBricksStatisticsFile, watchedBricks, hostStatisticsFile,
                eventStatisticsFile, hostCPUStatisticsFile, hostMemoryStatisticsFile);
    }

}
