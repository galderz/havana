package g.java.numbers;

public class Bitwise {

   public static void main(String[] args) {
      // & operator is to check whether a number is even or odd
      System.out.printf("123 is odd? %b%n", (123 & 1) == 1);
      System.out.printf("122 is odd? %b%n", (123 & 1) != 1);

      // & and | operators to allow us to pass multiple options to a function in a single int
      //...
      System.out.println("2 ^ 55: " + Math.pow(2, 55));
      System.out.println("Hex: " + 0x80000000000002L);
   }
}
