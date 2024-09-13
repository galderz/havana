package arrays;

import java.util.Arrays;
import java.util.stream.IntStream;

public class NegateAll
{
    public static void main(String[] args)
    {
        final int[] ints = {2, 5, 12, 2, 2, 5, 16, 16, 7, 12};
        System.out.println(Arrays.toString(ints));

        final int[] negated = IntStream.of(ints)
            .map(i -> -i)
            .toArray();
        System.out.println(Arrays.toString(negated));
    }
}
