# thread-instrumentor
Java Thread Interceptor VM Plugin


This is a simple plugin for tracking Thread creations and `Thread.start()` invocations.

To use simply register your implementation of `ThreadCallInterceptor` with `ThreadInterceptor.registerThreadInterceptor`.
And add the libraries to the JVM startup(Oracle's and OpenJDK version shown):


    -javaagent:interceptor-1.0-jar-with-dependencies.jar -Xbootclasspath/a:interceptor-1.0-jar-with-dependencies.jar

    -javaagent:interceptor-1.0.jar -Xbootclasspath/a:interceptor-1.0.jar -Xbootclasspath/a:asm-all-5.0.3.jar


This is built with ASM 5 and ObjectWeb's ASM5 is packaged in "with dependencies". You can use the no dependency version as well, but don't forget to add ASM5 to bootclasspath. 

### Integrating

If you wish to integrate this with an already existing Java Agent, then you have to specify  the following in your agent JAR's Manifest:


    Can-Redefine-Classes: true 
    Can-Retransform-Classes: true

And then do something along the lines ( `premain` method contents) 

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
			
