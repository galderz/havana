package file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Linking
{
    public static void main(String[] args) throws IOException
    {
        final var home = System.getProperty("user.home");
        final var link = Path.of(home, "workspace", "qollider", "2805", "test_link");
        final var target = Path.of("graalvm", "mandrel", "sdk", "latest_graalvm_home");
        Files.createSymbolicLink(link, target);
    }
}
