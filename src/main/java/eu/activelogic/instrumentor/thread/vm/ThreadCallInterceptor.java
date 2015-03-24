package eu.activelogic.instrumentor.thread.vm;

public interface ThreadCallInterceptor {

	public void callStart(Thread t);

	public void createThread(Thread t, Runnable r);

}
