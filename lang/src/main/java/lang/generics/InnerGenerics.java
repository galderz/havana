package lang.generics;

import java.util.function.Supplier;

public class InnerGenerics {

   public static void main(String[] args) {
      Supplier<A<?>> bar = () -> new A<>();
      //foo(bar); // <- does not compile :-(

      Supplier<? extends A<?>> bar2 = () -> new A<>();
      //foo2(bar2); // <- still does not compile :-(
   }

   static class A<T> {}

   static <X> void foo(Supplier<A<X>> a) {}

   static <X> void foo2(Supplier<? extends A<X>> a) {}
}
