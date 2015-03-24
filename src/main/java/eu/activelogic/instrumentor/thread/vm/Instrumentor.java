package eu.activelogic.instrumentor.thread.vm;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * 
 * 
 * @author Aleksandr Panzin
 */
public class Instrumentor implements ClassFileTransformer {

	public static void premain(String agentArgs, Instrumentation inst) {
		try {
			inst.addTransformer(new Instrumentor(), true);

			InputStream in = Instrumentor.class.getResourceAsStream("/java/lang/Thread.class");
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

	public byte[] transform(ClassLoader cl, String name, Class<?> clazz, ProtectionDomain pD, byte[] clazzBytes) throws IllegalClassFormatException {
		try {
			if (name.equals("java/lang/Thread")) {
				System.out.println("Redefining Thread class");
				ClassReader cr = new ClassReader(clazzBytes);
				ClassWriter cw = new ClassWriter(0);
				ThreadClassEnchanser ncv = new ThreadClassEnchanser(cw);
				cr.accept(ncv, ClassReader.EXPAND_FRAMES);
				System.out.println("Redefined Thread class");
				return cw.toByteArray();
			} else
				return null;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

}
