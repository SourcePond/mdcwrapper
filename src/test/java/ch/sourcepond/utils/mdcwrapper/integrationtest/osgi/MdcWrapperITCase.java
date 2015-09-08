package ch.sourcepond.utils.mdcwrapper.integrationtest.osgi;

import static ch.sourcepond.testing.bundle.OptionsHelper.defaultOptions;
import static java.lang.reflect.Proxy.isProxyClass;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import java.util.concurrent.ScheduledExecutorService;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

import ch.sourcepond.utils.mdcwrapper.MdcWrapper;
import ch.sourcepond.utils.mdcwrapper.impl.MdcWrapperTest;

/**
 * @author rolandhauser
 *
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class MdcWrapperITCase extends MdcWrapperTest {

	/**
	 * 
	 */
	@Inject
	private MdcWrapper wrapper;

	/**
	 * @return
	 * @throws Exception
	 */
	@Configuration
	public Option[] config() throws Exception {
		return options(mavenBundle("org.mockito", "mockito-all").versionAsInProject(), defaultOptions());
	}

	/**
	 * 
	 */
	@Test
	public void verifyWrapExecutor() {
		final ScheduledExecutorService service = wrapper.wrap(newScheduledThreadPool(1),
				ScheduledExecutorService.class);
		service.shutdown();
		assertTrue(isProxyClass(service.getClass()));
	}

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
