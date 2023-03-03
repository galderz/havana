package util.bitmap.v0;

import util.Asserts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HexFormat;

public class BitMapTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testHighBits();
        testLowBits();
        testMarkSingle();
    }

    private static void testMarkSingle()
    {
        System.out.println("BitMapTest.testMarkSingle");
        long num = 281479315055456L;
        final BitMap bitMap = new BitMap();
        bitMap.mark(num);
        assert bitMap.isMarked(num);
    }

    private static void testHighBits()
    {
        System.out.println("BitMapTest.testHighBits");
        final BitMap bitMap = new BitMap();
        assert 16384 == bitMap.getHighBits(281479315055456L);

        final HexFormat hex = HexFormat.of();

        // 000100010295e360
        // 0001 0001 0295 e360
        // [0, 1, 0, 1, 2, -107, -29, 96]
        // 281479315055456
        final String axHex = "000100010295e360";
        final byte[] asBytes = hex.parseHex(axHex);
        System.out.println(Arrays.toString(asBytes));
        long asNum = new BigInteger(axHex, 16).longValue();
        System.out.println(asNum);
        //    0    0    0    1    0    0    0    1    0    2    9    5    e    3    6    0
        // 0000 0000 0000 0001 0000 0000 0000 0001 0000 0010 1001 0101 1110 0011 0110 0000
        System.out.println(Long.toBinaryString(asNum));

        int alignment = 8;
        int logAlignment = log2(alignment); // 3

        long lowShift = 31;
        long highShift = lowShift + logAlignment;

        //    0    0    0    1    0    0    0    1    0    2    9    5    e    3    6    0
        // 0000 0000 0000 0001 0000 0000 0000 0001 0000 0010 1001 0101 1110 0011 0110 0000 >> lowShift
        //    0    0    0    1    0    0    0
        // 0000 0000 0000 0001 0000 0000 0000 00
        // 0000 0000 0000 00 0100 0000 0000 0000
        // 0100 0000 0000 0000
        // 16384
        long asNumHigh = asNum >> highShift;
        System.out.println(asNumHigh);
        System.out.println(Long.toBinaryString(asNumHigh));

        //    0    0    0    1    0    0    0    1    0    2    9    5    e    3    6    0
        // 0000 0000 0000 0001 0000 0000 0000 0001 0000 0010 1001 0101 1110 0011 0110 0000 >> logAlignment
        //    0    0    0    0    2    0    0    0    2    0    5    2    b    c    6    c
        // 0000 0000 0000 0000 0010 0000 0000 0000 0010 0000 0101 0010 1011 1100 0110 1100
        long lowSize = 1L << lowShift;
        long lowMask = lowSize - 1;
        final long lowShifted = asNum >> logAlignment;
        System.out.println(Long.toBinaryString(lowShifted));
        System.out.println(hex.toHexDigits(lowShifted));

        //    0    0    0    0    2    0    0    0    2    0    5    2    b    c    6    c
        // 0000 0000 0000 0000 0010 0000 0000 0000 0010 0000 0101 0010 1011 1100 0110 1100 & lowMask
        //    0    0    0    0    0    0    0    0    2    0    5    2    b    c    6    c
        // 0000 0000 0000 0000 0000 0000 0000 0000 0010 0000 0101 0010 1011 1100 0110 1100 & lowMask
        long asNumLow = lowShifted & lowMask;
        System.out.println(Long.toBinaryString(asNumLow));
        System.out.println(hex.toHexDigits(asNumLow));
        System.out.println(asNumLow);
    }

    private static void testLowBits()
    {
        System.out.println("BitMapTest.testLowBits");
        final BitMap bitMap = new BitMap();
        assert 542293100 == bitMap.getLowBits(281479315055456L);
    }

    static int log2(int num) {
        return 31 - Integer.numberOfLeadingZeros(num);
    }
}
