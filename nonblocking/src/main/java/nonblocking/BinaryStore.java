package nonblocking;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
            .map(Map.Entry::getValue).findFirst()
            .orElse(null);
    }

    public void put(byte[] key, byte[] value)
    {
        map.put(key, value);
    }
}
