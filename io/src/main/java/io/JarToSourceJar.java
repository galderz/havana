package io;

import java.nio.file.Path;

public class JarToSourceJar
{
    public static void main(String[] args)
    {
        System.out.println(
            toSourceJar(
                Path.of("/Users/g/.m2/repository/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5.jar")
            )
        );
    }

    static Path toSourceJar(Path path)
    {
        final var parent = path.getParent();
        final var fileName = path.getFileName().toString();
        final var extensionIndex = fileName.lastIndexOf('.');
        final var sourcesFileName = String.format("%s-sources.jar", fileName.substring(0, extensionIndex));
        return parent.resolve(sourcesFileName);
    }
}
