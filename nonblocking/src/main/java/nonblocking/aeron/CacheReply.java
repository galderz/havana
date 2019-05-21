package nonblocking.aeron;

import io.aeron.Publication;
import org.agrona.BufferUtil;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

import static nonblocking.aeron.AeronSystem.AERON;
import static org.agrona.BitUtil.CACHE_LINE_LENGTH;

public class CacheReply {

   private final Publication publication;
   private final UnsafeBuffer buffer;
   private final IdleStrategy retryIdleStrategy;
   private final int retryAttempts;

   public CacheReply() {
      publication = AERON.aeron.addPublication(Constants.CHANNEL, Constants.CACHE_OUT_STREAM);

      final ByteBuffer byteBuffer = BufferUtil.allocateDirectAligned(
         publication.maxMessageLength(), CACHE_LINE_LENGTH);

      buffer = new UnsafeBuffer(byteBuffer);

      retryIdleStrategy = Constants.cacheOutIdleStrategy();
      retryAttempts = Constants.REPLY_RETRIES;
   }

   // TODO use return and maybe log it?
   boolean complete(boolean success, long correlationId) {
      int index = 0;

      buffer.putLong(index, correlationId);
      index += 8;

      buffer.putByte(index, (byte) (success ? 1 : 0));
      index += 1;

      return offer(index);
   }

   private boolean offer(final int length) {
      retryIdleStrategy.reset();

      int attempts = retryAttempts;
      while (true) {
         final long result = publication.offer(buffer, 0, length);
         if (result > 0)
            return true;

         if (result == Publication.CLOSED) {
            throw new RuntimeException("connection to the archive has been closed");
         }

         if (result == Publication.NOT_CONNECTED) {
            throw new RuntimeException("connection to the archive is no longer available");
         }

         if (result == Publication.MAX_POSITION_EXCEEDED) {
            throw new RuntimeException("offer failed due to max position being reached");
         }

         if (--attempts <= 0) {
            return false;
         }

         retryIdleStrategy.idle();
      }
   }

}
