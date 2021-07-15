package j.process;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DockerRunIt
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
//        var command = Arrays.asList(
//            "docker"
//            , "run"
//            , "-it"
//            , "--entrypoint"
//            , "/bin/sh"
//            , "mandrel-productization-builder"
//        );

        var command = Arrays.asList(
            "bash"
            , "-c"
            , "docker run -it --entrypoint /bin/sh mandrel-productization-builder > /dev/tty"
//            , ">"
//            , "/dev/tty"
        );

        Path outputDir = FileSystems.getDefault().getPath("/Users/g/1");

        Process process = new ProcessBuilder(command)
            .directory(outputDir.toFile())
            .inheritIO()
            .start();

        final var exitValue = process.waitFor();
        if (exitValue != 0)
            throw new RuntimeException("Process ended abruptly, exit value: " + exitValue);
    }

}
