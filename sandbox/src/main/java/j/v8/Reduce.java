package j.v8;

import java.util.Arrays;
import java.util.List;

public class Reduce {

   public static void main(String...args) {
      List<Wrapper> wrappers = Arrays.asList(new Wrapper(1), new Wrapper(2), new Wrapper(3));
      Integer sum = wrappers.stream().reduce(0, (acc, w) -> acc + w.count, (x, y) -> x + y);
      System.out.println(sum);
   }

   static class Wrapper {
      int count;

      public Wrapper(int count) {
         this.count = count;
      }
   }

}
