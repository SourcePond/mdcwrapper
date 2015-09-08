package ch.sourcepond.utils.mdcwrapper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * <p>
 * Simple facade to wrap executor services into SLF4J MDC aware proxies. See the
 * <a href="http://logback.qos.ch/manual/mdc.html#managedThreads">logback
 * documentation</a> for more information about mapped diagnostic context.
 * </p>
 * 
 * <p>
 * An executor proxy created through this facade will wrap any {@link Runnable}
 * or {@link Callable} task into a MDC-aware wrapper. This wrapper will copy the
 * MDC context from the master thread (the thread which committed the task to
 * the executor) to the pool-thread (the thread which executes the task) before
 * the original (wrapped) task is actually executed. After execution, the
 * wrapper will cleanup the MDC context on the pool-thread to avoid memory
 * leaks.
 * </p>
 * 
 * <p>
 * Collections of tasks are also taken into account when wrapping is performed.
 * </p>
 * 
 */
public interface MdcWrapper {

	/**
	 * <p>
	 * Wraps the executor specified into a MDC-aware proxy using the interface
	 * specified.
	 * </p>
	 * 
	 * <p>
	 * A proxy created through this method will surround any {@link Runnable} or
	 * {@link Callable} task into a MDC-aware wrapper. This wrapper will copy
	 * the MDC context from the master thread (the thread which committed the
	 * task to the executor) to the pool-thread (the thread which executes the
	 * task) before the original (wrapped) task is actually executed. After
	 * execution, the wrapper will cleanup the MDC context on the pool-thread to
	 * avoid memory leaks.
	 * </p>
	 * 
	 * @param <T>
	 *            Type of the executor to be wrapped, must be assignable from
	 *            {@link Executor}.
	 * @param pExecutor
	 *            Executor to wrapped, must not be {@code null}
	 * @param pInterface
	 *            The interface of the executor; will be used to create the
	 *            proxy, must not be {@code null}
	 * @return New wrapper, never {@code null}
	 * @throws NullPointerException
	 *             Thrown, if either argument is {@code null}
	 * @throws IllegalArgumentException
	 *             Thrown, if the class object specified is not an interface.
	 */
	<T extends Executor> T wrap(T pExecutor, Class<T> pInterface);

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
