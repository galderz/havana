package util.bitmap.v1;

import util.Asserts;

import java.util.ArrayList;
import java.util.List;

public class NoAllocationBitSetTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testSetSingle();
        testSetAll();
    }

    private static void testSetAll()
    {
        System.out.println("NoAllocationBitSetTest.testSetAll");
        final int numberOfBits = 1 << 30;
        final NoAllocationBitSet bitSet = new NoAllocationBitSet(numberOfBits);
        for (int i = 0; i < numberOfBits; i++)
        {
            final boolean success = bitSet.set(i);
            assert success;
        }
        int count = 0;
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            count++;
            assert bitSet.get(i);
        }
        assert numberOfBits == count;
    }

    private static void testSetSingle()
    {
        System.out.println("NoAllocationBitSetTest.testSetSingle");
        final NoAllocationBitSet bitSet = new NoAllocationBitSet(1 << 30);
        final int index = 32768;
        bitSet.set(index);
        final List<Integer> indexes = new ArrayList<>();
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i+1)) {
            indexes.add(i);
        }
        assert 1 == indexes.size();
        assert index == indexes.get(0);
        assert bitSet.get(indexes.get(0));
    }
}
