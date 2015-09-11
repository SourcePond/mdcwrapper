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
package ch.sourcepond.utils.mdcwrapper.integrationtest.jsr330;

import static com.google.inject.Guice.createInjector;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;

import ch.sourcepond.utils.mdcwrapper.MdcWrapper;
import ch.sourcepond.utils.mdcwrapper.impl.DefaultMdcWrapper;
import ch.sourcepond.utils.mdcwrapper.impl.MdcWrapperTest;

/**
 *
 */
public class CdiMdcWrapperITCase extends MdcWrapperTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.impl.MdcWrapperTest#getWrapper()
	 */
	@Override
	protected MdcWrapper getWrapper() {
		final Injector injector = createInjector(new Module() {

			@Override
			public void configure(final Binder binder) {
				binder.bind(MdcWrapper.class).to(DefaultMdcWrapper.class);
			}
		});
		return injector.getInstance(MdcWrapper.class);
	}

}
