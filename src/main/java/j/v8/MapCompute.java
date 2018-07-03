package j.v8;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapCompute {

   public static void main(String[] args) {
      putIfAbsent();
      replace();
      replaceIfEquals();
   }

   private static void replaceIfEquals() {
      {
         ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
         map.put(7, "Bulbasaur");
         map.replace(7, "Bulbasaur", "Squirtle");
         System.out.println(map.get(7));
      }

      {
         ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
         map.put(7, "Bulbasaur");
         map.compute(7, (k, oldV) -> {
            if (oldV != null && oldV.equals("Bulbasaur"))
               return "Squirtle";
            else
               return oldV;
         });
         System.out.println(map.get(7));
      }

      {
         ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
         map.replace(7, "Bulbasaur", "Squirtle");
         System.out.println(map.get(7));
      }

      {
         ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
         map.compute(7, (k, oldV) -> {
            if (oldV != null && oldV.equals("Bulbasaur"))
               return "Squirtle";
            else
               return oldV;
         });
         System.out.println(map.get(7));
      }

      {
         ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
         map.put(7, "Wartortle");
         map.replace(7, "Bulbasaur", "Squirtle");
         System.out.println(map.get(7));
      }

      {
         ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
         map.put(7, "Wartortle");
         map.compute(7, (k, oldV) -> {
            if (oldV != null && oldV.equals("Bulbasaur"))
               return "Squirtle";
            else
               return oldV;
         });
         System.out.println(map.get(7));
      }
   }

   private static void replace() {
      {
         ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
         map.put(4, "Bulbasaur");
         map.replace(4, "Charmander");
         System.out.println(map.get(4));
      }

      {
         ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
         map.put(4, "Bulbasaur");
         map.compute(4, (k, oldV) -> {
            if (oldV != null)
               return "Charmander";
            else
               return oldV;
         });
         System.out.println(map.get(4));
      }

      {
         ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
         map.replace(4, "Charmander");
         System.out.println(map.get(4));
      }

      {
         ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
         map.compute(4, (k, oldV) -> {
            if (oldV != null)
               return "Charmander";
            else
               return oldV;
         });
         System.out.println(map.get(4));
      }
   }

   private static void putIfAbsent() {
      {
         ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
         map.put(1, "Bulbasaur");
         map.putIfAbsent(1, "Squirtle");
         System.out.println(map.get(1));
      }

      {
         ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
         map.put(1, "Bulbasaur");
         map.compute(1, (k, oldV) -> {
            if (oldV != null)
               return oldV;
            else
               return "Squirtle";
         });
         System.out.println(map.get(1));
      }

      {
         ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
         map.putIfAbsent(4, "Squirtle");
         System.out.println(map.get(4));
      }

      {
         ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
         map.compute(4, (k, oldV) -> {
            if (oldV != null)
               return oldV;
            else
               return "Squirtle";
         });
         System.out.println(map.get(4));
      }
   }

}
