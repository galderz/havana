package j.v8;

import java.util.function.Function;
import static java.lang.System.out;

// https://leanpub.com/whatsnewinjava8/read
public class FunctionalInterfaces {

   public static void main(String... args) {
      Function<String, String> atr = (name) -> {return "@" + name;};
      Function<String, Integer> leng = (name) -> {return name.length();};
      Function<String, Integer> leng2 = String::length;

      for (String s : args) out.println(leng2.apply(s));
   }

}
