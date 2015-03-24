package eu.activelogic.instrumentor.thread.vm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class ThreadStartAdviceCreator extends AdviceAdapter {

	public ThreadStartAdviceCreator(MethodVisitor mv, int access, String name,
			String desc) {
		super(Opcodes.ASM5, mv, access, name, desc);
	}

	@Override
	protected void onMethodEnter() {
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC,
				Type.getInternalName(ThreadInterceptor.class), "callStart",
				"(Ljava/lang/Thread;)V", false);
	}

	@Override
	protected void onMethodExit(int opcode) {

	}

}
