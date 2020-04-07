package jawa.regex;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class FindOrExtract
{
    public static void main(String[] args) throws Exception
    {
        final var path = System.getProperty("path");

        try (var lines = Files.lines(Path.of(path)))
        {
            final var it = lines.iterator();
            while (it.hasNext())
            {
                final var line = it.next();

                final var matcher = Pattern
                    .compile(
                        "maven[^({|\\n)]*\\{"
                    )
                    .matcher(line);
                if (matcher.find())
                {
                    System.out.println(line);
                }
            }
        }
    }
}
