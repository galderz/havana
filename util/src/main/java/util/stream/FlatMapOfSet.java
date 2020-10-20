package util.stream;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class FlatMapOfSet
{
    public static void main(String[] args)
    {
        final var map = new HashMap<Integer, Set<Integer>>();
        map.put(1, Set.of(10, 11));
        map.put(2, Set.of(20));
        map.put(3, Set.of(30, 31));

        // Convert to Set<IntTuple>
        final var setOfIntTuples = map.entrySet().stream()
            .flatMap(entry -> entry.getValue().stream().map(id -> new IntTuple(entry.getKey(), id)))
            .collect(Collectors.toSet());

        System.out.println(setOfIntTuples);
    }

    static final class IntTuple
    {
        final int a;
        final int b;

        IntTuple(int a, int b)
        {
            this.a = a;
            this.b = b;
        }

        @Override
        public String toString()
        {
            return "IntTuple{" +
                "a=" + a +
                ", b=" + b +
                '}';
        }
    }
}
