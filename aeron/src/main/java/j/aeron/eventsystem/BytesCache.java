package j.aeron.eventsystem;

import java.util.HashMap;

public class BytesCache {

   private final HashMap<byte[], byte[]> map = new HashMap<>();

    void putIfAbsent(byte[] key, byte[] value) {
       map.putIfAbsent(key, value);
    }

}
