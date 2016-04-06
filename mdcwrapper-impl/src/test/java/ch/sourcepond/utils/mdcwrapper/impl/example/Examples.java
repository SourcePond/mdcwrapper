package ch.sourcepond.utils.mdcwrapper.impl.example;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import ch.sourcepond.utils.mdcwrapper.api.MdcWrapper;
import ch.sourcepond.utils.mdcwrapper.impl.DefaultMdcWrapper;

@SuppressWarnings("unused")
public class Examples {
	private final MdcWrapper wrapper = new DefaultMdcWrapper();

	public void getInstanceNoDependencyInjection() {
		final MdcWrapper wrapper = new DefaultMdcWrapper();
	}

	public void getInstanceFromBundleContext(final BundleContext ctx) {
		final ServiceTracker<MdcWrapper, MdcWrapper> tracker = new ServiceTracker<>(ctx, MdcWrapper.class, null);
		tracker.open();
		final MdcWrapper wrapper = tracker.getService();
	}

	public void wrapExecutor() {
		final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
		final ScheduledExecutorService mdcAwareExecutor = wrapper.wrap(executor, ScheduledExecutorService.class);
	}

	public void wrapThreadFactory() {
		final ThreadFactory factory = Executors.defaultThreadFactory();
		final ThreadFactory mdcAwareFactory = wrapper.wrap(factory);
	}

	public void wrapRunnable() {
		final Runnable task = new Runnable() {

			@Override
			public void run() {
				// Do some stuff
			}
		};
		final Runnable mdcAwareTask = wrapper.wrap(task);
	}

	public void wrapCallable() {
		final Callable<Object> task = new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				// Do some stuff an return result
				return null;
			}
		};
		final Callable<Object> mdcAwareTask = wrapper.wrap(task);
	}
}
