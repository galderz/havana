package jawa.string;

public class Replace {

   public static void main(String[] args) {
      String key = "abcd.hibernate.cache.com.acme.EntityA.memory.object.count";
      final String replaced = key.replace("abcd.", "");
      System.out.println(replaced);
   }

}
