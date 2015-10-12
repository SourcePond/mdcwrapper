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
package ch.sourcepond.utils.mdcwrapper;

import static ch.sourcepond.testing.bundle.OptionsHelper.defaultOptions;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;

import javax.inject.Inject;

import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;

import ch.sourcepond.utils.mdcwrapper.impl.MdcWrapperIntegrationTest;

/**
 *
 */
public class OSGiMdcWrapperTest extends MdcWrapperIntegrationTest {

	/**
	 * 
	 */
	@Inject
	private MdcWrapper wrapper;

	/**
	 * @return
	 * @throws Exception
	 */
	@Configuration
	public Option[] config() throws Exception {
		return options(mavenBundle("ch.sourcepond.utils", "mdcwrapper-api").versionAsInProject(),
				mavenBundle("ch.sourcepond.utils", "mdcwrapper-impl").versionAsInProject(),
				mavenBundle("org.objenesis", "objenesis").versionAsInProject(),
				wrappedBundle(maven("ch.sourcepond.utils", "mdcwrapper-impl").classifier("tests")).imports(
						"org.hamcrest.*;resolution:=optional,org.ops4j.pax.exam.*;resolution:=optional,ch.sourcepond.testing.bundle;resolution:=optional,*"),
				defaultOptions());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.sourcepond.utils.mdcwrapper.impl.MdcWrapperTest#getWrapper()
	 */
	@Override
	protected MdcWrapper getWrapper() {
		return wrapper;
	}
}
