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
        "| %%-%ds| %%-%ds|%%%ds |%n"
        , constantLength
        , operationLength
        , valueLength
    );
    public static final String LINE = String.format(String.format(
        "+ %%%ds+ %%%ds+%%%ds +"
        , constantLength
        , operationLength
        , valueLength
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
        footer();
    }

    private static void header()
    {
        System.out.println(LINE);
        System.out.printf(
            FORMAT
            , "Constant"
            , "Operation"
            , "Value"
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
            , op
            , prettyHex(f.apply(d))
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
