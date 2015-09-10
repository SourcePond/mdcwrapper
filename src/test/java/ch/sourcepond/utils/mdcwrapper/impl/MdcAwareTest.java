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
