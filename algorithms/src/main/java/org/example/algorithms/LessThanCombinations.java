package org.example.algorithms;

import java.util.ArrayList;

public class LessThanCombinations
{
    enum Type
    {
        // todo add byte, char...
        BOOLEAN(8)
        , SHORT(16)
        , INT(32)
        , LONG(64);

        final int size;

        Type(int size)
        {
            this.size = size;
        }

        public int getSize()
        {
            return size;
        }
    }

    static void lessThanCombinations(int index, ArrayList<Type> cache, int sum, ArrayList<ArrayList<Type>> result, Type[] types, int limit)
    {
        if (sum >= limit)
        {
            return;
        }

        if (index == types.length)
        {
            if (!cache.isEmpty())
            {
                // Swallow copy cache and add it to result
                result.add(new ArrayList<>(cache));
            }
            return;
        }

        cache.add(types[index]);
        lessThanCombinations(index, cache, sum + types[index].size, result, types, limit);
        // Remove last entry because it will have exceeded or equalled the limit
        cache.removeLast();

        lessThanCombinations(index + 1, cache, sum, result, types, limit);
    }

    static ArrayList<ArrayList<Type>> lessThanCombinations(Type[] types, int limit)
    {
        final ArrayList<ArrayList<Type>> result = new ArrayList<>();
        lessThanCombinations(0, new ArrayList<>(), 0, result, types, limit);
        return result;
    }

    public static void main(String[] args)
    {
        final Type[] types = Type.values();
        final int limit = 64;

        var result = lessThanCombinations(types, limit);
        System.out.println("Result count: " + result.size());
        for (var combination : result) {
            int sum = combination.stream().mapToInt(Type::getSize).sum();
            System.out.println(combination + "  (sum=" + sum + ")");
        }
    }
}
