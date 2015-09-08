package ch.sourcepond.utils.mdcwrapper.impl;

import static org.slf4j.MDC.clear;
import static org.slf4j.MDC.getCopyOfContextMap;
import static org.slf4j.MDC.setContextMap;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Base class for {@link Runnable} and {@link Callable} wrapper classes which
 * take care of <em>Mapped Diagnostic Context</em> (MDC) for SLF4J loggers.
 *
 */
@SuppressWarnings("rawtypes")
abstract class MdcAware {
	private final Map submittingThreadContext;

	/**
	 * @param pContextMap
	 */
	protected MdcAware() {
		// Store a copy of the current MDC context-map. The current MDC
		// context-map is provided by the thread which will commit this
		// object to a thread-pool.
		submittingThreadContext = getCopyOfContextMap();
	}

	/**
	 * Sets-up MCD for the child-thread.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected final void transferMDC() {
		// If the thread which submitted this object to the thread-pool did not
		// provide any context information, clear also the context of the
		// current thread.
		if (submittingThreadContext == null) {
			clear();
		} else {
			// If the thread which submitted this object to the thread-pool did
			// provide context information, set it on the thread-local
			// reference.
			setContextMap(submittingThreadContext);
		}
	}
}