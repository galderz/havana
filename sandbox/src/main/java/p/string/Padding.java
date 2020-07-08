package p.string;

public class Padding {

   public static void main(String[] args) {
      System.out.println(padRight("Howto", ' ', 20) + "*");
      System.out.println(padLeft("Howto", ' ', 20) + "*");

      System.out.println(padLeft("5", '0', 5));
      System.out.println(padRight("5", '0', 5));
   }

   public static String padLeft(String s, char padding, int n) {
      return String.format("%" + n + "s", s).replace(' ', padding);
   }

   public static String padRight(String s, char padding, int n) {
      return String.format("%-" + n + "s", s).replace(' ', padding);
   }

}
