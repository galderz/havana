package jawa.net;

import java.net.MalformedURLException;
import java.net.URL;

public class URLing
{
    public static void main(String[] args) throws MalformedURLException
    {
        var urlText = "https://github.com/quarkusio/quarkus/tree/master";
        var url = new URL(urlText);
        System.out.println(url.getPath());

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
    }
}
