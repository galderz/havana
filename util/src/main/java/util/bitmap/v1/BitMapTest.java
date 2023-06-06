package util.bitmap.v1;

import util.Asserts;

import java.util.BitSet;

public class BitMapTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testSign();
        testHighBits();
        testLowBits();
        // testBiggestArray();
    }

    private static void testLowBits()
    {
        System.out.println("BitMapTest.testLowBits");
        final BitMap bitMap = new BitMap();
        assert 542293100 == bitMap.getLowBits(281479315055456L);
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

    private static void testBiggestArray()
    {
        final BitSet thirtyBitsBitSet = new BitSet(1 << 30);
        System.out.println(thirtyBitsBitSet);
        System.out.println(thirtyBitsBitSet.size());
        final BitSet thirtyOneBitsBitSet = new BitSet(1 << 31);
        System.out.println(thirtyOneBitsBitSet);
    }
}
