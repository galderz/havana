package p.picocli;

import picocli.CommandLine;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;

public class ConvertingTypes
{
    public static void main(String[] args)
    {
        new CommandLine(new Test())
            .registerConverter(GitURI.class, GitURI::of)
            .setCaseInsensitiveEnumValuesAllowed(true)
            .execute(
                "-uri"
                , "https://github.com/galderz/camel-quarkus/tree/t_jaxb_1161"
                , "-uris"
                , "https://github.com/quarkusio/quarkus/tree/master"
                , "-uris"
                , "https://github.com/infinispan/infinispan/tree/master"
            );
    }

    @CommandLine.Command(
        name = "test"
        , aliases = {"t"}
        , description = "Test quarkus."
        , mixinStandardHelpOptions = true
    )
    static class Test implements Runnable
    {
        @CommandLine.Option(names = "-uri")
        GitURI uri;

        @CommandLine.Option(names = "-uris")
        List<GitURI> uris;

        @Override
        public void run()
        {
            System.out.printf("Test(uri=%s,uris=%s)%n", uri, uris);
        }
    }

    record GitURI(
        String organization
        , String name
        , String branch
        , String url
    )
    {
        static GitURI of(URI uri)
        {
            final var path = Path.of(uri.getPath());

            final var organization = path.getName(0).toString();
            final var name = path.getName(1).toString();
            final var branch = path.getFileName().toString();
            final var url = uri.resolve("..").toString();

            return new GitURI(organization, name, branch, url);
        }

        static GitURI of(String uri)
        {
            return GitURI.of(URI.create(uri));
        }
    }
}
