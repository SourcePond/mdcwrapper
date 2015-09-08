package ch.sourcepond.utils.mdcwrapper.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ch.sourcepond.utils.mdcwrapper.MdcWrapper;

/**
 * @author rolandhauser
 *
 */
public class MdcWrapperActivator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		context.registerService(MdcWrapper.class, new DefaultMdcWrapper(), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		// noop; service unregistration is done by framework
	}

}
