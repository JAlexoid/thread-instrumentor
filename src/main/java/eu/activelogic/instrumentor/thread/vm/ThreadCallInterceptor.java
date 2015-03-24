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

package eu.activelogic.instrumentor.thread.vm;

/**
 * An interface for the classes that wish to get notifications about all and any
 * Thread creations and start operations.
 * 
 * @author Aleksandr Panzin
 */
public interface ThreadCallInterceptor {

	/**
	 * Will be called right before the thread is started. Throwing any exception
	 * will be propagated to the caller of {@link Thread#start()}
	 * 
	 * @param t
	 *            the reference to the thread object that is started
	 */
	public void callStart(Thread t);

	/**
	 * When a new {@link Thread} object is created this method will be called as
	 * the last step of the constructor. For certain constructors, it will
	 * provide the {@link Runnable} object as well.<br/>
	 * 
	 * 
	 * Constructors that will provide a {@link Runnable}:
	 * {@link Thread#Thread(Runnable)} , {@link Thread#Thread(Runnable, String)}
	 * , {@link Thread#Thread(ThreadGroup, Runnable)},
	 * {@link Thread#Thread(ThreadGroup, Runnable, String)},
	 * {@link Thread#Thread(ThreadGroup, Runnable, String, long)}
	 * 
	 * @param t the {@link Thread} that has just been created( may not be initialised fully)
	 * @param r {@link Runnable} that is passed to the constructors that are covered(see above) or null
	 */
	public void createThread(Thread t, Runnable r);

}
