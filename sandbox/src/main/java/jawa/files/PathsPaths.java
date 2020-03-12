package jawa.files;

import java.nio.file.Path;

public class PathsPaths
{
    public static void main(String[] args)
    {
        final var path = Path.of("home", "g", "mandrel-mandrel");
        System.out.println(path);
        final var last = path.getFileName().toString();
        System.out.println(last);
    }
}
