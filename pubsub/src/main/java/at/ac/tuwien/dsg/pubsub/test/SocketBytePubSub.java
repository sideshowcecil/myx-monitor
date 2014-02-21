package at.ac.tuwien.dsg.pubsub.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import at.ac.tuwien.dsg.pubsub.PubSubMiddleware;
import at.ac.tuwien.dsg.pubsub.middleware.arch.component.socket.SocketBytePublisherDispatcher;
import at.ac.tuwien.dsg.pubsub.middleware.arch.component.socket.SocketByteSubscriberDispatcher;

/**
 * Test class ot show how the PubSubMiddleware can be used.
 * 
 * @author bernd.rathmanner
 * 
 */
public class SocketBytePubSub {
    public static void main(String[] args) {

        PubSubMiddleware mw = new PubSubMiddleware();
        mw.addDispatcher(SocketBytePublisherDispatcher.class, SocketByteSubscriberDispatcher.class);

        // start the listeners
        mw.run();

        // wait till a button is pressed
        System.out.print("Press enter to exit...");
        try {
            (new BufferedReader(new InputStreamReader(System.in))).readLine();
        } catch (IOException e) {
        }

        mw.close();
    }
}
