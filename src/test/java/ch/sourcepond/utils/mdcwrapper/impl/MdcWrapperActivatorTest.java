package ch.sourcepond.utils.mdcwrapper.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;
import org.osgi.framework.BundleContext;

import ch.sourcepond.utils.mdcwrapper.MdcWrapper;

/**
 * @author rolandhauser
 *
 */
public class MdcWrapperActivatorTest {
	private final MdcWrapper wrapper = mock(MdcWrapper.class);
	private final MdcWrapperActivator activator = new MdcWrapperActivator(wrapper);
	private final BundleContext context = mock(BundleContext.class);

	/**
	 * 
	 */
	@Test
	public void verifyDefaultConstructor() throws Exception {
		// Should not cause an exception to be thrown
		new MdcWrapperActivator().start(context);
	}

	/**
	 * 
	 */
	@Test
	public void verifyStart() throws Exception {
		activator.start(context);
		verify(context).registerService(MdcWrapper.class, wrapper, null);
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void verifyStop() throws Exception {
		activator.stop(context);
		verifyZeroInteractions(context);
	}
}
