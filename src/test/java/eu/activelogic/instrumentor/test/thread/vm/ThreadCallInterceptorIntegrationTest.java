/*
 *    Copyright 2015 Aleksandr Panzin (alex@activelogic.eu)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.activelogic.instrumentor.test.thread.vm;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.activelogic.instrumentor.thread.vm.ThreadCallInterceptor;
import eu.activelogic.instrumentor.thread.vm.ThreadInterceptor;

/**
 * Some simple tests
 * 
 * @author Aleksandr Panzin
 */
public class ThreadCallInterceptorIntegrationTest {

	private static final String THREAD_NAME = "TEST-THREAD";

	static class MyInterceptor implements ThreadCallInterceptor {

		public Thread createdThread;
		public Thread startedThread;
		public Runnable createdRunnable;

		public void createThread(Thread t, Runnable r) {
			createdThread = t;
			createdRunnable = r;
		}

		public void callStart(Thread t) {
			startedThread = t;
		}
	};

	@Test
	public void newThreadCreationShouldInvokeTheInterceptor() {
		MyInterceptor m = new MyInterceptor();
		ThreadInterceptor.registerThreadInterceptor(m);

		Runnable r = new Runnable() {
			public void run() {

			}
		};

		Thread t = new Thread(r);

		assertTrue(t == m.createdThread);
		assertTrue(r == m.createdRunnable);
		assertNull(m.startedThread);

	}

	@Test
	public void newThreadCreationShouldInvokeTheInterceptorNoRunnable() {
		MyInterceptor m = new MyInterceptor();
		ThreadInterceptor.registerThreadInterceptor(m);

		Thread t = new Thread();

		assertTrue(t == m.createdThread);
		assertNull(m.createdRunnable);
		assertNull(m.startedThread);

	}

	@Test
	public void newThreadCreationShouldInvokeTheInterceptorNullRunnable() {
		MyInterceptor m = new MyInterceptor();
		ThreadInterceptor.registerThreadInterceptor(m);

		Runnable r = null;

		Thread t = new Thread(r);

		assertTrue(t == m.createdThread);
		assertNull(m.createdRunnable);
		assertNull(m.startedThread);

	}

	@Test
	public void newThreadCreationShouldInvokeTheInterceptorSubclass() {
		MyInterceptor m = new MyInterceptor();
		ThreadInterceptor.registerThreadInterceptor(m);

		Thread t = new Thread() {
			@Override
			public void run() {
				super.run();
			}
		};

		assertTrue(t == m.createdThread);
		assertNull(m.createdRunnable);
		assertNull(m.startedThread);

	}

	@Test
	public void threadStartShouldInvokeTheInterceptor() {
		MyInterceptor m = new MyInterceptor();
		ThreadInterceptor.registerThreadInterceptor(m);

		Runnable r = new Runnable() {
			public void run() {
				try {
					Thread.sleep(10l);
				} catch (InterruptedException e) {
				}
			}
		};

		Thread t = new Thread(r);
		t.start();

		assertTrue(t == m.createdThread);
		assertTrue(r == m.createdRunnable);
		assertTrue(t == m.startedThread);

	}

	@Test(expected = RuntimeException.class)
	public void threadStartExceptionShouldPropagete() {
		ThreadInterceptor
				.registerThreadInterceptor(new ThreadCallInterceptor() {

					@Override
					public void createThread(Thread t, Runnable r) {

					}

					@Override
					public void callStart(Thread t) {
						if (t.getName().equals(THREAD_NAME)) {
							throw new RuntimeException();
						}
					}
				});

		Thread t = new Thread(THREAD_NAME);
		t.start();
	}

	@Test(expected = RuntimeException.class)
	public void threadCreateExceptionShouldPropagete() {
		ThreadInterceptor
				.registerThreadInterceptor(new ThreadCallInterceptor() {

					@Override
					public void createThread(Thread t, Runnable r) {
						if (t.getName().equals(THREAD_NAME)) {
							throw new RuntimeException();
						}
					}

					@Override
					public void callStart(Thread t) {
					}
				});

		new Thread(THREAD_NAME);
	}

}
