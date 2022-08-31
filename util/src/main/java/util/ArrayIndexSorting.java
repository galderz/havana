package util;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ArrayIndexSorting
{
    public static void main(String[] args)
    {
        long[] counters = new long[]{9, 0, 1, 0, 5};
        String[] names = new String[]{"a", "x", "c", "y", "b"};

        Integer[] sortedIndexes = IntStream.range(0, counters.length)
            .boxed()
            .filter(i -> counters[i] > 0)
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
