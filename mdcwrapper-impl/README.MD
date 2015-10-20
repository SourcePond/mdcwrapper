# Quick Overview
This bundle provides the default implementation of the MdcWrapper interface. For further information about the purpose of the API and how to use it, see its [documentation](https://github.com/SourcePond/mdcwrapper-api).

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