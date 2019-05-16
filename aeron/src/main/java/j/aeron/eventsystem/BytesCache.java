package j.aeron.eventsystem;

import java.util.HashMap;

public class BytesCache {

   private final HashMap<byte[], byte[]> map = new HashMap<>();

    boolean putIfAbsent(byte[] key, byte[] value) {
       return map.putIfAbsent(key, value) == null;
    }

}
