package g.java.v8;

import static java.lang.System.out;

// https://leanpub.com/whatsnewinjava8/read
public class MultipleDefaultMethods {

   interface Foo {
      default void talk() {
         out.println("Foo!");
      }
   }

   interface Bar {
      // Compilation error if multiple defaults found? IntelliJ does not throw error...
//      default void talk() {
//         out.println("Bar!");
//      }
   }

   class FooBar implements Foo, Bar {
      @Override
      public void talk() {
         Foo.super.talk();
      }
   }

}
