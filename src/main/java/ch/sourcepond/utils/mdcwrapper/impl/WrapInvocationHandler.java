package ch.sourcepond.utils.mdcwrapper.impl;

import static java.lang.reflect.Array.get;
import static java.lang.reflect.Array.getLength;
import static java.lang.reflect.Array.newInstance;
import static java.lang.reflect.Array.set;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;

/**
 * 
 *
 */
final class WrapInvocationHandler implements InvocationHandler {
	private final Object targetExecutor;

	/**
	 * @param pTargetExecutor
	 */
	WrapInvocationHandler(final Object pTargetExecutor) {
		targetExecutor = pTargetExecutor;
	}

	/**
	 * @param pArg
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object wrapIfNecessary(final Object pArg) {
		Object rc = pArg;
		if (pArg instanceof Runnable) {
			rc = new MdcAwareRunnable((Runnable) pArg);
		} else if (pArg instanceof Callable) {
			rc = new MdcAwareCallable<>((Callable) pArg);
		} else if (pArg instanceof Collection) {
			final Collection cl = (Collection) pArg;
			rc = wrapCollection(cl);
		} else if (pArg != null && pArg.getClass().isArray()) {
			rc = wrapArray(pArg);
		}

		return rc;
	}

	/**
	 * @param pArg
	 * @param rc
	 * @return
	 */
	private Object wrapArray(final Object pArg) {
		final int size = getLength(pArg);
		if (size > 0) {
			final Object firstValue = get(pArg, 0);
			if ((firstValue instanceof Runnable) || (firstValue instanceof Callable)) {
				final Object wrapped = newInstance(pArg.getClass().getComponentType(), size);
				set(wrapped, 0, wrapIfNecessary(firstValue));

				for (int i = 1; i < size; i++) {
					set(wrapped, i, get(pArg, i));
				}

				return wrapped;
			}
		}
		return pArg;
	}

	/**
	 * @param pArg
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object wrapCollection(final Collection pArg) {
		final Iterator it = pArg.iterator();
		if (it.hasNext()) {
			final Object n = it.next();
			if ((n instanceof Runnable) || (n instanceof Callable)) {
				final Collection wrapped = new ArrayList<>(pArg.size());
				wrapped.add(wrapIfNecessary(n));

				while (it.hasNext()) {
					wrapped.add(wrapIfNecessary(it.next()));
				}

				return wrapped;
			}
		}
		return pArg;
	}

	/**
	 * @param pArgs
	 * @return
	 */
	private Object[] wrap(final Object[] pArgs) {
		if (pArgs != null) {
			for (int i = 0; i < pArgs.length; i++) {
				pArgs[i] = wrapIfNecessary(pArgs[i]);
			}
		}
		return pArgs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		try {
			return method.invoke(targetExecutor, wrap(args));
		} catch (final InvocationTargetException e) {
			// Always throw original exception to avoid
			// UndeclaredThrowableExceptions
			throw e.getTargetException();
		}
	}
}
