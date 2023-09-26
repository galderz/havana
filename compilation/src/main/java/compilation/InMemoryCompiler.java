package compilation;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Processor;
import javax.lang.model.element.Modifier;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class InMemoryCompiler
{
    static Class<?> compile(JavaFile java) {
        ClassLoader parent = java.getClass().getClassLoader();
        List<String> options = Collections.emptyList();
        List<Processor> processors = Collections.emptyList();
        ClassLoader loader = compile(java, parent, options, processors);
        try {
            String name = java.packageName;
            name = name.isEmpty() ? java.typeSpec.name : name + "." + java.typeSpec.name;
            return loader.loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("loading class failed after successful compilation?!", e);
        }
    }

    static ClassLoader compile(
        JavaFile java
        , ClassLoader parent
        , List<String> options
        , List<Processor> processors
    )
    {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assert compiler != null : "no system java compiler available - JDK is required!";
        final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        final StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(
            diagnostics
            , Locale.getDefault()
            , StandardCharsets.UTF_8
        );
        InMemoryFileManager manager = new InMemoryFileManager(standardFileManager, parent);
        JavaCompiler.CompilationTask task = compiler.getTask(
            null // a writer for additional output from the compiler; use System.err if null
            , manager
            , diagnostics
            , options
            , null // names of classes to be processed by annotation processing, null means no classes
            , Collections.singleton(java.toJavaFileObject())
        );

        if (!processors.isEmpty())
            task.setProcessors(processors);
        boolean success = task.call();

        if (!success)
            throw new RuntimeException("compilation failed" + diagnostics.getDiagnostics());

        return manager.getClassLoader(null);
    }
}
