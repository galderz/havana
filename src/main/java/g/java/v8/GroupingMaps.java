package g.java.v8;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.summingLong;

public class GroupingMaps {

   public static void main(String... args) {
      Map<String, Long> m1 = new HashMap<>();
      m1.put("a", 1L);

      Map<String, Long> m2 = new HashMap<>();
      m2.put("a", 2L);
      m2.put("b", 2L);

      Map<String, Long> m3 = new HashMap<>();
      m3.put("a", 3L);
      m3.put("b", 3L);
      m3.put("c", 3L);

      List<Map<String, Long>> maps = Arrays.asList(m1, m2, m3);

      Map<String, Long> collected =
         maps.stream()
            .flatMap(m -> m.entrySet().stream())
            .collect(groupingBy(e -> e.getKey(), summingLong(Map.Entry::getValue)));
      System.out.println(collected);
   }

}
