package ch.sourcepond.utils.mdcwrapper.impl;

import static ch.sourcepond.utils.mdcwrapper.impl.Constants.MDC_KEY;
import static ch.sourcepond.utils.mdcwrapper.impl.Constants.MDC_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.MDC;

/**
 *
 */
public class MdcAwareRunnableTest extends MdcAwareTest<MdcAwareRunnable> {
	private final Runnable delegate = mock(Runnable.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.impl.MdcAwareTest#createMdcAware()
	 */
	@Override
	protected MdcAwareRunnable createMdcAware() {
		return new MdcAwareRunnable(delegate);
	}

	/**
	 * 
	 */
	@Test
	public void verifyRun() {
		doAnswer(new Answer<Object>() {

			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				// MDC context should have been copied before delegate run has
				// been invoked.
				assertEquals(MDC_VALUE, MDC.get(MDC_KEY));
				return null;
			}
		}).when(delegate).run();

		MDC.put(MDC_KEY, MDC_VALUE);
		final MdcAwareRunnable r = createMdcAware();
		r.run();

		// MDC should have been cleared after delegate run has been invoked.
		assertNull(MDC.get(MDC_KEY));
	}

}
