package p.regex;

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

                var mavenMatcher = Pattern
                    .compile(
                        "\"maven\"[^({|\\n)]*\\{"
                    )
                    .matcher(line);
                if (mavenMatcher.find())
                {
                    System.out.println(line);
                }

                final var groupIdMatcher = Pattern
                    .compile(
                        "\"groupId\"[^\"]*\"([a-z0-9\\.]*)\""
                    )
                    .matcher(line);
                if (groupIdMatcher.find())
                {
                    System.out.println(line);
                    System.out.println(groupIdMatcher.group(1));
                }
            }
        }
    }
}
