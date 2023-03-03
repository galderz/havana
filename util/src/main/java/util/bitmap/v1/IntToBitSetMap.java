package util.bitmap.v1;

import java.util.BitSet;
import java.util.Objects;

public class IntToBitSetMap
{
    private final int[] keys;
    private final BitSet[] values;
    private int size;

    IntToBitSetMap()
    {
        this(8);
    }

    IntToBitSetMap(int capacity)
    {
        keys = new int[capacity];
        values = new BitSet[capacity];
    }

    BitSet get(int key)
    {
        final int mask = values.length - 1;
        int index = hash(key, mask);

        BitSet value = values[index];
        while (Objects.nonNull(value))
        {
            if (keys[index] == key)
                break;

            index = ++index & mask;
        }

        return value;
    }

    void put(int key, BitSet value)
    {
        final int mask = values.length - 1;
        int index = hash(key, mask);

        BitSet prevValue = values[index];
        while (Objects.nonNull(prevValue))
        {
            if (keys[index] == key)
                break;

            index = ++index & mask;
            prevValue = values[index];
        }

        if (Objects.isNull(prevValue))
        {
            size++;
            keys[index] = key;
        }

        values[index] = value;
    }

    int hash(int value, int mask)
    {
        return hash(value) & mask;
    }

    // From https://stackoverflow.com/questions/664014/what-integer-hash-function-are-good-that-accepts-an-integer-hash-key
    int hash(int value)
    {
        value = ((value >>> 16) ^ value) * 0x45d9f3b;
        value = ((value >>> 16) ^ value) * 0x45d9f3b;
        value = (value >>> 16) ^ value;
        return value;
    }
}