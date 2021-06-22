package lang.system;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShortsAsHex
{
    static final int constantLength = 32;
    static final int operationLength = 24;
    static final int valueLength = 32;

    static final String FORMAT = String.format(
        "| %%-%ds| %%-%ds| %%-%ds|%n"
        , constantLength
        , valueLength
        , operationLength
    );
    public static final String LINE = String.format(String.format(
        "+ %%%ds+ %%%ds+ %%%ds+"
        , constantLength
        , valueLength
        , operationLength
    ), " ", " ", " ").replace(" ", "-");

    public static void main(String[] args)
    {
        header();
        show(Short.MAX_VALUE, "Short.MAX_VALUE");
        show(Short.MIN_VALUE, "Short.MIN_VALUE");
        show(Short.MAX_VALUE - 1, "Short.MAX_VALUE - 1");
        show(Short.MIN_VALUE + 1, "Short.MIN_VALUE + 1");
        footer();
    }

    private static void header()
    {
        System.out.println(LINE);
        System.out.printf(
            FORMAT
            , "Constant"
            , "Value"
            , "Operation"
        );
        System.out.println(LINE);
    }

    private static void show(int value, String msg)
    {
        System.out.printf(
            FORMAT
            , msg
            , prettyHex(value)
            , ""
        );
    }

    private static void footer()
    {
        System.out.println(LINE);
    }

    static String prettyHex(int v)
    {
        final var hex = Integer.toHexString(v).toUpperCase();
        final var padding = "0".repeat(8 - hex.length());
        final var paddedHex = padding + hex;
        return Stream
            .of(paddedHex.split("(?<=\\G.{4})"))
            .collect(Collectors.joining("_", "0x", ""));
    }
}
