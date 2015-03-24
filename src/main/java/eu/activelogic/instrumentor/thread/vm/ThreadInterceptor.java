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

import java.util.ArrayList;


/**
 * The registry class that handles the {@link ThreadCallInterceptor}s and forwards the method calls.
 * 
 * @author Aleksandr Panzin
 */
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
