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

import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

/**
 *
 */
public class WrapInvocationHandlerTest {

	/**
	 *
	 * @param <T>
	 */
	private class TypeMatcher<T> implements ArgumentMatcher<T> {
		private final Class<?> mdcClass;

		/**
		 * @param pMdcClass
		 */
		TypeMatcher(final Class<?> pMdcClass) {
			mdcClass = pMdcClass;
		}

		@Override
		public boolean matches(final Object item) {
			return mdcClass.equals(item.getClass());
		}

		@Override
		public String toString() {
			return mdcClass.getName();
		}
	}

	/**
	 *
	 * @param <T>
	 */
	private class CollectionMatcher<T extends Collection<?>> extends TypeMatcher<T> {

		/**
		 * @param pMdcClass
		 */
		CollectionMatcher(final Class<?> pMdcClass) {
			super(pMdcClass);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public boolean matches(final Object item) {
			final Collection arg = (Collection) item;
			final Iterator it = arg.iterator();

			return arg.size() == 2 && super.matches(it.next()) && super.matches(it.next());
		}
	}

	private final TestExecutorService executor = mock(TestExecutorService.class);
	private final Runnable runnable = mock(Runnable.class);
	private final Runnable secondRunnable = mock(Runnable.class);
	private final Callable<?> callable = mock(Callable.class);
	private final Callable<?> secondCallable = mock(Callable.class);
	private TestExecutorService proxy;

	/**
	 * @throws Exception
	 */
	@Before
	public void setup() throws Exception {
		proxy = (TestExecutorService) newProxyInstance(getClass().getClassLoader(),
				new Class<?>[] { TestExecutorService.class }, new WrapInvocationHandler(executor));
	}

	/**
	 * 
	 */
	@Test
	public void verifyNullArg() {
		proxy.execute(null);
		verify(executor).execute(null);
	}

	/**
	 * 
	 */
	@Test
	public void verifyDoNotWrapObject() {
		final Object ob = new Object();
		proxy.unwrapped(ob);
		verify(executor).unwrapped(ob);
	}

	/**
	 * 
	 */
	@Test
	public void verifyNoArgMethod() {
		proxy.isShutdown();
		verify(executor).isShutdown();
	}

	/**
	 * @throws Throwable
	 */
	@Test
	public void verifyWrapRunnable() {
		proxy.execute(runnable);
		verify(executor).execute(Mockito.argThat(new TypeMatcher<MdcAwareRunnable>(MdcAwareRunnable.class)));
	}

	/**
	 * @throws Throwable
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void verifyWrapCallable() {
		proxy.submit(callable);
		verify(executor).submit(Mockito.argThat(new TypeMatcher<MdcAwareCallable>(MdcAwareCallable.class)));
	}

	/**
	 * @throws Throwable
	 */
	@Test
	public void verifyWrapEmptyCollection() {
		final List<Runnable> empty = new ArrayList<>();
		proxy.executeAllRunnables(empty);
		verify(executor).executeAllRunnables(empty);
	}

	/**
	 * @throws Throwable
	 */
	@Test
	public void verifyDoNotWrapIncompatibleCollection() {
		final List<Object> unwrapped = asList(new Object());
		proxy.unwrappedCollection(unwrapped);
		verify(executor).unwrappedCollection(unwrapped);
	}

	/**
	 * @throws Throwable
	 */
	@Test
	public void verifyWrapRunnableCollection() {
		final List<Runnable> collection = asList(runnable, secondRunnable);
		proxy.executeAllRunnables(collection);
		verify(executor).executeAllRunnables(
				Mockito.argThat(new CollectionMatcher<Collection<Runnable>>(MdcAwareRunnable.class)));
	}

	/**
	 * @throws Throwable
	 */
	@Test
	public void verifyWrapCallableCollection() {
		final List<Callable<?>> collection = asList(callable, secondCallable);
		proxy.executeAllCallables(collection);
		verify(executor).executeAllCallables(
				Mockito.argThat(new CollectionMatcher<Collection<Callable<?>>>(MdcAwareCallable.class)));
	}

	/**
	 * @throws Throwable
	 */
	@Test
	public void verifyWrapEmptyArray() {
		final Runnable[] empty = new Runnable[0];
		proxy.executeAllRunnables(empty);
		verify(executor).executeAllRunnables(empty);
	}

	/**
	 * @throws Throwable
	 */
	@Test
	public void verifyDoNotWrapIncompatibleArray() {
		final Object[] unwrapped = new Object[] { new Object() };
		proxy.unwrappedArray(unwrapped);
		verify(executor).unwrappedArray(unwrapped);
	}

	/**
	 * @throws Throwable
	 */
	@Test
	public void verifyWrapRunnableArray() {
		final Runnable[] array = new Runnable[] { runnable, secondRunnable };
		proxy.executeAllRunnables(array);
		verify(executor).executeAllRunnables(array);
		assertEquals(MdcAwareRunnable.class, array[0].getClass());
		assertEquals(MdcAwareRunnable.class, array[1].getClass());
	}

	/**
	 * @throws Throwable
	 */
	@Test
	public void verifyWrapCallableArray() {
		final Callable<?>[] array = new Callable<?>[] { callable, secondCallable };
		proxy.executeAllCallables(array);
		verify(executor).executeAllCallables(array);
		assertEquals(MdcAwareCallable.class, array[0].getClass());
		assertEquals(MdcAwareCallable.class, array[1].getClass());
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void verifyThrowTargetException() throws Exception {
		final RejectedExecutionException expected = new RejectedExecutionException();
		doThrow(expected).when(executor).execute(Mockito.argThat(new TypeMatcher<Runnable>(MdcAwareRunnable.class)));
		try {
			proxy.execute(runnable);
			fail("Exception expected");
		} catch (final RejectedExecutionException e) {
			assertSame(expected, e);
		}
	}
}
