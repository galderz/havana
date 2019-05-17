package nonblocking;

public interface BinaryCache {

   boolean putIfAbsent(byte[] key, byte[] value);

}
