package util.bitmap.v1;

import util.Asserts;

public class BitMapTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testSign();
    }

    private static void testSign()
    {
        System.out.println("BitMapTest.testSign");
        final BitMap bitMap = new BitMap();
        assert bitMap.getSignum(-9223090557539720352L) < 0;
        assert bitMap.getSignum(281479315055456L) > 0;
    }
}
