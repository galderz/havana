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
import java.util.function.Function;

public class IntToObjectMapTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testPutGet();
        testFullNoCollision();
        testFullCollision();
    }

    private static void testFullCollision()
    {
        System.out.println("IntToObjectMapTest.testFullCollision");
        final int capacity = 4;
        final Function<Integer, Integer> collisionHashFn = x -> 14;
        final IntToObjectMap<BitSet> map = new IntToObjectMap<>(capacity, collisionHashFn);
        for (int i = 0; i < capacity; i++)
        {
            map.put(i + 1, new BitSet());
        }
        assert 4 == map.size();
        assert map.isFull();
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

//    private static Collection<Integer> generateCollisionKeys(int numKeys)
//    {
//        final List<Integer> result = new ArrayList<>(numKeys);
//        final Random r = new Random();
//        final int key = r.nextInt();
//        final int targetHash = IntToObjectMap.hash(key);
//        System.out.println("Target hash: " + targetHash);
//        int count = 0;
//        while (count < 1_000_000_000)
//        {
//            final int candidate = r.nextInt();
//            final int hash = IntToObjectMap.hash(candidate);
//            if (hash == targetHash)
//            {
//                System.out.println("Match: " + candidate);
//                result.add(candidate);
//                if (result.size() == numKeys)
//                    return result;
//            }
//
//            count++;
//        }
//
//        System.out.println(result.size());
//        return Collections.emptyList();
//    }
}
