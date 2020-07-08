package p.picocli;

import picocli.CommandLine;

import java.util.List;

public class MultiParams
{
    public static void main(String[] args)
    {
        // As multiple <param><value> tuples
        // <param><value><value> does not work
        new CommandLine(new Test())
            .registerConverter(ConvertingTypes.GitURI.class, ConvertingTypes.GitURI::of)
            .setCaseInsensitiveEnumValuesAllowed(true)
            .execute(
                "-uris"
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
        @CommandLine.Option(names = "-uris")
        List<ConvertingTypes.GitURI> uris;

        @Override
        public void run()
        {
            System.out.printf("Test(uris=%s)%n", uris);
        }
    }
}
