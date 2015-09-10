/*Copyright (C) 2015 Roland Hauser, <sourcepond@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
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
