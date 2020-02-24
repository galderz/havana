package j.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Replacing
{
    public static void main(String[] args)
    {
        System.out.println(System.getProperty("maven.base.url"));

        Path mxPy = Path.of("sandbox", "target", "mx.py");
        Path mxPyReplaced = Path.of("sandbox", "target", "mx.py.replaced");
        try (var lines = Files.lines(mxPy))
        {
            final var replaced = lines
                .map(modifyMavenBaselUrl())
                .collect(Collectors.toList());
            Files.write(mxPyReplaced, replaced);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static Function<String, String> modifyMavenBaselUrl()
    {
        return line ->
        {
            var mavenBaselURL = String.format("\"%s\"", System.getProperty("maven.base.url"));
            return line.contains("_mavenRepoBaseURLs")
                ? line.replaceFirst("\\[", String.format("[ %s,", mavenBaselURL))
                : line;
        };
    }

}
