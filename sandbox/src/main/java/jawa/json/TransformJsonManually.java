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
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransformJsonManually
{
    public static void main(String[] args)
    {
        System.out.println("Args: " + Arrays.toString(args));
        final var argsMap = readArgs(args);
        System.out.println("Args map: " + argsMap);
        final var dependencies = readDependencies(argsMap);
        System.out.println("Dependencies: " + dependencies);

        final var path = System.getProperty("path");
        System.out.println("Path: " + path);

        final var transformed = transform(dependencies, Path.of(path));
        System.out.println("Transformed: " + transformed);
    }

    private static String transform(List<MavenArtifact> dependencies, Path path)
    {
        final var parser = MavenArtifactParser.of(dependencies);
        try
        {
            try (var lines = Files.lines(path))
            {
                parser.parse(lines);
                return null; // TODO
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    static class MavenArtifactParser
    {
        final List<MavenArtifact> dependencies;
        final List<Pattern> dependencyPatterns;

        String id;
        String sha1;
        String sourceSha1;
        String version;

        MavenArtifactParser(List<MavenArtifact> dependencies, List<Pattern> dependencyPatterns)
        {
            this.dependencies = dependencies;
            this.dependencyPatterns = dependencyPatterns;
        }

        static MavenArtifactParser of(List<MavenArtifact> dependencies)
        {
            final var dependencyPatterns = dependencies.stream()
                .map(
                    artifact -> Pattern.compile(artifact.id + "[^({|\\n)]*\\{")
                )
                .collect(Collectors.toList());
            return new MavenArtifactParser(dependencies, dependencyPatterns);
        }

        void parse(Stream<String> lines)
        {
            final var it = lines.iterator();
            while (it.hasNext())
            {
                final var line = it.next();
                if (id == null)
                {
                    final var maybeArtifact = getMavenArtifact(line);
                    if (maybeArtifact.isPresent())
                    {
                        System.out.println(line);
                        id = maybeArtifact.get().id;
                    }
                }
                else
                {
                    String tmpSha1 = getSha1(line);
                    if (tmpSha1 != null)
                    {
                        sha1 = tmpSha1;
                        continue;
                    }

                    String tmpSourceSha1 = getSourceSha1(line);
                    if (tmpSourceSha1 != null)
                    {
                        sourceSha1 = tmpSourceSha1;
                        continue;
                    }

                    version = getVersion(line);
                    if (version != null)
                    {
                        final var artifact = MavenArtifact.of(id, version, sha1, sourceSha1);
                        System.out.println(artifact);
                        id = null;
                        sha1 = null;
                        sourceSha1 = null;
                        version = null;
                    }
                }
            }
        }

        Optional<MavenArtifact> getMavenArtifact(String line)
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

        static MavenArtifact of(String id, String version, String sha1, String sourceSha1)
        {
            return new MavenArtifact(id, version, sha1, sourceSha1);
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
            else if (options!=null)
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
}
