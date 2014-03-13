package at.ac.tuwien.dsg.myx.util;

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
    private static String generateId(String prefix) {
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
}
