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

import static java.lang.reflect.Proxy.newProxyInstance;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import ch.sourcepond.utils.mdcwrapper.MdcWrapper;

/**
 * Default implementation of the {@link MdcWrapper} interface.
 *
 */
@Named
@Singleton
public class DefaultMdcWrapper implements MdcWrapper {

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.MdcWrapper#wrap(java.util.concurrent.
	 * Executor, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Executor> T wrap(final T pExecutor, final Class<T> pInterface) {
		notNull(pExecutor, "Executor to wrapped is null!");
		notNull(pInterface, "Executor interface is null!");
		isTrue(pInterface.isInterface(), "Class specified is not an interface!");
		return (T) newProxyInstance(pInterface.getClassLoader(), new Class<?>[] { pInterface },
				new WrapInvocationHandler(pExecutor));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.MdcWrapper#wrap(java.lang.Runnable)
	 */
	@Override
	public Runnable wrap(final Runnable pRunnable) {
		notNull(pRunnable, "Runnable to be wrapped is null!");
		return new MdcAwareRunnable(pRunnable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.MdcWrapper#wrap(java.util.concurrent.
	 * Callable)
	 */
	@Override
	public <V> Callable<V> wrap(final Callable<V> pCallback) {
		notNull(pCallback, "Callable to be wrapped is null!");
		return new MdcAwareCallable<>(pCallback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.MdcWrapper#wrap(java.util.concurrent.
	 * ThreadFactory)
	 */
	@Override
	public ThreadFactory wrap(final ThreadFactory pThreadFactory) {
		notNull(pThreadFactory, "ThreadFactory to be wrapped is null!");
		return (ThreadFactory) newProxyInstance(pThreadFactory.getClass().getClassLoader(),
				new Class<?>[] { ThreadFactory.class }, new WrapInvocationHandler(pThreadFactory));
	}
}
