package j.v8;

import java.util.Arrays;
import java.util.StringJoiner;

public class ArrayToStringIndex {

   public static void main(String... args) {
      String[] arr = new String[]{"a", "b", "c"};
      System.out.println(Arrays.toString(arr));

      System.out.println(toStringWithIndex(arr));
   }

   static String toStringWithIndex(String[] arr) {
      StringJoiner sj = new StringJoiner(", ", "[", "]");
      for (int i = 0; i < arr.length; i++)
         sj.add(i + ":" + arr[i]);

      return sj.toString();
   }

}
