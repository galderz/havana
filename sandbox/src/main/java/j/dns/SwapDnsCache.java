package j.dns;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.LinkedHashMap;

public class SwapDnsCache {

   public static void main(String[] args) throws Exception {
      final Field addressCacheField = InetAddress.class.getDeclaredField("addressCache");
      addressCacheField.setAccessible(true);
      final Object addressCache = addressCacheField.get(InetAddress.class);
      System.out.println(addressCache);

      final Method cacheInit = InetAddress.class.getDeclaredMethod("cacheInitIfNeeded");
      cacheInit.setAccessible(true);
      cacheInit.invoke(addressCache);

      final Field cacheField = addressCache.getClass().getDeclaredField("cache");
      cacheField.setAccessible(true);

      final Object cache = cacheField.get(addressCache);
      System.out.println(cache);

      //final LinkedHashMap<String, Object> newValue = new LinkedHashMap<>();
      //final LinkedHashMap<String, Object> newValue = new LinkedHashMap() {};
      final LinkedHashMap<String, Object> newValue = new MockMap();

      cacheField.set(addressCache, newValue);

      System.out.println(addressCache);

      System.out.println(cacheField.get(addressCache));

//      cacheField.set(new LinkedHashMap<String, Object>(), cache);
   }

   private static final class MockMap extends LinkedHashMap {}

}
