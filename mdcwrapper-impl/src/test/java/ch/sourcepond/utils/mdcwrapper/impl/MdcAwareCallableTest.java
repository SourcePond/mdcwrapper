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
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doAnswer;

import java.util.concurrent.Callable;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.MDC;

/**
 *
 */
public class MdcAwareCallableTest extends MdcAwareTest<MdcAwareCallable<Object>> {
	private static final Object OBJ = new Object();
	@SuppressWarnings("unchecked")
	private final Callable<Object> delegate = Mockito.mock(Callable.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.impl.MdcAwareTest#createMdcAware()
	 */
	@Override
	protected MdcAwareCallable<Object> createMdcAware() {
		return new MdcAwareCallable<>(delegate);
	}

	/**
	 * 
	 */
	@Test
	public void verifyCallable() throws Exception {
		doAnswer(new Answer<Object>() {

			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				// MDC context should have been copied before delegate run has
				// been invoked.
				assertEquals(MDC_VALUE, MDC.get(MDC_KEY));
				return OBJ;
			}
		}).when(delegate).call();

		MDC.put(MDC_KEY, MDC_VALUE);
		final MdcAwareCallable<Object> r = createMdcAware();
		assertSame(OBJ, r.call());

		// MDC should have been cleared after delegate run has been invoked.
		assertNull(MDC.get(MDC_KEY));
	}
}
