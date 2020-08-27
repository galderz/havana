package file;

import java.nio.file.Path;

public class PathsPaths
{
    public static void main(String[] args)
    {
        final var path = Path.of("home", "g", "mandrel-mandrel");
        System.out.println(path);
        System.out.println(path.resolve(Path.of("")));

        final var last = path.getFileName().toString();
        System.out.println(last);

        final var resources = Path.of("/Users", "blah", "1", "mandrel-packaging", "resources");
        System.out.println(resources);
        final var underResources = resources.resolve("blah-blah");
        System.out.println(underResources);
        final var siblingResources = resources.resolveSibling("target");
        System.out.println(siblingResources);

        final var mavenRepoHome = Path.of(System.getProperty("user.home"), ".m2", "repository");
        System.out.println(mavenRepoHome);

        final var downloadMarker = Path.of(System.getProperty("user.home"), "target", "quarkus-with-graal", "2020.03.20", "jdk", "download.marker");
        System.out.println(downloadMarker);
        System.out.println(downloadMarker.getParent().getFileName());

        final var relativePath = Path.of("sdk", "mxbuild", "dists", "jdk11", "graal-sdk");
        System.out.println(relativePath.getName(0));
        System.out.println(relativePath.getRoot());
    }
}
