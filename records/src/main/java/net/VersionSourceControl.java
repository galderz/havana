package net;

import check.Assert;

import java.net.URI;
import java.nio.file.Path;

public class VersionSourceControl
{
    public static void main(String[] args)
    {
        Assert.check();
        openjdkVcs();
        mercurialVcs();
    }

    private static void mercurialVcs()
    {
        final var uri = URI.create("http://hg.openjdk.java.net/jdk8/jdk8/jdk");
        final var repo = Repository.of(uri);
        assert repo.organization.equals("openjdk") : repo.organization;
        assert repo.name.equals("jdk") : repo.name;
    }

    private static void openjdkVcs()
    {
        final var uri = URI.create("https://github.com/openjdk/jdk11u-dev/tree/master");
        final var repo = Repository.of(uri);
        assert repo.organization.equals("openjdk");
        assert repo.name.equals("jdk11u-dev");
    }

    record Repository(
        String organization
        , String name
    )
    {
        static Repository of(URI uri)
        {
            final var host = uri.getHost();
            final var path = Path.of(uri.getPath());
            if (host.equals("github.com"))
            {
                final var organization = path.getName(0).toString();
                final var name = path.getName(1).toString();
                return new Repository(organization, name);
            }
            else if (host.equals("hg.openjdk.java.net"))
            {
                final var organization = "openjdk";
                final var name = path.getFileName().toString();
                return new Repository(organization, name);
            }
            throw new IllegalStateException("Unknown repository type");
        }
    }

}
