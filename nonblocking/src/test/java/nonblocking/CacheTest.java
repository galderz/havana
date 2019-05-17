package nonblocking;

import nonblocking.aeron.AeronCache;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CacheTest {

   @Test
   public void testPutIfAbsent() {
      BinaryCache cache = new Caches().cache();
      assertTrue(cache.putIfAbsent(new byte[]{1, 2, 3}, new byte[]{4, 5, 6}));
   }

}
