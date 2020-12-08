package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Lists
{
    public static void main(String[] args)
    {
        System.out.println(List.of("a", "b"));
        System.out.println(List.of(List.of("a"), "b", "c"));
        System.out.println(List.of(List.of("a"), List.of("b", "c")));
        System.out.println(Lists.append("c", List.of("a", "b")));

        System.out.println(Arrays.asList(null, "1", 2));
    }

    static <E> List<E> append(E element, List<E> list)
    {
        final var result = new ArrayList<E>(list);
        result.add(element);
        return Collections.unmodifiableList(result);
    }
}
