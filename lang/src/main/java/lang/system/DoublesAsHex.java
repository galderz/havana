package lang.system;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DoublesAsHex
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
        show(Double.NaN, "Double.NaN");
        show(-Double.NaN, "-Double.NaN");
        show(Double.NEGATIVE_INFINITY, "Double.NEGATIVE_INFINITY");
        show(-Double.NEGATIVE_INFINITY, "-Double.NEGATIVE_INFINITY");
        show(Double.MIN_VALUE, "Double.MIN_VALUE");
        show(-Double.MIN_VALUE, "-Double.MIN_VALUE");
        show(Double.MIN_NORMAL, "Double.MIN_NORMAL");
        show(-Double.MIN_NORMAL, "-Double.MIN_NORMAL");
        show(Double.MAX_VALUE, "Double.MAX_VALUE");
        show(-Double.MAX_VALUE, "-Double.MAX_VALUE");
        show(Double.POSITIVE_INFINITY, "Double.POSITIVE_INFINITY");
        show(-Double.POSITIVE_INFINITY, "-Double.POSITIVE_INFINITY");
        show(Double.longBitsToDouble(0x7ff8000000000100L), "A NaN that's not Double.NaN");
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

    private static void show(double d, String msg)
    {
        show(d, msg, Double::doubleToLongBits, "doubleToLongBits");
        show(d, msg, Double::doubleToRawLongBits, "doubleToRawLongBits");
    }

    private static void show(double d, String msg, Function<Double, Long> f, String op)
    {
        System.out.printf(
            FORMAT
            , msg
            , prettyHex(f.apply(d))
            , op
        );
    }

    private static void footer()
    {
        System.out.println(LINE);
    }

    static String prettyHex(long l)
    {
        final var hex = Long.toHexString(l).toUpperCase();
        final var padding = "0".repeat(16 - hex.length());
        final var paddedHex = padding + hex;
        return Stream
            .of(paddedHex.split("(?<=\\G.{4})"))
            .collect(Collectors.joining("_", "0x", "L"));
    }

}
