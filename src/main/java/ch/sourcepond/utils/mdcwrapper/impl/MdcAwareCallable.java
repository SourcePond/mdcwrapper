package ch.sourcepond.utils.mdcwrapper.impl;

import static org.slf4j.MDC.clear;

import java.util.concurrent.Callable;

/**
 * @author rolandhauser
 *
 * @param <V>
 */
final class MdcAwareCallable<V> extends MdcAware implements Callable<V> {
	private final Callable<V> delegate;

	/**
	 * @param pDelegate
	 */
	public MdcAwareCallable(final Callable<V> pDelegate) {
		delegate = pDelegate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public V call() throws Exception {
		transferMDC();
		try {
			return delegate.call();
		} finally {
			clear();
		}
	}
}