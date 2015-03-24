package eu.activelogic.instrumentor.thread.vm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

import static org.objectweb.asm.Opcodes.ASM5;

public class ThreadClassEnchanser extends ClassVisitor {

	private final ClassVisitor cv;

	public ThreadClassEnchanser(ClassVisitor cv) {
		super(ASM5);
		this.cv = cv;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature,
				exceptions);
		if (mv == null)
			return null;
		if (name.equals("start")) {
			return new ThreadStartAdviceCreator(mv, access, name, desc);
		} else if (name.equals("<init>")) {
			if (desc.equals("(Ljava/lang/Runnable;)V")
					|| desc.equals("(Ljava/lang/Runnable;Ljava/lang/String;)V")) {
				ThreadConstructorAdviceCreator ret = new ThreadConstructorAdviceCreator(
						mv, access, name, desc, 1);
				return ret;
			} else if (desc.equals("(Ljava/lang/ThreadGroup;Ljava/lang/Runnable;)V")
					|| desc.equals("(Ljava/lang/ThreadGroup;Ljava/lang/Runnable;Ljava/lang/String;)V")
					|| desc.equals("(Ljava/lang/ThreadGroup;Ljava/lang/Runnable;Ljava/lang/String;J)V")) {
				ThreadConstructorAdviceCreator ret = new ThreadConstructorAdviceCreator(
						mv, access, name, desc, 2);
				return ret;
			} else {
				ThreadConstructorAdviceCreator ret = new ThreadConstructorAdviceCreator(
						mv, access, name, desc, -1);
				return ret;
			}
		}
		return mv;
	}

	public void visit(int arg0, int arg1, String arg2, String arg3,
			String arg4, String[] arg5) {
		cv.visit(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
		return cv.visitAnnotation(arg0, arg1);
	}

	public void visitAttribute(Attribute arg0) {
		cv.visitAttribute(arg0);
	}

	public void visitEnd() {
		cv.visitEnd();
	}

	public FieldVisitor visitField(int arg0, String arg1, String arg2,
			String arg3, Object arg4) {
		return cv.visitField(arg0, arg1, arg2, arg3, arg4);
	}

	public void visitInnerClass(String arg0, String arg1, String arg2, int arg3) {
		cv.visitInnerClass(arg0, arg1, arg2, arg3);
	}

	public void visitOuterClass(String arg0, String arg1, String arg2) {
		cv.visitOuterClass(arg0, arg1, arg2);
	}

	public void visitSource(String arg0, String arg1) {
		cv.visitSource(arg0, arg1);
	}

	public AnnotationVisitor visitTypeAnnotation(int arg0, TypePath arg1,
			String arg2, boolean arg3) {
		return cv.visitTypeAnnotation(arg0, arg1, arg2, arg3);
	}

}
