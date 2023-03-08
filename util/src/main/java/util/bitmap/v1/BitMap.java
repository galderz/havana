package util.bitmap.v1;

import util.Asserts;
import util.PrettyNumbers;

import java.math.BigInteger;
import java.util.HexFormat;

//  0                   1                   2                   3                   4                   5                   6
//  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3
// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
// |S|                       High Bits                           |                     Low Bits                              | N/A |
// +-------------------------------------------------------------+-----------------------------------------------------------+-----+
public class BitMap
{
    final int objectAlignment;
    final int logAlignment;
    final int lowBitsCount;
    final long lowSize;
    final long lowMask;
    final int highBitsShift;

    public BitMap()
    {
        this(8);
    }

    public BitMap(int objectAlignment)
    {
        this.objectAlignment = objectAlignment;
        this.logAlignment = log2(objectAlignment);
        this.lowBitsCount = 30;
        this.lowSize = 1L << lowBitsCount;
        this.lowMask = lowSize - 1;
        this.highBitsShift = lowBitsCount + logAlignment;
    }

    int getSignum(long number)
    {
        return Long.signum(number);
    }

    int getLowBits(long num)
    {
        return (int) ((num >> logAlignment) & lowMask);
    }

    int getHighBits(long number)
    {
        return (int) (Math.abs(number) >> (highBitsShift));
    }

    static int log2(int num)
    {
        return 31 - Integer.numberOfLeadingZeros(num);
    }

    static void showLong(long number)
    {
        PrettyNumbers.showLong(number);

        final HexFormat hex = HexFormat.of();

        final String asBinary = String.format("%64s", Long.toBinaryString(number)).replace(' ', '0');
        final String signum = asBinary.substring(0, 1);
        final String highBits = asBinary.substring(1, 31);
        // assert highBits.length() == 30;
        final String lowBits = asBinary.substring(31, 61);
        // assert lowBits.length() == 30;

        System.out.println("Bitmap:");
        System.out.printf(
            " %s|%37s|%37s%n"
            , signum
            , Integer.parseInt(highBits, 2)
            , Integer.parseInt(lowBits, 2)
        );
        final String highBitsPrefix = highBits.substring(0, 2);
        final String highBitsSuffix = highBits.substring(2, 30);
        final String lowBitsPrefix = lowBits.substring(0, 2);
        final String lowBitsSuffix = lowBits.substring(2, 30);
        System.out.printf(
            " %s|%2s%s|%2s%s%n"
            , signum
            , hex.toHexDigits(Integer.parseInt(highBitsPrefix, 2)).charAt(7)
            , hex.toHexDigits(Integer.parseInt(highBitsSuffix, 2)).substring(1, 8).replaceAll("(.{1})", "    $1")
            , hex.toHexDigits(Integer.parseInt(lowBitsPrefix, 2)).charAt(7)
            , hex.toHexDigits(Integer.parseInt(lowBitsSuffix, 2)).substring(1, 8).replaceAll("(.{1})", "    $1")
        );
        System.out.printf(
            " %s|%s %s|%s %s%n"
            , signum
            , highBitsPrefix
            , highBitsSuffix.replaceAll("(.{4})", "$1 ").trim()
            , lowBitsPrefix
            , lowBitsSuffix.replaceAll("(.{4})", "$1 ").trim()
        );
    }

    static void showHex(String hexNumber)
    {
        showLong(new BigInteger(hexNumber, 16).longValue());
    }

    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        showHex("000000010295e360");
    }
}
