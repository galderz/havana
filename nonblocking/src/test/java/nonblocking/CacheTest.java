package nonblocking;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class CacheTest {

   @Test
   public void testPutIfAbsent() {
      BinaryCache cache = new Caches().cache();
      final byte[] key = {1, 2, 3};

      assertThat(cache.getOrNull(key), nullValue());
      assertThat(cache.putIfAbsent(key, new byte[]{4, 5, 6}), is(true));
      assertThat(cache.getOrNull(key), is(equalTo(new byte[]{4, 5, 6})));
      assertThat(cache.putIfAbsent(key, new byte[]{4, 5, 6}), is(false));
   }

}
