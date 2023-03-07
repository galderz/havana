package util.bitmap.v1;

import java.util.Objects;
import java.util.function.Function;

/**
 * Allocation free, fixed size, int to object map.
 */
public class NoAllocFixedIntToObjectMap<V>
{
    private static final int MIN_CAPACITY = 4;

    private final int[] keys;
    private final Object[] values;
    private final Function<Integer, Integer> hashFn;
    private int size;

    NoAllocFixedIntToObjectMap()
    {
        this(8, NoAllocFixedIntToObjectMap::hash);
    }

    NoAllocFixedIntToObjectMap(int capacity)
    {
        this(capacity, NoAllocFixedIntToObjectMap::hash);
    }

    NoAllocFixedIntToObjectMap(int initialCapacity, Function<Integer, Integer> hashFn)
    {
        int capacity = findNextPositivePowerOfTwo(Math.max(initialCapacity, MIN_CAPACITY));
        keys = new int[capacity];
        values = new Object[capacity];
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

    boolean put(int key, V value)
    {
        if (isFull())
            return false;

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
        return true;
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

    private static int findNextPositivePowerOfTwo(final int value) {
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }
}