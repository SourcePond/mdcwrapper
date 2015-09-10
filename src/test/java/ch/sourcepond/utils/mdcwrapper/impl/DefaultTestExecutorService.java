/*Copyright (C) 2015 Roland Hauser, <sourcepond@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
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
