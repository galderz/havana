package j.v8;

import java.util.function.Function;

public class FunctionExceptionHandling {

   public static void main(String[] args) {
      happyPath();
   }

   public static void happyPath() {
      Function<String, Integer> first = s -> {
         System.out.println("Called first");
         return Integer.parseInt(s);
      };

      Function<Integer, Integer> second = i -> {
         System.out.println("Called second");
         return -i;
      };

      Function<Integer, String> third = i -> {
         System.out.println("Called third");
         return String.valueOf(i);
      };

      System.out.println(first.andThen(second).andThen(third).apply("1"));
   }

}
