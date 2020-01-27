package nativ.external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        var command = Arrays.asList("java", "-version");
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
