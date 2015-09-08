package ch.sourcepond.utils.mdcwrapper.impl;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author rolandhauser
 *
 */
public interface TestExecutorService extends ScheduledExecutorService {

	/**
	 * @param pRunnables
	 */
	void executeAllRunnables(Collection<? extends Runnable> pRunnables);

	/**
	 * @param pRunnables
	 */
	void executeAllRunnables(Runnable[] pRunnables);

	/**
	 * @param pRunnables
	 */
	void executeAllCallables(Collection<Callable<?>> pCallables);

	/**
	 * @param pRunnables
	 */
	void executeAllCallables(Callable<?>[] pCallables);
}
