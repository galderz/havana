package util;

import java.util.Map;

public class MapOrder
{
    public static void main(String[] args)
    {
        // Order of insertion is not order of iteration
        final var map = Map.of("z", 1, "b", 2, "c", 3, "a", 4);
        for (Map.Entry<String, Integer> e : map.entrySet())
        {
            System.out.println(e.getKey() + ":" + e.getValue());
        }
    }
}
