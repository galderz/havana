package util.bitmap.v1;

import util.Asserts;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class IntToObjectMapTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testPutGet();
        testFullNoCollision();
    }

    private static void testFullNoCollision()
    {
        System.out.println("IntToObjectMapTest.testFullNoCollision");
        final int capacity = 4;
        Collection<Integer> keys = generateNoCollisionKeys(capacity);
        assert keys.size() == 4;

        final IntToObjectMap<BitSet> map = new IntToObjectMap<>(capacity);
        for (int i = 0; i < capacity; i++)
        {
            final int key = new Random().nextInt();
            map.put(key, new BitSet());
        }
        assert 4 == map.size();
        assert map.isFull();
    }

    private static void testPutGet()
    {
        System.out.println("IntToBitSetMapTest.testPutGet");
        final IntToObjectMap<BitSet> map = new IntToObjectMap<>();
        final int key = 16384;
        final BitSet value = new BitSet(30);
        value.set(542293100);
        map.put(key, value);
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
            final int hash = IntToObjectMap.hash(candidate);
            if (Objects.isNull(hashToKeys.putIfAbsent(hash, candidate)) && numKeys == hashToKeys.size())
                return hashToKeys.values();

            count++;
        }
        return Collections.emptySet();
    }
}
