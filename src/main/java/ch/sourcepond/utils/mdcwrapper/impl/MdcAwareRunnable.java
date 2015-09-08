package ch.sourcepond.utils.mdcwrapper.impl;

import static org.slf4j.MDC.clear;

/**
 * @author rolandhauser
 *
 */
final class MdcAwareRunnable extends MdcAware implements Runnable {
	private final Runnable delegate;

	/**
	 * @param pDelegate
	 */
	public MdcAwareRunnable(final Runnable pDelegate) {
		delegate = pDelegate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		transferMDC();
		try {
			delegate.run();
		} finally {
			clear();
		}
	}
}