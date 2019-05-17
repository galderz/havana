package nonblocking.aeron;

import io.aeron.Publication;
import org.agrona.BufferUtil;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

import static nonblocking.aeron.AeronSystem.AERON;
import static nonblocking.aeron.Constants.CACHE_IN_STREAM;
import static nonblocking.aeron.Constants.CHANNEL;
import static org.agrona.BitUtil.CACHE_LINE_LENGTH;

public class CacheProxy {

   private final Publication publication;
   private final UnsafeBuffer buffer;

   public CacheProxy() {
      publication = AERON.aeron.addPublication(CHANNEL, CACHE_IN_STREAM);

      final ByteBuffer byteBuffer = BufferUtil.allocateDirectAligned(
         publication.maxMessageLength(), CACHE_LINE_LENGTH);

      buffer = new UnsafeBuffer(byteBuffer);
   }

   boolean putIfAbsent(
      final byte[] key,
      final byte[] value,
      final long correlationId) {

      int index = 0;

      buffer.putLong(index, correlationId);
      index += 8;

      buffer.putByte(index, (byte) 0); // put method
      index++;

      buffer.putInt(index, key.length);
      index += 4;

      buffer.putBytes(index, key);
      index += key.length;

      buffer.putInt(index, value.length);
      index += 4;

      buffer.putBytes(index, value);
      index += value.length;

      return offer(index);
   }

   private boolean offer(final int length) {
      return publication.offer(buffer, 0, length) > 0;
   }

}
