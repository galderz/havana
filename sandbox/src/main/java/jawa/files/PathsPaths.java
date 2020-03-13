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

        final var resources = Path.of("/Users", "blah", "1", "mandrel-packaging", "resources");
        System.out.println(resources);
        final var underResources = resources.resolve("blah-blah");
        System.out.println(underResources);
        final var siblingResources = resources.resolveSibling("target");
        System.out.println(siblingResources);
    }
}
