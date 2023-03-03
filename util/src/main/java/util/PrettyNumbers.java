package util;

import java.util.HexFormat;

public class PrettyNumbers
{
    static void show(long number)
    {
        final HexFormat hex = HexFormat.of();
        final String asHex = hex.toHexDigits(number);
        final String asBinary = String.format("%64s", Long.toBinaryString(number)).replace(' ', '0');
        System.out.printf("Number:%n%d%n", number);
        System.out.printf("Hex:%n%s%n", asHex);
        System.out.println("Binary:");
        System.out.println(asHex.replaceAll("(.{1})", "    $1"));
        System.out.printf(" %s", asBinary.replaceAll("(.{4})", "$1 "));
    }

    public static void main(String[] args)
    {
        show(281479315055456L);
    }
}
