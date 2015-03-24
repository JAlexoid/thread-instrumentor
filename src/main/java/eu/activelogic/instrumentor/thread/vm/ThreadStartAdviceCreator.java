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

public class ThreadStartAdviceCreator extends AdviceAdapter {

	private static final String START_METHOD_SIG = "(Ljava/lang/Thread;)V";
	private static final String START_METHOD = "callStart";

	public ThreadStartAdviceCreator(MethodVisitor mv, int access, String name,
			String desc) {
		super(Opcodes.ASM5, mv, access, name, desc);
	}

	@Override
	protected void onMethodEnter() {
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC,
				Type.getInternalName(ThreadInterceptor.class), START_METHOD,
				START_METHOD_SIG, false);
	}

	@Override
	protected void onMethodExit(int opcode) {

	}

}
