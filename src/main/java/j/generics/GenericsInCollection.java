package j.generics;

import java.util.List;

public class GenericsInCollection {

   public static void main(String... args) {
      C c = new ImplC();
      List<A> as = c.col();
      //List<B> bs = c.col();`
   }

   interface A {

   }

   interface B extends A {

   }

   interface C {
      List<A> col();
   }

   static class ImplC implements C {

      @Override
      public List<A> col() {
         return null;
      }
   }

}
