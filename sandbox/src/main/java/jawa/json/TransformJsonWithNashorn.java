package jawa.json;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class TransformJsonWithNashorn
{
    public static void main(String[] args) throws Exception
    {
        final var path = System.getProperty("path");
        final var raw = readFile(Path.of(path));

        final var contents = raw.replace("suite = ", "");
        // System.out.println(contents);
        final var withoutComments = trimComments(contents);
        // System.out.println(withoutComments);

        final var withouTrailingCommas = trimTrailingCommas(withoutComments);
        //System.out.println(withouTrailingCommas);

        final var fixedBooleans = fixBooleans(withouTrailingCommas);
        System.out.println(fixedBooleans);

        parse(fixedBooleans);
    }

    private static String fixBooleans(String contents)
    {
        return contents
            .replaceAll("True", "true")
            .replaceAll("False", "false");
    }

    private static void parse(String contents) throws Exception
    {
        final var engine = new NashornScriptEngineFactory().getScriptEngine();
        final var bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.put("raw_data", contents);

        engine.eval(new FileReader("sandbox/src/main/resources/transform.js"));

        final var output = ((Invocable) engine).invokeFunction("transform");
        System.out.println(output.toString());
    }

    static String readFile(Path path)
    {
        try
        {
            try(var lines = Files.lines(path))
            {
                return lines
                    //.map(TransformJsonWithNashorn::trimComments)
                    .collect(Collectors.joining(System.lineSeparator()));
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    static String trimCommentsLine(String line)
    {
        if (line.contains("#"))
        {
            return String.format("%s", line.split("#")[0]);
        }

        return line;
    }

    static String trimComments(String contents)
    {
        return contents.replaceAll("#[^\\r\\n]*", "");
    }

    static String trimTrailingCommas(String contents)
    {
        return contents.replaceAll("\\,(?!\\s*?[\\{\\[\\\"\\'\\w])", "");
    }

}
