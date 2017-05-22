package j.v8;

import java.util.Optional;

public class Optionals {

   public static void main(String[] args) {
      {
         // Applies f to its content
         Optional<Integer> o1 = Optional.of(0);
         Optional<Integer> o2 = o1.map(x -> x + 1);
         assert Optional.of(1).equals(o2);
      }
      {
         // Retrieves its content or a default value
         String s1 = Optional.of("a").orElseGet(() -> "b");
         assert s1.equals("a");
         String s2 = Optional.<String>empty().orElseGet(() -> "b");
         assert s2.equals("b");
      }
      {
         // Apply f and get its content or get default value
         Integer i1 = Optional.of(0)
               .map(x -> x + 1)
               .orElseGet(() -> 10);
         assert i1 == 1;
         Integer i2 = Optional.<Integer>empty()
               .map(x -> x + 1)
               .orElseGet(() -> 10);
         assert i2 == 10;
      }
      {
         // Same as map, except g returns Option
         Optional<Integer> o1 = Optional.of(0)
               .flatMap(x -> Optional.of(1));
         assert Optional.of(1).equals(o1);
         Optional<Integer> o2 = Optional.empty()
               .flatMap(x -> Optional.of(1));
         assert Optional.empty().equals(o2);
      }
      {
         // Same as orElse/orElseGet, but returns an Option
         // Useful to select the first valid option from a selection of possible choices.
         Optional<String> o1 = Optional.of("1");
         Optional<String> o2 = Optional.of("2");
         Optional<String> o3 = Optional.of("3");

         Optional<String> user = Optional.empty();
         // user.orElse(o1).orElse(o2).orElse(o3);
         // ^ Not available in Java
      }
   }

}
