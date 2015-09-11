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
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import ch.sourcepond.utils.mdcwrapper.MdcWrapper;

/**
 *
 */
public abstract class MdcWrapperTest {

	/**
	 *
	 */
	private static class MdcTest {
		private final Lock lock = new ReentrantLock();
		private final Condition condition = lock.newCondition();
		private String mdcValue;

		/**
		 * 
		 */
		protected void setMdcValue() {
			lock.lock();
			try {
				mdcValue = MDC.get(MDC_KEY);
			} finally {
				condition.signalAll();
				lock.unlock();
			}
		}

		/**
		 * @throws InterruptedException
		 * 
		 */
		public void verifyMdcValue() throws InterruptedException {
			lock.lock();
			try {
				int count = 5;
				while (mdcValue == null && count-- > 0) {
					condition.await(100, TimeUnit.MILLISECONDS);
				}
				assertEquals(MDC_VALUE, mdcValue);
			} finally {
				MDC.clear();
				lock.unlock();
			}
		}
	}

	/**
	 * @author rolandhauser
	 *
	 */
	private static class MdcRunnable extends MdcTest implements Runnable {

		@Override
		public void run() {
			setMdcValue();
		}
	}

	/**
	 * @author rolandhauser
	 *
	 */
	private static class MdcCallback extends MdcTest implements Callable<Object> {

		@Override
		public Object call() throws Exception {
			setMdcValue();
			return null;
		}
	}

	private final MdcRunnable runnable = new MdcRunnable();
	private final MdcCallback callable = new MdcCallback();
	private final Runnable[] runnables = new Runnable[] { runnable };
	private final Collection<Runnable> runnableCollection = asList((Runnable) runnable);
	private final Callable<?>[] callables = new Callable<?>[] { callable };
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private final Collection<Callable<?>> callableCollection = (Collection) asList(callable);

	// Create test-service with exactly one thread
	private final TestExecutorService service = new DefaultTestExecutorService(1);
	private MdcWrapper wrapper;
	private TestExecutorService proxy;

	/**
	 * @return
	 */
	protected abstract MdcWrapper getWrapper();

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Before
	public void setup() throws Exception {
		wrapper = getWrapper();
		proxy = wrapper.wrap(service, TestExecutorService.class);

		// This is very important; the executor-service creates threads at that
		// time when tasks are submitted. If we don't do that here the threads
		// of the executor are created when the test Callables are submitted.
		// This would make the test pointless because the threads will then
		// inherit the MDC directly from the creator-thread.
		service.submit(mock(Callable.class)).get();
	}

	/**
	 * @throws InterruptedException
	 * 
	 */
	@After
	public void tearDown() throws InterruptedException {
		service.shutdown();
		service.awaitTermination(10, TimeUnit.SECONDS);
	}

	/**
	 * 
	 */
	/**
	 * 
	 */
	@Test(expected = NullPointerException.class)
	public void verifyExecutorIsNull() {
		wrapper.wrap(null, TestExecutorService.class);
	}

	/**
	 * 
	 */
	@Test(expected = NullPointerException.class)
	public void verifyExecutorInterfaceIsNull() {
		wrapper.wrap(service, null);
	}

	/**
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void verifyClassSpecifiedIsNotAnInterface() {
		wrapper.wrap((DefaultTestExecutorService) service, DefaultTestExecutorService.class);
	}

	/**
	 * 
	 */
	@Test(expected = NullPointerException.class)
	public void verifyWrapNullThreadFactory() {
		wrapper.wrap((ThreadFactory) null);
	}

	/**
	 * @throws InterruptedException
	 * 
	 */
	@Test
	public void verifyWrapThreadFactory() throws InterruptedException {
		final ThreadFactory factory = Executors.defaultThreadFactory();
		final ThreadFactory wrappedFactory = wrapper.wrap(factory);
		MDC.put(MDC_KEY, MDC_VALUE);
		final Thread th = wrappedFactory.newThread(runnable);
		th.start();
		th.join();
		runnable.verifyMdcValue();
	}

	/**
	 * 
	 */
	@Test(expected = NullPointerException.class)
	public void verifyWrapNullRunnable() {
		wrapper.wrap((Runnable) null);
	}

	/**
	 * @throws InterruptedException
	 * 
	 */
	@Test
	public void verifyWrapRunnable() throws InterruptedException {
		MDC.put(MDC_KEY, MDC_VALUE);
		service.execute(wrapper.wrap(runnable));
		runnable.verifyMdcValue();
	}

	/**
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test(expected = NullPointerException.class)
	public void verifyWrapNullCallable() {
		wrapper.wrap((Callable) null);
	}

	/**
	 * @throws InterruptedException
	 * 
	 */
	@Test
	public void verifyWrapCallable() throws InterruptedException {
		MDC.put(MDC_KEY, MDC_VALUE);
		service.submit(wrapper.wrap(callable));
		callable.verifyMdcValue();
	}

	/**
	 * 
	 */
	@Test
	public void verifyExecutorSingleRunnable() throws InterruptedException {
		MDC.put(MDC_KEY, MDC_VALUE);
		proxy.execute(runnable);
		runnable.verifyMdcValue();
	}

	/**
	 * 
	 */
	@Test
	public void verifyExecutorSingleCallable() throws InterruptedException {
		MDC.put(MDC_KEY, MDC_VALUE);
		proxy.submit(callable);
		callable.verifyMdcValue();
	}

	/**
	 * 
	 */
	@Test
	public void verifyExecutorCallableCollection() throws InterruptedException {
		MDC.put(MDC_KEY, MDC_VALUE);
		proxy.executeAllCallables(callableCollection);
		callable.verifyMdcValue();
	}

	/**
	 * 
	 */
	@Test
	public void verifyExecutorCallableArray() throws InterruptedException {
		MDC.put(MDC_KEY, MDC_VALUE);
		proxy.executeAllCallables(callables);
		callable.verifyMdcValue();
	}

	/**
	 * 
	 */
	@Test
	public void verifyExecutorRunnableCollection() throws InterruptedException {
		MDC.put(MDC_KEY, MDC_VALUE);
		proxy.executeAllRunnables(runnableCollection);
		runnable.verifyMdcValue();
	}

	/**
	 * 
	 */
	@Test
	public void verifyExecutorRunnableArray() throws InterruptedException {
		MDC.put(MDC_KEY, MDC_VALUE);
		proxy.executeAllRunnables(runnables);
		runnable.verifyMdcValue();
	}

}
