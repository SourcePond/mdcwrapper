package ch.sourcepond.utils.mdcwrapper;

import javax.inject.Inject;

import ch.sourcepond.utils.mdcwrapper.impl.MdcWrapperIntegrationTest;

/**
 *
 */
public class MdcWrapperTest extends MdcWrapperIntegrationTest {

	/**
	 * 
	 */
	@Inject
	private MdcWrapper wrapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.impl.MdcWrapperTest#getWrapper()
	 */
	@Override
	protected MdcWrapper getWrapper() {
		return wrapper;
	}
}