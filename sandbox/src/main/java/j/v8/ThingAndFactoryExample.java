package j.v8;

import java.util.Random;

public class ThingAndFactoryExample {

   public static void main(String[] args) {
      ThingAndFactory v1 = ThingAndFactory.mkMyThing();
      System.out.println(v1);
      ThingAndFactory v2 = ThingAndFactory.mkMyThing();
      System.out.println(v2);
   }

}
