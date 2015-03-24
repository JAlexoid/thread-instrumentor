package eu.activelogic.instrumentor.thread.vm;

import java.util.ArrayList;

public class ThreadInterceptor {

	private static ArrayList<ThreadCallInterceptor> interceptors = new ArrayList<ThreadCallInterceptor>();

	public static void callStart(Thread t) {
		for (ThreadCallInterceptor i : interceptors) {
			i.callStart(t);
		}
	}

	public static void createThread(Thread t, Runnable r) {
		for (ThreadCallInterceptor i : interceptors) {
			i.createThread(t, r);
		}
	}

	public static void registerThreadInterceptor(ThreadCallInterceptor i) {
		interceptors.add(i);
	}

	public static void unregisterThreadInterceptor(ThreadCallInterceptor i) {
		interceptors.remove(i);
	}

}
