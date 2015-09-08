package ch.sourcepond.utils.mdcwrapper.impl;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * @author rolandhauser
 *
 */
public class DefaultTestExecutorService extends ScheduledThreadPoolExecutor implements TestExecutorService {

	/**
	 * @param corePoolSize
	 * @param handler
	 */
	public DefaultTestExecutorService(final int corePoolSize, final RejectedExecutionHandler handler) {
		super(corePoolSize, handler);
	}

	/**
	 * @param corePoolSize
	 * @param threadFactory
	 * @param handler
	 */
	public DefaultTestExecutorService(final int corePoolSize, final ThreadFactory threadFactory,
			final RejectedExecutionHandler handler) {
		super(corePoolSize, threadFactory, handler);
	}

	/**
	 * @param corePoolSize
	 * @param threadFactory
	 */
	public DefaultTestExecutorService(final int corePoolSize, final ThreadFactory threadFactory) {
		super(corePoolSize, threadFactory);
	}

	/**
	 * @param corePoolSize
	 */
	public DefaultTestExecutorService(final int corePoolSize) {
		super(corePoolSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.impl.TestExecutorService#
	 * executeAllRunnables(java.util.Collection)
	 */
	@Override
	public void executeAllRunnables(final Collection<? extends Runnable> pRunnables) {
		for (final Runnable r : pRunnables) {
			execute(r);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.impl.TestExecutorService#
	 * executeAllRunnables(java.lang.Runnable[])
	 */
	@Override
	public void executeAllRunnables(final Runnable[] pRunnables) {
		for (final Runnable r : pRunnables) {
			execute(r);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.impl.TestExecutorService#
	 * executeAllCallables(java.util.Collection)
	 */
	@Override
	public void executeAllCallables(final Collection<Callable<?>> pCallables) {
		for (final Callable<?> c : pCallables) {
			submit(c);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.impl.TestExecutorService#
	 * executeAllCallables(java.util.concurrent.Callable[])
	 */
	@Override
	public void executeAllCallables(final Callable<?>[] pCallables) {
		for (final Callable<?> c : pCallables) {
			submit(c);
		}
	}
}
