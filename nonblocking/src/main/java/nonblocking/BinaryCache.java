package nonblocking;

public interface BinaryCache {

   boolean putIfAbsent(byte[] key, byte[] value);

   byte[] getOrNull(byte[] key);

}
