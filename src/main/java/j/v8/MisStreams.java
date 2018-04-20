package j.v8;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MisStreams {

   public static void main(String[] args) {
      Map<String, Long> m = IntStream.iterate(0, i -> i + 1)
         .limit(10)
         .filter(i -> i % 2 == 0)
         .peek(System.out::println)
          .boxed()
         .collect(Collectors.groupingBy(
            i -> i > 5 ? "mas que 5" : "menos que 5",
            Collectors.counting()
         ));

      System.out.println(m);

   }

}
