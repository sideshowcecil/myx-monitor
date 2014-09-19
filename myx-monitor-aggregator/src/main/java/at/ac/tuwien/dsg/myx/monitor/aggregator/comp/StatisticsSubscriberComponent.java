package at.ac.tuwien.dsg.myx.monitor.aggregator.comp;

import at.ac.tuwien.dsg.myx.monitor.aggregator.evaluation.StatisticsSubscriber;
import at.ac.tuwien.dsg.myx.monitor.aggregator.myx.MyxInterfaceNames;
import at.ac.tuwien.dsg.myx.monitor.em.events.Event;
import at.ac.tuwien.dsg.myx.util.MyxMonitoringUtils;
import at.ac.tuwien.dsg.pubsub.middleware.interfaces.ISubscriber;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;

public class StatisticsSubscriberComponent extends AbstractMyxSimpleBrick {

    public static final IMyxName IN_ISUBSCRIBER = MyxInterfaceNames.ISUBSCRIBER;

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
        String brickCountStatisticsFile = MyxMonitoringUtils.getInitProperties(this).getProperty(
                "brickCountStatisticsFile", null);
        String externalConnectionCountStatisticsFile = MyxMonitoringUtils.getInitProperties(this).getProperty(
                "externalConnectionCountStatisticsFile", null);
        statisticsSubscriber = new StatisticsSubscriber(brickCountStatisticsFile, externalConnectionCountStatisticsFile);
    }

}
