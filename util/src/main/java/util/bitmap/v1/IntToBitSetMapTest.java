package util.bitmap.v1;

import util.Asserts;

import java.util.BitSet;

public class IntToBitSetMapTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testPutGet();
    }

    private static void testPutGet()
    {
        System.out.println("IntToBitSetMapTest.testPutGet");
        final IntToBitSetMap map = new IntToBitSetMap();
        final int key = 16384;
        final BitSet value = new BitSet(30);
        value.set(542293100);
        map.put(key, value);
        assert value.equals(map.get(key));
    }
}
