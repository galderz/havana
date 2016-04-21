package g.java.v8;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MappingStream {

   public static void main(String... args) {
      Map<Integer, Object> data = new HashMap<>();
      data.put(1, "one");
      data.put(2, "two");
      data.put(3, "three");

      Map<Integer, Object> newData = data.entrySet().stream().map(e -> {
         e.setValue(((String) e.getValue()).getBytes());
         return e;
      }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

      System.out.println(newData);

      Stream<Object> s = data.entrySet().stream().map(e -> {
         System.out.println(e);
         return null;
      });
      Iterator<Object> it = s.iterator();
      while(it.hasNext()){
         it.next();
      }
   }

}
