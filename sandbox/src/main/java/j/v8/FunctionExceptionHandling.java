package j.v8;

import java.util.function.Function;

public class FunctionExceptionHandling {

   public static void main(String[] args) {
      // Happy path calls first, second and third functions
      // happyPath();

      // If second throws an exception, third is not called
      // unhappyPath();

      happyfierPath();
   }

   private static void happyfierPath() {
      Function<String, Integer> first = s -> {
         System.out.println("Called first");
         return Integer.parseInt(s);
      };

      Function<Integer, Integer> second = i -> {
         System.out.println("Called second");
         throw new RuntimeException("boo");
      };

      Function<Integer, Object[]> happyfier = i -> {
         try {
            return new Object[]{second.apply(i)};
         } catch (Throwable t) {
            return new Object[]{i, t};
         }
      };

      Function<Object[], String> third = obj -> {
         System.out.println("Called third");
         
         if (obj.length > 0) {
            final Throwable t = (Throwable) obj[1];
            throw new AssertionError(t);
         }

         return String.valueOf(obj[0]);
      };

      System.out.println(first.andThen(happyfier).andThen(third).apply("1"));
   }

   private static void unhappyPath() {
      Function<String, Integer> first = s -> {
         System.out.println("Called first");
         return Integer.parseInt(s);
      };

      Function<Integer, Integer> second = i -> {
         System.out.println("Called second");
         throw new RuntimeException("boo");
      };

      Function<Integer, String> third = i -> {
         System.out.println("Called third");
         return String.valueOf(i);
      };

      System.out.println(first.andThen(second).andThen(third).apply("1"));
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
