package at.ac.tuwien.dsg.myx.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * This class provides simple methods to get the correct ip address used in our
 * external connection identifiers.
 * 
 * @author bernd.rathmanner
 * 
 */
public final class IpResolver {

    private IpResolver() {
    }

    /**
     * Get the correct ip address based on the given socket. That is if is is
     * connected to a local address return the local ip, if is is connected to
     * an external address we return our public ip.
     * 
     * @param address
     * @return
     */
    public static String getLocalIp(Socket socket) {
        // if the connected sockets ip is not on a local network we use our
        // external ip
        if (!socket.getInetAddress().isLoopbackAddress() && !socket.getInetAddress().isSiteLocalAddress()) {
            // if not we obtain and use our external ip
            String ip = getExternalIp();
            if (ip != null && ip.length() > 0)
                return ip;
            // as a fallback we return our local address if we cannot obtain the
            // external address.
        } else if (socket.getInetAddress().isLoopbackAddress()) {
            // if we are connected to localhost we use it
            return socket.getLocalAddress().getHostAddress();
        }
        // if all fails or if we are connected to a local network we return our
        // first found local address, if one exists
        String[] addresses = getLocalAddresses();
        if (addresses.length > 0) {
            return addresses[0];
        }
        // as a final fallback we return the address of localhost
        return socket.getLocalAddress().getHostAddress();
    }

    /**
     * Get the public ip of the current host.
     * 
     * @return
     * @throws Exception
     */
    public static String getExternalIp() {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com").openStream()));
            return in.readLine();
        } catch (IOException e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    /**
     * Get all addresses of our connected network cards.
     * 
     * @return
     */
    public static String[] getLocalAddresses() {
        List<String> addresses = new ArrayList<>();
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface == null || networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;
                }
                Enumeration<InetAddress> interfaceAddresses = networkInterface.getInetAddresses();
                while (interfaceAddresses.hasMoreElements()) {
                    InetAddress address = interfaceAddresses.nextElement();
                    if (address.isSiteLocalAddress()) {
                        addresses.add(address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
        }
        return addresses.toArray(new String[addresses.size()]);
    }

}
