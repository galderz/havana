package util;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ArrayIndexSorting
{
    public static void main(String[] args)
    {
        long[] counters = new long[]{9, 1, 5};
        String[] names = new String[]{"c", "a", "b"};

        Integer[] sortedIndexes = IntStream.range(0, 3)
            .boxed()
            .sorted((i, j) -> Long.compare(counters[j], counters[i]))
            .toArray(Integer[]::new);

        System.out.println("Sorted: " + Arrays.toString(sortedIndexes));

        for (Integer index : sortedIndexes)
        {
            final long counter = counters[index];
            final String name = names[index];
            System.out.println(name + "=" + counter);
        }
    }
}
