package lang.nulls;

public class Nulls
{
    private static final String[] EMPTY_CACHE = { null };

    public static final ThisMap map = new ThisMap();

    public static void main(String[] args)
    {
        final var cache = getCacheCarefully();
        final var r0 = loadFromCache(cache, 0);
        final var r1 = loadFromCache(cache, 1);
        final var r10 = loadFromCache(cache, 10);
        System.out.println(r0);
        System.out.println(r1);
        System.out.println(r10);
    }

    private static String[] getCacheCarefully() {
        // racing type.classValueMap{.cacheArray} : null => new Entry[X] <=> new Entry[Y]
        if (map == null)  return EMPTY_CACHE;
        String[] cache = map.getCache();
        return cache;
        // invariant:  returned value is safe to dereference and check for an Entry
    }

    static String loadFromCache(String[] cache, int i) {
        return cache[i & (cache.length-1)];
    }

    static class ThisMap {
        String[] getCache()
        {
            return null;
        }
    }
}
