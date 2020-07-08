package p.regex;

import java.util.regex.Pattern;

public class FieldThenValueDoubleQuoted
{
    public static void main(String[] args)
    {
        String s = "      \"sha1\" : \"53addc878614171ff0fcbc8f78aed12175c22cdb\",\n";
        final var pattern = Pattern.compile("\"sha1\"\\s*:\\s*\"([a-f0-9]*)\"");
        final var matcher = pattern.matcher(s);
        System.out.println(matcher.find());
    }
}
