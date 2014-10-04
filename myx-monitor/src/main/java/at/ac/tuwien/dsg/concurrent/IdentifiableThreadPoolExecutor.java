package at.ac.tuwien.dsg.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Implementation of {@link IdentifiableExecutorService}.
 * 
 * @author bernd.rathmanner
 * 
 */
public class IdentifiableThreadPoolExecutor implements IdentifiableExecutorService {

    /**
     * A mapping of identifiers to the used {@link ExecutorService}.
     */
    final Map<Integer, ExecutorService> mapping = new HashMap<>();

    /**
     * The reals {@link ExecutorService} instances.
     */
    final ExecutorService[] executors;

    /**
     * Simple integer instance used for locking and {@link ExecutorService}
     * selection.
     */
    Integer c = 0;

    /**
     * Constructor.
     */
    public IdentifiableThreadPoolExecutor() {
        this(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Constructor.
     * 
     * @param size
     *            the amount of threads to use.
     */
    public IdentifiableThreadPoolExecutor(int size) {
        executors = new ExecutorService[size];
        for (int i = 0; i < size; i++) {
            executors[i] = Executors.newSingleThreadExecutor();
        }
    }

    @Override
    public void execute(Runnable command, int identifier) {
        if (mapping.containsKey(identifier)) {
            mapping.get(identifier).execute(command);
        } else {
            synchronized (c) {
                mapping.put(identifier, executors[c]);
                executors[c].execute(command);
                c = ++c % executors.length;
            }
        }
    }

    @Override
    public void execute(Runnable command) {
        synchronized (c) {
            executors[c].execute(command);
            c = ++c % executors.length;
        }
    }

    @Override
    public void shutdown() {
        for (ExecutorService executor : executors) {
            executor.shutdown();
        }
    }

    @Override
    public List<Runnable> shutdownNow() {
        List<Runnable> nonExecutedTasks = new ArrayList<>();
        for (ExecutorService executor : executors) {
            nonExecutedTasks.addAll(executor.shutdownNow());
        }
        return nonExecutedTasks;
    }

    @Override
    public boolean isShutdown() {
        return executors[0].isShutdown();
    }

    @Override
    public boolean isTerminated() {
        for (ExecutorService executor : executors) {
            if (!executor.isTerminated())
                return false;
        }
        return true;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        for (ExecutorService executor : executors) {
            if (!executor.awaitTermination(timeout, unit))
                return false;
        }
        return true;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        Future<T> futureResult;
        synchronized (c) {
            futureResult = executors[c].submit(task);
            c = ++c % executors.length;
        }
        return futureResult;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        Future<T> futureResult;
        synchronized (c) {
            futureResult = executors[c].submit(task, result);
            c = ++c % executors.length;
        }
        return futureResult;
    }

    @Override
    public Future<?> submit(Runnable task) {
        Future<?> futureResult;
        synchronized (c) {
            futureResult = executors[c].submit(task);
            c = ++c % executors.length;
        }
        return futureResult;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        List<Future<T>> futureResults;
        synchronized (c) {
            futureResults = executors[c].invokeAll(tasks);
            c = ++c % executors.length;
        }
        return futureResults;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException {
        List<Future<T>> futureResults;
        synchronized (c) {
            futureResults = executors[c].invokeAll(tasks, timeout, unit);
            c = ++c % executors.length;
        }
        return futureResults;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        T result;
        synchronized (c) {
            result = executors[c].invokeAny(tasks);
            c = ++c % executors.length;
        }
        return result;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        T result;
        synchronized (c) {
            result = executors[c].invokeAny(tasks, timeout, unit);
            c = ++c % executors.length;
        }
        return result;
    }
}
