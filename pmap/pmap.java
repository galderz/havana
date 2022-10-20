///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.5.0

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Command(
    name = "pmap"
    , mixinStandardHelpOptions = true
    , version = "pmap 0.1"
    , description = "pmap made with jbang"
)
class pmap implements Callable<Integer>
{
    @Parameters(index = "0", description = "The greeting to print", defaultValue = "World!")
    private File file;

    public static void main(String... args)
    {
        int exitCode = new CommandLine(new pmap()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception
    {
        System.out.printf("%30s\t%15s%n", "Mapping", "Total RSS");
        try (var lines = Files.lines(Path.of(file.getPath())))
        {
            lines
                .skip(1)
                .filter(l -> Character.isDigit(l.charAt(0)))
                .map(Mapping::fromLine)
                .collect(
                    Collectors.groupingBy(
                        m -> m.name
                        , Collectors.summingLong(m -> m.rss)
                    )
                )
                .entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry -> System.out.printf(
                    "%30s\t%15s%n"
                    , entry.getKey()
                    , entry.getValue()
                ));
        }

        return 0;
    }

    record Mapping(String name, long rss)
    {
        static Mapping fromLine(String line)
        {
            final String[] elements = line.split("\\s+");
            if (elements.length == 6)
            {
                return new Mapping(elements[5], Long.parseLong(elements[2]));
            }
            else if (line.contains("[ anon ]"))
            {
                return new Mapping("[ anon ]", Long.parseLong(elements[2]));
            }
            else if (line.contains("[ stack ]"))
            {
                return new Mapping("[ stack ]", Long.parseLong(elements[2]));
            }

            throw new IllegalStateException(String.format(
                "Unknown line format (%d): %s"
                , elements.length
                , line
            ));
        }
    }
}
