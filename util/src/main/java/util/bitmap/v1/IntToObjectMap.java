package util.bitmap.v1;

import java.util.BitSet;
import java.util.Objects;
import java.util.function.Function;

public class IntToObjectMap<V>
{
    private final int[] keys;
    private final Object[] values;
    private final Function<Integer, Integer> hashFn;
    private int size;

    IntToObjectMap()
    {
        this(8, IntToObjectMap::hash);
    }

    IntToObjectMap(int capacity)
    {
        this(capacity, IntToObjectMap::hash);
    }

    IntToObjectMap(int capacity, Function<Integer, Integer> hashFn)
    {
        keys = new int[capacity];
        values = new BitSet[capacity];
        this.hashFn = hashFn;
    }

    int size()
    {
        return size;
    }

    boolean isFull()
    {
        return keys.length == size;
    }

    V get(int key)
    {
        final int mask = values.length - 1;
        int index = hash(key, mask);

        Object value = values[index];
        while (Objects.nonNull(value))
        {
            if (keys[index] == key)
                break;

            index = ++index & mask;
        }

        return (V) value;
    }

    void put(int key, V value)
    {
        final int mask = values.length - 1;
        int index = hash(key, mask);

        Object prevValue = values[index];
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

    // From https://stackoverflow.com/questions/664014/what-integer-hash-function-are-good-that-accepts-an-integer-hash-key

    static int hash(int value)
    {
        value = ((value >>> 16) ^ value) * 0x45d9f3b;
        value = ((value >>> 16) ^ value) * 0x45d9f3b;
        value = (value >>> 16) ^ value;
        return value;
    }

    private int hash(int value, int mask)
    {
        return hashFn.apply(value) & mask;
    }
}