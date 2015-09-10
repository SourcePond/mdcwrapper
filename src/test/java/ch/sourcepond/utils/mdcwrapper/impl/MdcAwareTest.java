package ch.sourcepond.utils.mdcwrapper.impl;

import static ch.sourcepond.utils.mdcwrapper.impl.Constants.MDC_KEY;
import static ch.sourcepond.utils.mdcwrapper.impl.Constants.MDC_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.slf4j.MDC;

/**
 * @author rolandhauser
 *
 */
public abstract class MdcAwareTest<T extends MdcAware> {

	/**
	 * @return
	 */
	protected abstract T createMdcAware();

	/**
	 * 
	 */
	@Test
	public void verifyTransferMDC() {
		MDC.put(MDC_KEY, MDC_VALUE);
		final T mdcAware = createMdcAware();
		MDC.clear();
		mdcAware.transferMDC();
		assertEquals(MDC_VALUE, MDC.get(MDC_KEY));
	}

	/**
	 * 
	 */
	@Test
	public void verifyNoMdcToTransfer() {
		final T mdcAware = createMdcAware();
		MDC.put(MDC_KEY, MDC_VALUE);
		mdcAware.transferMDC();
		assertNull(MDC.get(MDC_KEY));
	}
}
