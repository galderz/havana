package nativ.external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        final var graalVmHome = "/Users/g/.m2/repository/org/graalvm";
        final var version = "20.1.0-SNAPSHOT";
        final var jarSvm = String.format(
            "%s/nativeimage/svm/%s/svm-%s.jar"
            , graalVmHome, version, version
        );
        final var jarPointsTo = String.format(
            "%s/nativeimage/pointsto/%s/pointsto-%s.jar"
            , graalVmHome, version, version
        );
        final var jarSdk = String.format(
            "%s/sdk/graal-sdk/%s/graal-sdk-%s.jar"
            , graalVmHome, version, version
        );
        final var jarCompiler = String.format(
            "%s/compiler/compiler/%s/compiler-%s.jar"
            , graalVmHome, version, version
        );

        var classpath = String.format(
            "%s:%s:%s:%s"
            , jarSvm
            , jarPointsTo
            , jarSdk
            , jarCompiler
        );

        var imageClasspath = String.format(
            "%s"
            , jarSvm
        );

        var command = Arrays.asList(
            "java"
            , "-cp"
            , classpath
            , "com.oracle.svm.hosted.NativeImageGeneratorRunner$JDK9Plus"
            , "-imagecp"
            , imageClasspath
        );

        System.out.println(String.join(" ", command));

        var outputDir = Path.of(".");
        var errorReportLatch = new CountDownLatch(1);

        Process process = new ProcessBuilder(command)
            .directory(outputDir.toFile())
            .inheritIO()
            .start();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new ErrorReplacingProcessReader(
            process.getErrorStream()
            , outputDir.resolve("reports").toFile()
            , errorReportLatch)
        );
        executor.shutdown();
        errorReportLatch.await();
        if (process.waitFor() != 0) {
            throw new RuntimeException("Image generation failed. Exit code: " + process.exitValue());
        }
    }
}
