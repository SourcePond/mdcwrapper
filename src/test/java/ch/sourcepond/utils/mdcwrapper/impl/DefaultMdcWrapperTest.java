package ch.sourcepond.utils.mdcwrapper.impl;

import ch.sourcepond.utils.mdcwrapper.MdcWrapper;

/**
 * @author rolandhauser
 *
 */
public class DefaultMdcWrapperTest extends MdcWrapperTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.impl.MdcWrapperTest#getWrapper()
	 */
	@Override
	protected MdcWrapper getWrapper() {
		return new DefaultMdcWrapper();
	}

}
