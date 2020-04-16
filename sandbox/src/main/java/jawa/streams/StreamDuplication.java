package jawa.streams;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class StreamDuplication
{
    public static void main(String[] args)
    {
        // Proper method
        Stream
            .of("a", "b", "c", "a", "a")
            .distinct()
            .forEach(System.out::println);

        // Silly method
        Map<String, String> map = new HashMap<>();
        Stream
            .of("a", "b", "c", "a", "a")
            .filter(s -> !s.isEmpty() && !map.containsKey(s))
            .forEach(s ->
                {
                    System.out.println(s);
                    map.put(s, s);
                }
            );
    }

}
