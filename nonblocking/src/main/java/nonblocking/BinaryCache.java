package nonblocking;

public interface BinaryCache {

   boolean putIfAbsent(byte[] key, byte[] value);

   byte[] getOrNull(byte[] key);

   boolean put(byte[] key, byte[] value);

   void invalidateAll();

   void invalidate(byte[] key);

   long count();

}
