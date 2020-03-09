package jawa.regex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ReplaceInFile
{
    public static final String FILE_PATH = System.getProperty("path");
    public static final String REGEX = System.getProperty("regex");
    public static final String REPLACEMENT = System.getProperty("replacement");

    public static void main(String[] args) throws IOException
    {
        final var path = Paths.get(FILE_PATH);
        try (var lines = Files.lines(path))
        {
            List<String> replaced = lines
                .map(ReplaceInFile::replaceGroupId)
                .collect(Collectors.toList());
            Files.write(path, replaced);
        }
    }

    private static String replaceGroupId(String line)
    {
        return line.contains("groupId")
            ? line.replaceFirst(REGEX, REPLACEMENT)
            : line;
    }
}
