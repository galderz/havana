package org.example.jmh.generators.bc2bc;

import org.openjdk.jmh.generators.bytecode.JmhBytecodeGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JmhBc2BcGenerator
{
    public static void main(String... args) throws Exception
    {
        final Path buildDir = Path.of(args[0]);

        final Path sourceDirectory = buildDir.resolve("generated-sources").resolve("bc");
        final Path genClassesDir = buildDir.resolve("generated-classes");
        final Path classesDir = buildDir.resolve("classes");
        final List<Path> pathArgs = List.of(
            classesDir // compiled bytecode directory
            , sourceDirectory // output source directory
            , genClassesDir // output resources directory
        );

        final String[] bytecodeGenArgs = pathArgs.stream()
            .map(Path::toString)
            .toArray(String[]::new);

        JmhBytecodeGenerator.main(bytecodeGenArgs);

//        final File compiledBytecodeDirectory = new File(args[0]);
//        final File outputSourceDirectory = new File(args[1]);
//        final File outputResourceDirectory = new File(args[2]);
        // final File classDirectory = new File(args[3]);

//        final Path path = Path.of("/Users/galder/1/fibula-show/2406-team/jmh/target/generated-sources/bc/org/sample/jmh_generated/MyFirstBenchmark_helloWorld_jmhTest.java");
//        System.out.println(
//            path.getFileName().endsWith(".java")
//        );

        System.out.println();

        final Set<File> javaFiles = classFilesInDirectory(sourceDirectory);
        final List<Path> classPath = List.of(
            classesDir
            , Path.of(System.getProperty("user.home"))
                .resolve(".m2/repository/org/openjdk/jmh/jmh-core/1.37/jmh-core-1.37.jar")
        );
        new JavaCompilerTool().compile(javaFiles, classPath);
    }

    static Set<File> classFilesInDirectory(Path dir) throws IOException
    {
        try (Stream<Path> javaPaths = Files.find(
           dir
           , Integer.MAX_VALUE
           , (path, attr) -> path.toString().endsWith(".java")
        ))
        {
            return javaPaths.map(Path::toFile)
                .collect(Collectors.toSet());
        }
    }
}
