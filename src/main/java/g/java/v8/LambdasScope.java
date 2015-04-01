package g.java.v8;

import static java.lang.System.out;

// https://leanpub.com/whatsnewinjava8/read
public class LambdasScope {

   Runnable r1 = () -> out.println(this);
   Runnable r2 = () -> out.println(toString());

   public String toString() { return "Hello, world!"; }

   public static void main(String... args) {
      new LambdasScope().r1.run(); //Hello, world!
      new LambdasScope().r2.run(); //Hello, world!
   }

}
