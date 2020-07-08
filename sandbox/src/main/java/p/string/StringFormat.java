package p.string;

public class StringFormat {

   public static void main(String[] args) {
      System.out.println(
         String.format("%1$s %1$s %1$s %1$s %1$s %1$s", "hello")
      );

      System.out.println(
         String.format("%2$s %1$s %1$s %1$s %1$s %1$s %1$s", "hello", false)
      );
   }

}
