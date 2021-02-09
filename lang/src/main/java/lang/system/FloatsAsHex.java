package lang.system;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FloatsAsHex
{
    static final int constantLength = 32;
    static final int operationLength = 24;
    static final int valueLength = 16;

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
        show(Float.NaN, "Float.NaN");
        show(-Float.NaN, "-Float.NaN");
        show(Float.NEGATIVE_INFINITY, "Float.NEGATIVE_INFINITY");
        show(-Float.NEGATIVE_INFINITY, "-Float.NEGATIVE_INFINITY");
        show(Float.MIN_VALUE, "Float.MIN_VALUE");
        show(-Float.MIN_VALUE, "-Float.MIN_VALUE");
        show(Float.MIN_NORMAL, "Float.MIN_NORMAL");
        show(-Float.MIN_NORMAL, "-Float.MIN_NORMAL");
        show(Float.MAX_VALUE, "Float.MAX_VALUE");
        show(-Float.MAX_VALUE, "-Float.MAX_VALUE");
        show(Float.POSITIVE_INFINITY, "Float.POSITIVE_INFINITY");
        show(-Float.POSITIVE_INFINITY, "-Float.POSITIVE_INFINITY");
        show(Float.intBitsToFloat(0x7FC0_0100), "A NaN that's not Float.NaN");
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

    private static void show(float f, String msg)
    {
        show(f, msg, Float::floatToIntBits, "floatToIntBits");
        show(f, msg, Float::floatToRawIntBits, "floatToRawIntBits");
    }

    private static void show(float aFloat, String msg, Function<Float, Integer> f, String op)
    {
        System.out.printf(
            FORMAT
            , msg
            , prettyHex(f.apply(aFloat))
            , op
        );
    }

    private static void footer()
    {
        System.out.println(LINE);
    }

    static String prettyHex(int i)
    {
        final var hex = Integer.toHexString(i).toUpperCase();
        final var padding = "0".repeat(8 - hex.length());
        final var paddedHex = padding + hex;
        return Stream
            .of(paddedHex.split("(?<=\\G.{4})"))
            .collect(Collectors.joining("_", "0x", "L"));
    }
}
