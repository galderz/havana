package j.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;

import java.time.Duration;
import java.util.function.BiFunction;

public class ComputeAndSelectiveExpiration {

   public static void main(String[] args) throws InterruptedException {
      Cache<String, VersionedEntry> cache = Caffeine.newBuilder()
         .expireAfter(new CustomExpiry())
         .build();

      cache.asMap().compute("1",
         new ComputeFn(
            new VersionedEntry("one", 123)
         )
      );

      cache.asMap().compute("2",
         new ComputeFn(
            new VersionedEntry("two", null)
         )
      );

      Thread.sleep(5000);

      System.out.println("Value for 1 is: " + cache.getIfPresent("1"));
      System.out.println("Value for 2 is: " + cache.getIfPresent("2"));

      cache.asMap().compute("1",
         new ComputeFn(
            new VersionedEntry("oneone", null)
         )
      );

      Thread.sleep(5000);

      System.out.println("Value for 1 is: " + cache.getIfPresent("1"));
      System.out.println("Value for 2 is: " + cache.getIfPresent("2"));
   }

   static class CustomExpiry implements Expiry<String, VersionedEntry> {

      @Override
      public long expireAfterCreate(String key, VersionedEntry value, long currentTime) {
         System.out.println("Called expireAfterCreate");

         if (value.version == null) {
            System.out.println("Version is null, return 5 seconds expiration");
            return Duration.ofSeconds(5).toNanos();
         }

         return Long.MAX_VALUE;
      }

      @Override
      public long expireAfterUpdate(String key, VersionedEntry value, long currentTime, long currentDuration) {
         System.out.println("Called expireAfterUpdate");

         if (value.version == null) {
            System.out.println("Version is null, return 5 seconds expiration");
            return Duration.ofSeconds(5).toNanos();
         }

         return Long.MAX_VALUE;
      }

      @Override
      public long expireAfterRead(String key, VersionedEntry value, long currentTime, long currentDuration) {
         System.out.println("Called expireAfterRead");
         return Long.MAX_VALUE;
      }

   }

   static class ComputeFn implements BiFunction<String, VersionedEntry, VersionedEntry> {

      final VersionedEntry entry;

      ComputeFn(VersionedEntry entry) {
         this.entry = entry;
      }

      @Override
      public VersionedEntry apply(String key, VersionedEntry value) {
         System.out.println("Called apply");
         return entry;
      }

   }

   static class VersionedEntry {
      String value;
      Object version;

      public VersionedEntry(String value, Object version) {
         this.value = value;
         this.version = version;
      }
   }

}
