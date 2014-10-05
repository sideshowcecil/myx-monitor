package at.ac.tuwien.dsg.myx.monitor.aggregator.comp;

import at.ac.tuwien.dsg.concurrent.IdentifiableThreadPoolExecutor;

/**
 * An extension to the PubSub {@link MessageDistributor} where only one thread
 * is used to forward events.
 * 
 * @author bernd.rathmanner
 * 
 */
public class MessageDistributor extends at.ac.tuwien.dsg.pubsub.middleware.myx.MessageDistributor {

    public MessageDistributor() {
        executor = new IdentifiableThreadPoolExecutor(1);
    }

}
