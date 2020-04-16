package jawa.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * -Dpath=...
 * --dependencies id=ASM_7.1,version=0.1,sha1=a1b2c3,sourceSha1=a2b3c4 id=JACOCOAGENT_0.8.4,version=0.2,sha1=d1e2f3
 */
public class TransformJsonManually
{
    public static void main(String[] args)
    {
        checkAssertEnabled();

        System.out.println("Args: " + Arrays.toString(args));
        final var argsMap = readArgs(args);
        System.out.println("Args map: " + argsMap);
        final var dependencies = readDependencies(argsMap);
        System.out.println("Dependencies: " + dependencies);

        final var path = System.getProperty("path");
        System.out.println("Path: " + path);

        final var transformed = transform(dependencies, Path.of(path));
        System.out.println("Transformed: ");
        System.out.println(transformed);
    }

    private static void checkAssertEnabled()
    {
        boolean enabled = false;
        assert enabled = true;
        if(!enabled)
            throw new AssertionError("assert not enabled");
    }

    private static String transform(List<MavenArtifact> dependencies, Path path)
    {
        try
        {
            try (var lines = Files.lines(path))
            {
                Parsed parsed = parse(dependencies, lines);
                System.out.println(parsed.versions);
                System.out.println(parsed.sha1s);
                System.out.println(parsed.sourceSha1s);

                assert parsed.versions.get("ASM_7.1").lineNumber == 48;
                assert parsed.versions.get("JACOCOAGENT_0.8.4").lineNumber == 24;

                assert parsed.sha1s.get("ASM_7.1").lineNumber == 43;
                assert parsed.sha1s.get("JACOCOAGENT_0.8.4").lineNumber == 19;

                assert parsed.sourceSha1s.get("ASM_7.1").lineNumber == 44;

                return apply(dependencies, parsed);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    static String apply(List<MavenArtifact> dependencies, Parsed parsed)
    {
        final var result = new ArrayList<>(parsed.lines);
        dependencies.forEach(apply(a -> a.version, parsed.versions, result));
        dependencies.forEach(apply(a -> a.sha1, parsed.sha1s, result));
        dependencies.forEach(apply(a -> a.sourceSha1, parsed.sourceSha1s, result));
        return String.join("\n", result);
    }

    static Consumer<MavenArtifact> apply(
        Function<MavenArtifact, String> extract
        , Map<String, Coordinate> values
        , List<String> lines
    )
    {
        return artifact ->
        {
            final var coordinate = values.get(artifact.id);
            if (coordinate != null)
            {
                final var line = lines.get(coordinate.lineNumber);
                final var replaced = line.replace(coordinate.value, extract.apply(artifact));
                lines.set(coordinate.lineNumber, replaced);
            }
        };
    }

    static Parsed parse(List<MavenArtifact> dependencies, Stream<String> lines)
    {
        int lineNumber = -1;
        String id = null;
        String sha1 = null;
        String sourceSha1 = null;
        String version = null;

        final var endlines = new ArrayList<String>();
        final Map<String, Coordinate> versions = new HashMap<>();
        final Map<String, Coordinate> sha1s = new HashMap<>();
        final Map<String, Coordinate> sourceSha1s = new HashMap<>();

        final var it = lines.iterator();
        while (it.hasNext())
        {
            final var line = it.next();

            lineNumber++;
            endlines.add(line);

            if (id == null)
            {
                final var maybeArtifact = getMavenArtifact(dependencies, line);
                if (maybeArtifact.isPresent())
                {
                    id = maybeArtifact.get().id;
                }
            }
            else
            {
                String tmpSha1 = getSha1(line);
                if (tmpSha1 != null)
                {
                    sha1 = tmpSha1;
                    sha1s.put(id, new Coordinate(sha1, lineNumber));
                    continue;
                }

                String tmpSourceSha1 = getSourceSha1(line);
                if (tmpSourceSha1 != null)
                {
                    sourceSha1 = tmpSourceSha1;
                    sourceSha1s.put(id, new Coordinate(sourceSha1, lineNumber));
                    continue;
                }

                version = getVersion(line);
                if (version != null)
                {
                    versions.put(id, new Coordinate(version, lineNumber));
                    id = null;
                }
            }
        }

        return new Parsed(endlines, versions, sha1s, sourceSha1s);
    }

    static Optional<MavenArtifact> getMavenArtifact(List<MavenArtifact> dependencies, String line)
    {
        // TODO cache pattern
        return dependencies.stream()
            .filter(artifact -> MavenArtifact.pattern(artifact).matcher(line).find())
            .findFirst();
    }

    static String getSha1(String line)
    {
        final var pattern = Pattern.compile("\"sha1\"\\s*:\\s*\"([a-f0-9]*)\"");
        final var matcher = pattern.matcher(line);
        if (matcher.find())
        {
            // System.out.println(line);
            return matcher.group(1);
        }
        return null;
    }

    static String getSourceSha1(String line)
    {
        final var pattern = Pattern.compile("\"sourceSha1\"\\s*:\\s*\"([a-f0-9]*)\"");
        final var matcher = pattern.matcher(line);
        if (matcher.find())
        {
            // System.out.println(line);
            return matcher.group(1);
        }
        return null;
    }

    static String getVersion(String line)
    {
        final var pattern = Pattern.compile("\"version\"\\s*:\\s*\"([0-9\\.]*)\"");
        final var matcher = pattern.matcher(line);
        if (matcher.find())
        {
            // System.out.println(line);
            return matcher.group(1);
        }
        return null;
    }

    static final class Coordinate
    {
        final String value;
        final int lineNumber;

        Coordinate(String value, int lineNumber)
        {
            this.value = value;
            this.lineNumber = lineNumber;
        }

        @Override
        public String toString()
        {
            return "Coordinate{" +
                "value='" + value + '\'' +
                ", lineNumber=" + lineNumber +
                '}';
        }
    }

    static final class Parsed
    {
        final List<String> lines;
        final Map<String, Coordinate> versions;
        final Map<String, Coordinate> sha1s;
        final Map<String, Coordinate> sourceSha1s;

        Parsed(
            List<String> lines
            , Map<String, Coordinate> versions
            , Map<String, Coordinate> sha1s
            , Map<String, Coordinate> sourceSha1s
        )
        {
            this.lines = lines;
            this.versions = versions;
            this.sha1s = sha1s;
            this.sourceSha1s = sourceSha1s;
        }
    }

    static List<MavenArtifact> readDependencies(Map<String, List<String>> args)
    {
        final var dependencies = args.get("dependencies");
        return dependencies.stream()
            .map(TransformJsonManually::toFields)
            .map(MavenArtifact::of)
            .collect(Collectors.toList());
    }

    static Map<String, String> toFields(String dependency)
    {
        final var fields = Arrays.asList(dependency.split(","));
        return fields.stream()
            .map(fs -> fs.split("="))
            .collect(Collectors.toMap(fs -> fs[0], fs -> fs[1]));
    }

    static Map<String, List<String>> readArgs(String... args)
    {
        final Map<String, List<String>> params = new HashMap<>();

        List<String> options = null;
        for (int i = 0; i < args.length; i++)
        {
            final String arg = args[i];

            if (arg.startsWith("--"))
            {
                if (arg.length() < 3)
                {
                    System.err.println("Error at argument " + arg);
                    return params;
                }

                options = new ArrayList<>();
                params.put(arg.substring(2), options);
            }
            else if (options != null)
            {
                options.add(arg);
            }
            else
            {
                System.err.println("Illegal parameter usage");
                return params;
            }
        }

        return params;
    }

    static final class MavenArtifact
    {
        final String id;
        final String version;
        final String sha1;
        final String sourceSha1;

        private MavenArtifact(String id, String version, String sha1, String sourceSha1)
        {
            this.id = id;
            this.version = version;
            this.sha1 = sha1;
            this.sourceSha1 = sourceSha1;
        }

        static Pattern pattern(MavenArtifact artifact)
        {
            return Pattern.compile(artifact.id + "[^({|\\n)]*\\{");
        }

        @Override
        public String toString()
        {
            return "MavenArtifact{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", sha1='" + sha1 + '\'' +
                ", sourceSha1='" + sourceSha1 + '\'' +
                '}';
        }

        static MavenArtifact of(Map<String, String> fields)
        {
            final var id = fields.get("id");
            final var version = fields.get("version");
            final var sha1 = fields.get("sha1");
            final var sourceSha1 = fields.get("sourceSha1");
            return new MavenArtifact(id, version, sha1, sourceSha1);
        }
    }

}
