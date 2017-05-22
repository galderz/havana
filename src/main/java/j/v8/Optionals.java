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
   }

}
