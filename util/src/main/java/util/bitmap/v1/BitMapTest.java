package util.bitmap.v1;

import util.Asserts;

import java.util.HexFormat;

public class BitMapTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testSign();
        testHighBits();
    }

    private static void testHighBits()
    {
        System.out.println("BitMapTest.testHighBits");
        final BitMap bitMap = new BitMap();
        assert 32768 == bitMap.getHighBits(281479315055456L); // 0000000000008000
    }

    private static void testSign()
    {
        System.out.println("BitMapTest.testSign");
        final BitMap bitMap = new BitMap();
        assert bitMap.getSignum(-9223090557539720352L) < 0; // 800100010295e360
        assert bitMap.getSignum(281479315055456L) > 0;      // 000100010295e360
    }
}
