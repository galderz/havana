package jawa.net;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

public class URLing
{
    public static void main(String[] args) throws MalformedURLException, URISyntaxException
    {
        extractFirstPath();
        extractFileName();
        extractURL();
        differentiateURLs();
    }

    private static void extractFirstPath() throws MalformedURLException
    {
        var url = new URL("https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-19.3.0/graalvm-ce-java8-linux-amd64-19.3.0.tar.gz");
        System.out.println(url.getPath());
        System.out.println(Path.of(url.getPath()).getName(0));
    }

    private static void extractFileName() throws MalformedURLException
    {
        var url = new URL("http://s01.yyz.redhat.com/~sgehwolf/mandrel/jdk-11/linux-x86_64/svm.tar.gz");
        System.out.println(url.getFile());
        System.out.println(Path.of(url.getFile()).getFileName());
    }

    private static void differentiateURLs() throws MalformedURLException
    {
        var url1 = new URL("https://github.com/openjdk/jdk11u");
        var url2 = new URL("https://github.com/graalvm/labs-openjdk-11");
        System.out.println(Path.of(url1.getPath()));
        System.out.println(Path.of(url2.getPath()));
        System.out.println(Path.of(url1.getPath()).getName(0));
        System.out.println(Path.of(url2.getPath()).getName(0));
    }

    private static void extractURL() throws MalformedURLException, URISyntaxException
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
        System.out.println(elements[2]);

        System.out.println("\nOption 2:");
        final var uri = url.toURI();
        System.out.println(uri);
        final var path = Path.of(uri.getPath());
        System.out.println(path.getFileName().toString());
        System.out.println(uri.resolve(".."));
        System.out.println(path.getName(1));
    }
}
