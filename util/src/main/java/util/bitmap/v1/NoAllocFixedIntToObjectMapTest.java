package util.bitmap.v1;

import util.Asserts;

import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class NoAllocFixedIntToObjectMapTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testPutGet();
        testFullNoCollision();
        testFullCollision();
        testOverflow();
        testVerySmallCapacity();
        testZeroAndOne();
    }

    private static void testZeroAndOne()
    {
        System.out.println("NoAllocFixedIntToObjectMapTest.testZeroAndOne");
        final NoAllocFixedIntToObjectMap<BitSet> map = new NoAllocFixedIntToObjectMap<>();
        final BitSet zeroBitSet = new BitSet();
        map.put(0, zeroBitSet);
        assert zeroBitSet == map.get(0);
        final BitSet oneBitSet = new BitSet();
        map.put(1, oneBitSet);
        assert oneBitSet == map.get(1);
    }

    private static void testVerySmallCapacity()
    {
        System.out.println("NoAllocFixedIntToObjectMapTest.testVerySmallCapacity");
        final int capacity = 1;
        final NoAllocFixedIntToObjectMap<BitSet> map = new NoAllocFixedIntToObjectMap<>(capacity);
        final BitSet value = new BitSet();
        map.put(0, value);
        assert null == map.get(32768);
    }

    private static void testOverflow()
    {
        System.out.println("NoAllocFixedIntToObjectMapTest.testOverflow");
        final int capacity = 4;
        final NoAllocFixedIntToObjectMap<BitSet> map = new NoAllocFixedIntToObjectMap<>(capacity);
        for (int i = 0; i < capacity + 1; i++)
        {
            final int key = i + 1;
            final boolean success = map.put(key, new BitSet());
            if (i == capacity)
            {
                assert !success;
            }
            else
            {
                assert success;
            }
        }
    }

    private static void testFullCollision()
    {
        System.out.println("NoAllocFixedIntToObjectMapTest.testFullCollision");
        final int capacity = 4;
        final Function<Integer, Integer> collisionHashFn = x -> 14;
        final NoAllocFixedIntToObjectMap<BitSet> map = new NoAllocFixedIntToObjectMap<>(capacity, collisionHashFn);
        for (int i = 0; i < capacity; i++)
        {
            final boolean success = map.put(i + 1, new BitSet());
            assert success;
        }
        assert 4 == map.size();
        assert map.isFull();
    }

    private static void testFullNoCollision()
    {
        System.out.println("NoAllocFixedIntToObjectMapTest.testFullNoCollision");
        final int capacity = 4;
        Collection<Integer> keys = generateNoCollisionKeys(capacity);
        assert keys.size() == 4;

        final NoAllocFixedIntToObjectMap<BitSet> map = new NoAllocFixedIntToObjectMap<>(capacity);
        for (int i = 0; i < capacity; i++)
        {
            final int key = new Random().nextInt();
            final boolean success = map.put(key, new BitSet());
            assert success;
        }
        assert 4 == map.size();
        assert map.isFull();
    }

    private static void testPutGet()
    {
        System.out.println("NoAllocFixedIntToObjectMapTest.testPutGet");
        final NoAllocFixedIntToObjectMap<BitSet> map = new NoAllocFixedIntToObjectMap<>();
        final int key = 16384;
        final BitSet value = new BitSet(30);
        value.set(542293100);
        final boolean success = map.put(key, value);
        assert success;
        assert value.equals(map.get(key));
        assert 1 == map.size();
        assert !map.isFull();
    }

    private static Collection<Integer> generateNoCollisionKeys(int numKeys)
    {
        final Map<Integer, Integer> hashToKeys = new HashMap<>(numKeys);
        final Random r = new Random();
        int count = 0;
        while (count < 10_000)
        {
            final int candidate = r.nextInt();
            final int hash = NoAllocFixedIntToObjectMap.hash(candidate);
            if (Objects.isNull(hashToKeys.putIfAbsent(hash, candidate)) && numKeys == hashToKeys.size())
                return hashToKeys.values();

            count++;
        }
        return Collections.emptySet();
    }
}
