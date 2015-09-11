package ch.sourcepond.utils.mdcwrapper.integrationtest.jsr330;

import static com.google.inject.Guice.createInjector;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;

import ch.sourcepond.utils.mdcwrapper.MdcWrapper;
import ch.sourcepond.utils.mdcwrapper.impl.DefaultMdcWrapper;
import ch.sourcepond.utils.mdcwrapper.impl.MdcWrapperTest;

/**
 * @author rolandhauser
 *
 */
public class CdiMdcWrapperITCase extends MdcWrapperTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.impl.MdcWrapperTest#getWrapper()
	 */
	@Override
	protected MdcWrapper getWrapper() {
		final Injector injector = createInjector(new Module() {

			@Override
			public void configure(final Binder binder) {
				binder.bind(MdcWrapper.class).to(DefaultMdcWrapper.class);
			}
		});
		return injector.getInstance(MdcWrapper.class);
	}

}
