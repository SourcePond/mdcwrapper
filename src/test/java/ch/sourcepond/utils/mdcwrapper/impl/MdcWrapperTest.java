package ch.sourcepond.utils.mdcwrapper.impl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Collection;
import java.util.concurrent.Callable;
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
 * @author rolandhauser
 *
 */
public abstract class MdcWrapperTest {

	private static class MdcTest {
		static final String MDC_KEY = "mdcKey";
		static final String MDC_VALUE = "mdcValue";
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
	@SuppressWarnings("rawtypes")
	private final Collection<Callable> callableCollection = asList((Callable) callable);

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
	@Test
	public void verifyWrapSingleRunnable() throws InterruptedException {
		MDC.put(MdcTest.MDC_KEY, MdcTest.MDC_VALUE);
		proxy.execute(runnable);
		runnable.verifyMdcValue();
	}

	/**
	 * 
	 */
	@Test
	public void verifyWrapSingleCallable() throws InterruptedException {
		MDC.put(MdcTest.MDC_KEY, MdcTest.MDC_VALUE);
		proxy.submit(callable);
		callable.verifyMdcValue();
	}

	/**
	 * 
	 */
	@Test
	public void verifyWrapRunnableCollection() throws InterruptedException {
		MDC.put(MdcTest.MDC_KEY, MdcTest.MDC_VALUE);
		proxy.executeAllRunnables(runnableCollection);
		runnable.verifyMdcValue();
	}

	/**
	 * 
	 */
	@Test
	public void verifyWrapRunnableArray() throws InterruptedException {
		MDC.put(MdcTest.MDC_KEY, MdcTest.MDC_VALUE);
		proxy.executeAllRunnables(runnables);
		runnable.verifyMdcValue();
	}

}
