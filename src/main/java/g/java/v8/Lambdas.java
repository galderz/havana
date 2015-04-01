package g.java.v8;

import java.util.function.*;
import java.util.Arrays;

// https://leanpub.com/whatsnewinjava8/read
public final class Lambdas {

   public static void main(String[] args) {
      Runnable r = () -> System.out.println("lambda runnable function: no parameters, with ()");
      r.run();

      Consumer<String> c1 = (String str) -> System.out.println(str);
      c1.accept("lambda consumer: 1 parameter, with parenthesis");

      Consumer<String> c2 = System.out::println;
      c2.accept("lambda consumer: 1 parameter, without parenthesis");

      BiFunction<String, String, Integer> b1 = (String s1, String s2) -> { return s2.length() - s1.length(); };
      System.out.println(b1.apply("9", "10"));

      BiFunction<String, String, Integer> b2 = (s1, s2) -> s2.length() - s1.length();
      println(b2.apply("9", "10"));

      String[] strArray = {"bbb", "aaaaa", "cc", "d", "zzzzzz"};
      Arrays.sort(strArray, (String s1, String s2) -> s2.length() - s1.length());
      println(Arrays.toString(strArray));
   }

   static final class Scope {
      // TODO...
   }

   static void println(Object o) {
      System.out.println(o.toString());
   }

}
