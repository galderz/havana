package jawa.net;

import java.net.URI;
import java.nio.file.Path;

public class URIing
{
    public static void main(String[] args)
    {
        shortenedURIs();
        enhancingURIs();
    }

    private static void shortenedURIs()
    {
        final var basic = URI.create(
            "github://quarkusio/quarkus/master"
        );
        System.out.println(basic);

        final var enhanced = URI.create(
            "github://quarkusio/quarkus/master/galderz/94a22d7"
        );
        System.out.println(enhanced);
    }

    private static void enhancingURIs()
    {
        final var basic = URI.create(
            "https://github.com/quarkusio/quarkus/tree/master"
        );
        System.out.println(basic);

        final var enhanced = URI.create(
            "https://github.com/quarkusio/quarkus/tree/master/galderz/94a22d7"
        );
        System.out.println(enhanced);
        System.out.println(enhanced.getPath());
        System.out.println(Path.of(enhanced.getPath()));
    }
}
