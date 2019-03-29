package j.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SafeCaching {

   public static void main(String[] args) throws Exception {
      Entry e = new Entry();
      e.setValue("1");

      String prev = e.getValue();
      Future<Void> future = Executors.newSingleThreadExecutor().submit(new Callable<Void>() {
         @Override
         public Void call() throws Exception {
            e.setValue("2");
            return null;
         }
      });
      future.get();
      System.out.println(prev);
   }

   static class Entry {
      String value;

      public void setValue(String value) {
         this.value = value;
      }

      public String getValue() {
         return value;
      }
   }
}
