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
