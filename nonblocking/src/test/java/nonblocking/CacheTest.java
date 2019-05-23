package nonblocking;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CacheTest {

   @Test
   public void testPutIfAbsent() {
      BinaryCache cache = new Caches().cache();
      final byte[] key = {1, 2, 3};
      assertNull(cache.getOrNull(key));
      assertTrue(cache.putIfAbsent(key, new byte[]{4, 5, 6}));
      assertArrayEquals(new byte[]{4, 5, 6}, cache.getOrNull(key));
   }

}
