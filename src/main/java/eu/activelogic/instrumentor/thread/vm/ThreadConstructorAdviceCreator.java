package eu.activelogic.instrumentor.thread.vm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class ThreadConstructorAdviceCreator extends AdviceAdapter {

	private final int runnableIndex;

	public ThreadConstructorAdviceCreator(MethodVisitor mv, int access,
			String name, String desc, int runnableIndex) {
		super(Opcodes.ASM5, mv, access, name, desc);
		this.runnableIndex = runnableIndex;
	}

	@Override
	protected void onMethodEnter() {

	}

	@Override
	protected void onMethodExit(int opCode) {
		if (opCode == Opcodes.ATHROW)
			return;
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		if (runnableIndex < 0)
			mv.visitInsn(Opcodes.ACONST_NULL);
		else
			mv.visitVarInsn(Opcodes.ALOAD, runnableIndex);
		
		mv.visitMethodInsn(Opcodes.INVOKESTATIC,
				Type.getInternalName(ThreadInterceptor.class), 
				"createThread",
				"(Ljava/lang/Thread;Ljava/lang/Runnable;)V", 
				false);
	}

	public int getRunnableIndex() {
		return runnableIndex;
	}

}
