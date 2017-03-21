package j.numbers;

public class ComposedNumber {

   public static void main(String[] args) {
      short type = 0x1000;
      int id = 1;
//      System.out.println(type | id);
//      System.out.println((type | id) & 0xF0000000);
      type = 0x5000;
      id = 10;
      System.out.println(type | id);
      System.out.println((type | id) & 0xF000);
//      System.out.println((type | id) << 4);
//      System.out.println(Test.values()[0]);
//      System.out.println(Test.values()[1]);
//      System.out.println(Test.values()[2]);
//      System.out.println(type >> 12);
//      System.out.println((type | id) & 0x0FFF);

      System.out.println(0x05 & 0x10);
      System.out.println(0x11 & 0x10);
      System.out.println(0x81 & 0x10);
   }

   enum Test {
      A,
      B,
      C
   }

   interface ListenerInterface<T> {
      void handleEvent(T event);
   }

   public enum MySingleton implements ListenerInterface<Object> {
      INSTANCE;

      @SuppressWarnings("unchecked")
      public static <T> ListenerInterface<T> getListener() {
         return (ListenerInterface<T>) INSTANCE;
      }

      public void handleEvent(Object event) {
         System.out.println("I am a singleton. An event happened: " + event);
      }

   }
}
