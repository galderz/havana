package jawa.net;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

public class URLing
{
    public static void main(String[] args) throws MalformedURLException, URISyntaxException
    {
        var urlText = "https://github.com/quarkusio/quarkus/tree/master";
        var url = new URL(urlText);

        System.out.println("Option 1:");
        System.out.println(url);
        final var elements = url.getPath().split("/");
        System.out.println(elements[elements.length - 1]);
        final var repo = String.format(
            "%s://%s/%s/%s"
            , url.getProtocol()
            , url.getAuthority()
            , elements[1]
            , elements[2]
        );
        System.out.println(repo);

        System.out.println("\nOption 2:");
        final var uri = url.toURI();
        System.out.println(uri);
        System.out.println(Path.of(uri.getPath()).getFileName().toString());
        System.out.println(uri.resolve(".."));
    }
}
