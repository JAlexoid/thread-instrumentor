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
				Type.getInternalName(ThreadInterceptor.class), "createThread",
				"(Ljava/lang/Thread;Ljava/lang/Runnable;)V", false);
	}

	public int getRunnableIndex() {
		return runnableIndex;
	}

}
