package svm.sourcecache.library;

import java.util.Date;

public class Greeter
{
    public static String greet(String name)
    {
        final var length = name.length();
        if (length % 2 == 0)
        {
            final var encoded = cipher1(name, name.length() % 26);
            return String.format(
                "hello %s - %tc - %s"
                , name
                , new Date()
                , encoded
            );
        }
        else
        {
            final var encoded = cipher2(name, "this is only a test");
            return String.format(
                "hola %s - %tc - %s"
                , name
                , new Date()
                , encoded
            );
        }
    }

    static String cipher1(String msg, int shift)
    {
        StringBuilder result = new StringBuilder();
        int len = msg.length();
        for (int x = 0; x < len; x++)
        {
            char c = (char) (msg.charAt(x) + shift);
            if (c > 'z')
                result.append((char) (msg.charAt(x) - (26 - shift)));
            else
                result.append((char) (msg.charAt(x) + shift));
        }
        return result.toString();
    }

    static String cipher2(String text, final String key)
    {
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z')
                continue;
            result.append((char) ((c + key.charAt(j) - 2 * 'A') % 26 + 'A'));
            j = ++j % key.length();
        }
        return result.toString().toLowerCase();
    }
}
