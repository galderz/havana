package j.v8;

import java.util.Random;

public interface ThingAndFactory {

   static ThingAndFactory mkMyThing() {
      Random r = new Random();
      int next = r.nextInt();
      return next % 2 == 0
            ? new Thing1()
            : new Thing2();
   }

   final class Thing1 implements ThingAndFactory {
   }

   final class Thing2 implements ThingAndFactory {
   }

}
