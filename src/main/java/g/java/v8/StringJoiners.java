package g.java.v8;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class StringJoiners {

   public static void main(String... args) {
      List<String> strs = Arrays.asList("ggg", "zzz");
      String commaSeparatedNumbers = strs.stream()
            .collect(Collectors.joining("\", \"", "[\"", "\"]"));
      System.out.println(commaSeparatedNumbers);

   }

}
