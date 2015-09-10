package ch.sourcepond.utils.mdcwrapper;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * <p>
 * Simple facade to wrap executor services into SLF4J MDC aware proxies. See the
 * <a href="http://logback.qos.ch/manual/mdc.html#managedThreads">logback
 * documentation</a> for more information about mapped diagnostic context.
 * </p>
 * 
 * <p>
 * A proxy created through this facade will wrap any {@link Runnable} or
 * {@link Callable} task into a MDC-aware wrapper. This wrapper will copy the
 * MDC context from the master thread (the thread which committed the task to
 * the executor) to the pool-thread (the thread which executes the task) before
 * the original task is actually executed. After execution, the wrapper will
 * cleanup the MDC context on the pool-thread to avoid memory leaks.
 * </p>
 * 
 * <p>
 * Collections and one-dimensional arrays of tasks are also taken into account
 * when wrapping is performed.
 * </p>
 * 
 */
public interface MdcWrapper {

	/**
	 * <p>
	 * Wraps the executor specified into a MDC-aware proxy using the executor
	 * interface specified.
	 * </p>
	 * 
	 * <p>
	 * The created executor proxy will wrap tasks passed to its methods in
	 * following cases:
	 * <ul>
	 * <li>The method takes one or more {@link Runnable} as argument.</li>
	 * <li>The method takes one or more {@link Callable} as argument.</li>
	 * <li>The method takes one or more {@link Collection} of {@link Runnable}
	 * as argument.</li>
	 * <li>The method takes one or more {@link Collection} of {@link Callable}
	 * as argument.</li>
	 * <li>The method takes one or more one-dimensional array of
	 * {@link Runnable} as argument.</li>
	 * <li>The method takes one or more one-dimensional array of
	 * {@link Callable} as argument.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * If a collection of tasks is passed to a method on the proxy, all
	 * contained tasks will be wrapped and added to a new collection. The new
	 * collection will eventually passed to the according method on the original
	 * executor. If an array of tasks is passed to a method on the proxy, all
	 * contained tasks will be wrapped. The wrapped tasks will replace the
	 * original tasks in the parameter array. In contrast to collections,
	 * parameter arrays will not be duplicated.
	 * </p>
	 * 
	 * @param <T>
	 *            Type of the executor to be wrapped, must be assignable from
	 *            {@link Executor}.
	 * @param pExecutor
	 *            Executor to be proxied, must not be {@code null}
	 * @param pInterface
	 *            The interface of the executor; will be used to create the
	 *            proxy, must not be {@code null}
	 * @return New MDC-aware executor proxy, never {@code null}
	 * @throws NullPointerException
	 *             Thrown, if either argument is {@code null}
	 * @throws IllegalArgumentException
	 *             Thrown, if the class specified is not an interface, or, is
	 *             not assignable from {@link Executor}
	 */
	<T extends Executor> T wrap(T pExecutor, Class<T> pInterface);

	/**
	 * Wraps the thread-factory specified into a MDC-aware proxy. Every task
	 * passed to the {@link ThreadFactory#newThread(Runnable)} method on the
	 * proxy will be wrapped before it is eventually passed to the original
	 * thread-factory specified.
	 * 
	 * @param pThreadFactory
	 *            Thread-factory to be proxied, must not be {@code null}
	 * @return New MDC-aware thread-factory proxy, never {@code null}
	 */
	ThreadFactory wrap(ThreadFactory pThreadFactory);

	/**
	 * @param pRunnable
	 * @return
	 */
	Runnable wrap(Runnable pRunnable);

	/**
	 * @param pCallback
	 * @return
	 */
	<V> Callable<V> wrap(Callable<V> pCallback);
}
