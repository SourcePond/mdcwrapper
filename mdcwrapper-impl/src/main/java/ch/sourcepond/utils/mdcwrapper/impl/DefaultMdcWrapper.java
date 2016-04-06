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

import static java.lang.reflect.Proxy.getInvocationHandler;
import static java.lang.reflect.Proxy.isProxyClass;
import static java.lang.reflect.Proxy.newProxyInstance;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ch.sourcepond.utils.mdcwrapper.api.MdcWrapper;

/**
 * Default implementation of the {@link MdcWrapper} interface.
 *
 */
public class DefaultMdcWrapper implements BundleActivator, MdcWrapper {

	/**
	 * @param pToBeWrapped
	 * @param pLoader
	 * @param pInterface
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T createProxyIfNecessary(final T pToBeWrapped, final ClassLoader pLoader, final Class<T> pInterface) {
		final Class<?> cl = pToBeWrapped.getClass();

		// If the object is already a MDC-aware wrapper simply return it.
		if (isProxyClass(cl) && WrapInvocationHandler.class.equals(getInvocationHandler(pToBeWrapped).getClass())) {
			return pToBeWrapped;
		}
		return (T) newProxyInstance(pLoader, new Class<?>[] { pInterface }, new WrapInvocationHandler(pToBeWrapped));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.MdcWrapper#wrap(java.util.concurrent.
	 * Executor, java.lang.Class)
	 */
	@Override
	public <T extends Executor> T wrap(final T pExecutor, final Class<T> pInterface) {
		notNull(pExecutor, "Executor to wrapped is null!");
		notNull(pInterface, "Executor interface is null!");
		isTrue(pInterface.isInterface(), "Class specified is not an interface!");
		return createProxyIfNecessary(pExecutor, pInterface.getClassLoader(), pInterface);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.MdcWrapper#wrap(java.lang.Runnable)
	 */
	@Override
	public Runnable wrap(final Runnable pRunnable) {
		notNull(pRunnable, "Runnable to be wrapped is null!");

		// If the object is already a MDC-aware wrapper simply return it.
		if (MdcAwareRunnable.class.equals(pRunnable.getClass())) {
			return pRunnable;
		}

		return new MdcAwareRunnable(pRunnable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.MdcWrapper#wrap(java.util.concurrent.
	 * Callable)
	 */
	@Override
	public <V> Callable<V> wrap(final Callable<V> pCallable) {
		notNull(pCallable, "Callable to be wrapped is null!");

		// If the object is already a MDC-aware wrapper simply return it.
		if (MdcAwareCallable.class.equals(pCallable.getClass())) {
			return pCallable;
		}

		return new MdcAwareCallable<>(pCallable);
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
		return createProxyIfNecessary(pThreadFactory, pThreadFactory.getClass().getClassLoader(), ThreadFactory.class);
	}

	/**
	 * @param context
	 * @throws Exception
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		context.registerService(MdcWrapper.class, this, null);
	}

	/**
	 * @param context
	 * @throws Exception
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		// noop; service un-registation is done by the framework automatically
	}
}
