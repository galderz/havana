package org.example.jmh.generators.bc2bc;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class JavaCompilerTool
{
    public void compile(Set<File> files, List<Path> classPath)
    {
        Asserts.needEnabledAsserts();

        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assert compiler != null : "no system java compiler available - JDK is required!";

        final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        try (final StandardJavaFileManager fileManager = compiler.getStandardFileManager(
            diagnostics
            , Locale.getDefault()
            , StandardCharsets.UTF_8
        ))
        {
            fileManager.setLocation(
                StandardLocation.CLASS_PATH
                , classPath.stream().map(Path::toFile).toList()
            );

            final Iterable<? extends JavaFileObject> compilationUnit = fileManager
                .getJavaFileObjects(files.toArray(new File[0]));

            final CompilationTask task = compiler.getTask(
                null // a writer for additional output from the compiler; use System.err if null
                , fileManager
                , diagnostics
                , null // no compiler options
                , null // names of classes to be processed by annotation processing, null means no classes
                , compilationUnit
            );

            boolean success = task.call();
            if (!success)
            {
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics())
                {
                    System.out.format("Error on line %d in %s: %s%n"
                        , diagnostic.getLineNumber()
                        , diagnostic.getSource().toUri()
                        , diagnostic.getMessage(Locale.ENGLISH)
                    );
                }
            }
            else
            {
                System.out.println("Compilation success.");
            }
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    public static void main(String[] args)
    {
        final Set<File> javaFiles = Set.of(Path.of("jmh-generator-bc2bc/src/test/java/HelloJmh.java").toFile());
        new JavaCompilerTool().compile(javaFiles, List.of());
    }
}
