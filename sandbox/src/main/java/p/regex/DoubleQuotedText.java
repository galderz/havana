package p.regex;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class DoubleQuotedText
{
    public static void main(String[] args)
    {
        String s = "  \"version\" : \"5.244.0\",";
        System.out.println(s);

        extractWithoutQuotes(s);
        System.out.println("--");
        extractWithoutQuotesHeavy(s);
        System.out.println("--");
        extractWithQuotesAndWithoutDot(s);
        System.out.println("--");
        extractWithQuotesAndDot(s);
    }

    private static void extractWithoutQuotes(String s)
    {
        final var pattern = Pattern.compile("\"([0-9]\\.[0-9]{1,3}\\.[0-9]{1,2})\"");
        final var matcher = pattern.matcher(s);
        final var mxVersion = matcher.results()
            .map(result -> result.group(1))
            .findFirst()
            .orElse(null);
        System.out.println(mxVersion);
    }

    private static void extractWithoutQuotesHeavy(String s)
    {
        final var pattern = Pattern.compile("\"(.*?)\"");
        final var matcher = pattern.matcher(s);
        final var mxVersion = matcher.results()
            .reduce((first, second) -> second)
            .map(result -> result.group(1))
            .orElse(null);
        System.out.println(mxVersion);
    }

    private static void extractWithQuotesAndWithoutDot(String s)
    {
        final var pattern = Pattern.compile("\"[^\"\"]+\"");
        final var matcher = pattern.matcher(s);
        final var mxVersion = matcher.results()
            .reduce((first, second) -> second)
            .map(MatchResult::group)
            .orElse(null);
        System.out.println(mxVersion);
    }

    private static void extractWithQuotesAndDot(String s)
    {
        final var pattern = Pattern.compile("\".+?\"");
        final var matcher = pattern.matcher(s);
        final var mxVersion = matcher.results()
            .reduce((first, second) -> second)
            .map(MatchResult::group)
            .orElse(null);
        System.out.println(mxVersion);
    }

}
