package nonblocking;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class CacheTest
{
    static BinaryCache CACHE;

    @BeforeClass
    public static void createCache() {
        CACHE = new Caches().cache();
    }

    @AfterClass
    public static void destroyCache() {
        // TODO
    }

    @Test
    public void testPutIfAbsent()
    {
        final byte[] key = {1, 2, 3};

        assertThat(CACHE.getOrNull(key), nullValue());
        assertThat(CACHE.putIfAbsent(key, new byte[]{4, 5, 6}), is(true));
        assertThat(CACHE.getOrNull(key), is(equalTo(new byte[]{4, 5, 6})));
        assertThat(CACHE.putIfAbsent(key, new byte[]{4, 5, 6}), is(false));
    }

    @Test
    public void testPut()
    {
        final byte[] key = {7, 8, 9};

        assertThat(CACHE.getOrNull(key), nullValue());
        assertThat(CACHE.put(key, new byte[]{10, 11, 12}), is(true));
        assertThat(CACHE.getOrNull(key), is(equalTo(new byte[]{10, 11, 12})));
        assertThat(CACHE.put(key, new byte[]{10, 11, 12}), is(true));
    }

    @Test
    public void testInvalidateAll()
    {
        final byte[] k1 = {13, 14, 15};
        final byte[] k2 = {16, 17, 18};

        assertThat(CACHE.putIfAbsent(k1, new byte[]{19, 20, 21}), is(true));
        assertThat(CACHE.putIfAbsent(k2, new byte[]{22, 23, 24}), is(true));
        assertThat(CACHE.getOrNull(k1), is(equalTo(new byte[]{19, 20, 21})));
        assertThat(CACHE.getOrNull(k2), is(equalTo(new byte[]{22, 23, 24})));
        CACHE.invalidateAll();
        assertThat(CACHE.getOrNull(k1), is(nullValue()));
        assertThat(CACHE.getOrNull(k2), is(nullValue()));
    }

    @Test
    public void testInvalidate()
    {
        final byte[] key = {25, 26, 27};

        assertThat(CACHE.putIfAbsent(key, new byte[]{28, 29, 30}), is(true));
        assertThat(CACHE.getOrNull(key), is(equalTo(new byte[]{28, 29, 30})));
        CACHE.invalidate(key);
        assertThat(CACHE.getOrNull(key), is(nullValue()));
    }
}
