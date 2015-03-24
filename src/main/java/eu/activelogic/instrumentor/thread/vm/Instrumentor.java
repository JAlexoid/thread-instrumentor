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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.instrument.*;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * A Java VM agent to attach hooks to Thread creation and execution.
 * 
 * @author Aleksandr Panzin
 */
public class Instrumentor implements ClassFileTransformer {

	public static final String JAVA_LANG_THREAD = "java/lang/Thread";
	public static final String JAVA_LANG_THREAD_CLASS = "/java/lang/Thread.class";

	/**
	 * @see java.lang.instrument
	 */
	public static void premain(String agentArgs, Instrumentation inst) {
		try {
			inst.addTransformer(new Instrumentor(), true);

			InputStream in = Instrumentor.class.getResourceAsStream(JAVA_LANG_THREAD_CLASS);
			ByteArrayOutputStream baos = new ByteArrayOutputStream(in.available());
			byte[] read = new byte[255];
			int ready = 0;
			while ((ready = in.read(read)) >= 0) {
				if (ready > 0)
					baos.write(read, 0, ready);
			}
			in.close();
			ClassDefinition cld = new ClassDefinition(Thread.class, baos.toByteArray());
			inst.redefineClasses(new ClassDefinition[] { cld });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.instrument.ClassFileTransformer#transform(java.lang.ClassLoader, java.lang.String, java.lang.Class, java.security.ProtectionDomain, byte[])
	 */
	public byte[] transform(ClassLoader cl, String name, Class<?> clazz, ProtectionDomain pD, byte[] clazzBytes) throws IllegalClassFormatException {
		try {
			if (JAVA_LANG_THREAD.equals(name)) {
				ClassReader cr = new ClassReader(clazzBytes);
				ClassWriter cw = new ClassWriter(0);
				ThreadClassEnchanser ncv = new ThreadClassEnchanser(cw);
				cr.accept(ncv, ClassReader.EXPAND_FRAMES);
				return cw.toByteArray();
			} else
				return null;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

}
