package at.ac.tuwien.dsg.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

/**
 * An extension to the {@link ExecutorService} interface that allow to 
 * execute tasks on specific {@link Thread}s using identifiers.
 */
public interface IdentifiableExecutorService extends ExecutorService {
    /**
     * Executes the given command at some time in the future. The command may
     * execute in a new thread, in a pooled thread, or in the calling thread, 
     * at the discretion of the {@code Executor} implementation. This method
     * guarantees that a {@link Runnable} with the same identifier is always
     * executed by the same {@link Thread}.
     * 
     * @param command
     *            the runnable task
     * @param identifier
     *            the identifier
     * @throws RejectedExecutionException
     *             if this task cannot be accepted for execution
     * @throws NullPointerException
     *             if command is null
     */
    void execute(Runnable command, int identifier);
}
