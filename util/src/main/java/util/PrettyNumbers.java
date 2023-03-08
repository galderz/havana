package util;

import java.math.BigInteger;
import java.util.HexFormat;

public class PrettyNumbers
{
    static void showLong(long number)
    {
        final HexFormat hex = HexFormat.of();
        final String asHex = hex.toHexDigits(number);
        final String asBinary = String.format("%64s", Long.toBinaryString(number)).replace(' ', '0');
        System.out.printf("Number:%n%d%n", number);
        System.out.printf("Hex:%n%s%n", asHex);
        System.out.println("Binary:");
        System.out.println(asHex.replaceAll("(.{1})", "    $1"));
        System.out.printf(" %s%n", asBinary.replaceAll("(.{4})", "$1 "));
    }

    static void showHex(String hexNumber)
    {
        showLong(new BigInteger(hexNumber, 16).longValue());
    }

    public static void main(String[] args)
    {
        showLong(281479315055456L);
        showHex("800100010295e360");
        showLong(32768);
        showHex("000000010295e360");
        showLong(1 << 14);
    }
}
