package at.ac.tuwien.dsg.myx;

import java.util.UUID;

public final class IdGenerator {

    private IdGenerator() {
    }

    /**
     * Generate a random architecture runtime id.
     * 
     * @return
     */
    public static String generateArchitectureRuntimeId() {
        return "ArchitectureRuntimeId-" + UUID.randomUUID();
    }
}
