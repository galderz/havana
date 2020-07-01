package net;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class URIing
{
    public static void main(String[] args) throws Exception
    {
        Assert.check();

        constructURI();

        extractWithBranchAndRemoteWithCommitId();
        extractWithBranch();
        extractShortest();

        shortenedURIs();
        enhancingURIs();
    }

    private static void constructURI() throws URISyntaxException
    {
        final var uri = new URI(
            "https"
            , "github.com"
            , Path.of("/","galderz", "jawa").toString()
            , null
        );
        assert uri.toString().equals("https://github.com/galderz/jawa") : uri.toString();
    }

    private static void extractWithBranchAndRemoteWithCommitId()
    {
        final var uri = URI.create(
            "github://quarkusio/quarkus/master/galderz/dbaa257"
        );

        final var organization = uri.getHost();
        final var path = Path.of(uri.getPath());
        final var repoName = path.getName(0);
        final var numPathElements = path.getNameCount();
        final var url = String.format("https://github.com/%s/%s", organization, repoName);
        final var branch = path.getName(1); // extra branch
        final var remoteName = path.getName(2); // extra commit id
        final var commitId = path.getName(3); // extra commit id

        assert numPathElements == 4 : numPathElements;
        assert organization.equals("quarkusio") : organization;
        assert repoName.toString().equals("quarkus") : repoName;
        assert url.equals("https://github.com/quarkusio/quarkus") : url;
        assert branch.toString().equals("master") : branch;
        assert remoteName.toString().equals("galderz") : commitId;
        assert commitId.toString().equals("dbaa257") : commitId;
    }

    private static void extractWithBranch()
    {
        final var uri = URI.create(
            "github://openjdk/jdk11u-dev/master"
        );

        final var organization = uri.getHost();
        final var path = Path.of(uri.getPath());
        final var repoName = path.getName(0);
        final var numPathElements = path.getNameCount();
        final var url = String.format("https://github.com/%s/%s", organization, repoName);
        final var branch = path.getName(1); // extra branch

        assert numPathElements == 2 : numPathElements;
        assert organization.equals("openjdk") : organization;
        assert repoName.toString().equals("jdk11u-dev") : repoName;
        assert url.equals("https://github.com/openjdk/jdk11u-dev") : url;
        assert branch.toString().equals("master") : branch;
    }

    private static void extractShortest()
    {
        final var uri = URI.create(
            "github://quarkusio/quarkus"
        );

        final var organization = uri.getHost();
        final var path = Path.of(uri.getPath());
        final var repoName = path.getName(0);
        final var numPathElements = path.getNameCount();
        final var url = String.format("https://github.com/%s/%s", organization, repoName);

        assert numPathElements == 1 : numPathElements;
        assert organization.equals("quarkusio") : organization;
        assert repoName.toString().equals("quarkus") : repoName;
        assert url.equals("https://github.com/quarkusio/quarkus") : url;
    }

    private static void shortenedURIs()
    {
        final var basic = URI.create(
            "github://quarkusio/quarkus/master"
        );
        assert basic.toString().equals(
            "github://quarkusio/quarkus/master"
        );

        final var enhanced = URI.create(
            "github://quarkusio/quarkus/master/galderz/94a22d7"
        );
        assert enhanced.toString().equals(
            "github://quarkusio/quarkus/master/galderz/94a22d7"
        );
    }

    private static void enhancingURIs()
    {
        final var basic = URI.create(
            "https://github.com/quarkusio/quarkus/tree/master"
        );
        assert basic.toString().equals(
            "https://github.com/quarkusio/quarkus/tree/master"
        );

        final var enhanced = URI.create(
            "https://github.com/quarkusio/quarkus/tree/master/galderz/94a22d7"
        );
        assert enhanced.toString().equals(
            "https://github.com/quarkusio/quarkus/tree/master/galderz/94a22d7"
        );
        assert enhanced.getPath().equals(
            "/quarkusio/quarkus/tree/master/galderz/94a22d7"
        );
        assert Path.of(enhanced.getPath()).toString().equals(
            "/quarkusio/quarkus/tree/master/galderz/94a22d7"
        );
    }
}
