package eu.activelogic.instrumentor.test.thread.vm;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.activelogic.instrumentor.thread.vm.ThreadCallInterceptor;
import eu.activelogic.instrumentor.thread.vm.ThreadInterceptor;

public class ThreadCallInterceptorIntegrationTest {

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

}
