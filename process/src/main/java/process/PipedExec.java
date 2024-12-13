package process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class PipedExec
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        final List<String> execArgs = List.of(
            "/bin/sh"
            , "-c"
            , String.format(
                "strings %s| grep %s"
                , "/Users/galder/1/fibula-show/2410-unfibula/unfibula/target/benchmarks"
                , "com.oracle.svm.core.VM.Java.Version="
            )
        );

        final ProcessBuilder processBuilder = new ProcessBuilder(execArgs);

        final Process process = processBuilder.start();
        final BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
        final String line = output.readLine();
        int exitValue = process.waitFor();
        System.out.println("Exit value: " + exitValue);
        System.out.println(line);
    }
}
