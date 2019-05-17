package nonblocking;

import java.util.HashMap;

public class BinaryStore {

   private final HashMap<byte[], byte[]> map = new HashMap<>();

   public boolean putIfAbsent(byte[] key, byte[] value) {
      return map.putIfAbsent(key, value) == null;
   }

}
