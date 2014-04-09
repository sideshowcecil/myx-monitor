package at.ac.tuwien.dsg.myx.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.UUID;

/**
 * Util class to handle the dynamic generation of runtime ids.
 * 
 * @author bernd.rathmanner
 * 
 */
public final class IdGenerator {

    private static long counter = 0;

    private IdGenerator() {
    }

    /**
     * Generate a random id with the given prefix.
     * 
     * @param prefix
     * @return
     */
    public static String generateId(String prefix) {
        StringBuilder id = new StringBuilder(prefix);
        if (prefix.length() > 0 && !prefix.endsWith("-")) {
            id.append("-");
        }
        id.append(UUID.randomUUID());

        return id.toString();
    }

    /**
     * Generate a random architecture runtime id.
     * 
     * @return
     */
    public static String generateArchitectureRuntimeId() {
        return generateId("ArchitectureRuntimeId-");
    }

    /**
     * Generate a unique runtime instantiation id.
     * 
     * @param blueprintId
     * @return
     */
    public static String generateRuntimeInstantiationId(String blueprintId) {
        return blueprintId + "-" + ++counter;
    }

    /**
     * Get the id of the host which should be the same for each invocation.
     * 
     * @return the id of the host or <code>null</code> if it could not be
     *         obtained.
     */
    public static String getHostId() {
        // we first try to generate the host id from the machines hostname
        try {
            return UUID.nameUUIDFromBytes(InetAddress.getLocalHost().getHostName().getBytes()).toString();
        } catch (UnknownHostException e) {
        }

        // if we cannot use the hostname we try to get the id from a mac address
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface == null || networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;
                }
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac == null || mac.length == 0)
                    continue;

                StringBuilder sb = new StringBuilder();
                int zeroFieldCount = 0;
                for (int i = 0; i < mac.length; i++) {
                    if (mac[i] == 0)
                        zeroFieldCount++;
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }

                if (zeroFieldCount > 4)
                    continue;

                return UUID.nameUUIDFromBytes(sb.toString().getBytes()).toString();
            }
        } catch (SocketException e) {
        }

        // the id could not be obtained
        return null;
    }

    /**
     * Generate an id from the given connection identifier. This method should
     * be used to shorten long connection identifieres.
     * 
     * @param fullConnectionIdentifier
     * @return
     */
    public static String generateConnectionIdentifier(String fullConnectionIdentifier) {
        return UUID.nameUUIDFromBytes(fullConnectionIdentifier.getBytes()).toString();
    }
}
