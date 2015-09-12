# Quick Overview
The mdcwrapper bundle provides a simple service to wrap specific objects (executors, thread-factories, runnables and callables) into SLF4J MDC aware proxies. See the Logback documentation at http://logback.qos.ch/manual/mdc.html#managedThreads for more information about mapped diagnostic context in conjunction with managed threads. A proxy created through this facade will wrap any java.lang.Runnable or java.util.concurrent.Callable task into a MDC-aware wrapper. The wrapper will copy the MDC context from the master thread (the thread which committed the task to the executor) to the pool-thread (the thread which executes the task) before the original task is actually executed. After execution, the wrapper will cleanup the MDC context on the pool-thread to avoid memory leaks.

## Installation
The bundle is currently not released and therefore not available from Maven Central.

## Examples
### Get an instance of MdcWrapper

Without any dependency injection mechanism:
```
MdcWrapper wrapper = new DefaultMdcWrapper();
```

With Google Guice; *DefaultMdcWrappper* is annotated with *@Named* and can also be used with Eclipse Sisu (see https://www.eclipse.org/sisu/).
```
Injector injector = Guice.createInjector(new Module() {

	@Override
	public void configure(final Binder binder) {
		// DefaultMdcWrapper is annotated with @Singleton
		binder.bind(MdcWrapper.class).to(DefaultMdcWrapper.class);
	}
});
...
MdcWrapper wrapper = injector.getInstance(MdcWrapper.class);
```

The mdcwrapper bundle contains an OSGi Bundle-Activator which exports a singleton instance of MdcWrapper as service to the OSGi service registry. You can get a reference to it with a ServiceTracker...
```
final ServiceTracker<MdcWrapper, MdcWrapper> tracker = new ServiceTracker<>(ctx, MdcWrapper.class, null);
tracker.open();
MdcWrapper wrapper = tracker.getService();
```

... or with OSGi Blueprint
```
<blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0">
	<reference id="wrapper" interface="ch.sourcepond.utils.mdcwrapper.MdcWrapper" />
</blueprint>
```

### Wrap an Executor service
```
ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
ScheduledExecutorService mdcAwareExecutor = wrapper.wrap(executor, ScheduledExecutorService.class);
```

### Wrap a Thread-factory
```
ThreadFactory factory = Executors.defaultThreadFactory();
ThreadFactory mdcAwareFactory = wrapper.wrap(factory);
```

### Wrap a single task
If you cannot wrap a whole executor service, or if you want to implement your own MDC mechanism, you can can use the following methods to wrap single tasks:

For runnables:
```
Runnable task = new Runnable() {

	@Override
	public void run() {
		// Do some stuff
	}
};
Runnable mdcAwareTask = wrapper.wrap(task);
```

For callables:
```
Callable<Object> task = new Callable<Object>() {

	@Override
	public Object call() throws Exception {
		// Do some stuff an return result
		return null;
	}
};
Callable<Object> mdcAwareTask = wrapper.wrap(task);
```