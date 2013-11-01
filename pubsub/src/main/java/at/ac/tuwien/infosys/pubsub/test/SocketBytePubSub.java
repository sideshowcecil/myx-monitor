package at.ac.tuwien.infosys.pubsub.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import at.ac.tuwien.infosys.pubsub.PubSubMiddleware;
import at.ac.tuwien.infosys.pubsub.middleware.socket.SocketBytePublisherListener;
import at.ac.tuwien.infosys.pubsub.middleware.socket.SocketByteSubscriberListener;

public class SocketBytePubSub {
    public static void main(String[] args) {
        int pubPort = 6666;
        int subPort = 6667;

        PubSubMiddleware mw = new PubSubMiddleware();
        mw.addListener(new SocketBytePublisherListener(pubPort),
                new SocketByteSubscriberListener(subPort));
        
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
