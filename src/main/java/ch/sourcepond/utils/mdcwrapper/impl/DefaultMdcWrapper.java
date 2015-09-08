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
 * @author rolandhauser
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
		isTrue(Executor.class.isAssignableFrom(pInterface), "Interface specified is not assignable from {0}",
				Executor.class.getName());
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
		return (ThreadFactory) newProxyInstance(ThreadFactory.class.getClassLoader(),
				new Class<?>[] { ThreadFactory.class }, new WrapInvocationHandler(pThreadFactory));
	}
}
