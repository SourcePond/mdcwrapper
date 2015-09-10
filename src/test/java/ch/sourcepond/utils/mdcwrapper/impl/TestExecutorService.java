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

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author rolandhauser
 *
 */
public interface TestExecutorService extends ScheduledExecutorService {

	/**
	 * @param pRunnables
	 */
	void executeAllRunnables(Collection<? extends Runnable> pRunnables);

	/**
	 * @param pRunnables
	 */
	void executeAllRunnables(Runnable[] pRunnables);

	/**
	 * @param pRunnables
	 */
	void executeAllCallables(Collection<Callable<?>> pCallables);

	/**
	 * @param pRunnables
	 */
	void executeAllCallables(Callable<?>[] pCallables);

	/**
	 * @param pObj
	 */
	void unwrapped(Object pObj);

	/**
	 * @param pCollection
	 */
	void unwrappedCollection(Collection<Object> pCollection);

	/**
	 * @param pCollection
	 */
	void unwrappedArray(Object[] pArray);
}
