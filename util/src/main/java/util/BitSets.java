package util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BitSets
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testSetSingle();
        testSetAll();
    }

    private static void testSetAll()
    {
        System.out.println("BitSets.testSetAll");
        final int numberOfBits = 1 << 30;
        final BitSet bitSet = new BitSet(numberOfBits);
        for (int i = 0; i < numberOfBits; i++)
        {
            bitSet.set(i);
        }
        int count = 0;
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            count++;
        }
        assert numberOfBits == count;
    }

    private static void testSetSingle()
    {
        System.out.println("BitSets.testSetSingle");
        final BitSet bitSet = new BitSet(1 << 30);
        final int index = 32768;
        bitSet.set(index);
        final List<Integer> indexes = new ArrayList<>();
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1))
        {
            indexes.add(i);
        }
        assert 1 == indexes.size();
        assert index == indexes.get(0);
    }
}
