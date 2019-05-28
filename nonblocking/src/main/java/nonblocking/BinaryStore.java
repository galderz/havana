package nonblocking;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Rudimentary byte[] key/value store
 */
public class BinaryStore
{
    private final HashMap<byte[], byte[]> map = new HashMap<>();

    public boolean putIfAbsent(byte[] key, byte[] value)
    {
        boolean result = getOrNull(key) == null;
        map.put(key, value);
        return result;
    }

    public byte[] getOrNull(byte[] key)
    {
        return map.entrySet().stream()
            .filter(e -> Arrays.equals(e.getKey(), key))
            .map(Entry::getValue).findFirst()
            .orElse(null);
    }

    public void put(byte[] key, byte[] value)
    {
        map.put(key, value);
    }

    public void clear()
    {
        map.clear();
    }

    public void remove(byte[] key)
    {
        final Iterator<Entry<byte[], byte[]>> it = map.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<byte[], byte[]> entry = it.next();
            if (Arrays.equals(entry.getKey(), key)) {
                it.remove();
                return;
            }
        }
    }
}
