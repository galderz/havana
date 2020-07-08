package p.numbers;

public class Numbers {

   public static void main(String[] args) {
//      int x = 10;
//      int y = 23;
//      int z = 256;
//
//      System.out.println(256 % 10);
//      System.out.println(23 % 10);
//
//      System.out.println(i);

      System.out.println(numberToOrdinal(23));
      System.out.println(numberToOrdinal(1));
      System.out.println(numberToOrdinal(31));
      System.out.println(numberToOrdinal(10));
      System.out.println(numberToOrdinal(112));
   }

   private static String numberToOrdinal(int i) {
      final int tenths = i % 100;
      switch (tenths) {
         case 11:
         case 12:
         case 13:
            return i + "th";
         default:
            final int x = i % 10;
            switch (x) {
               case 1:
                  return i + "st";
               case 2:
                  return i + "nd";
               case 3:
                  return i + "rd";
               default:
                  return i + "th";
            }
      }
   }

}
