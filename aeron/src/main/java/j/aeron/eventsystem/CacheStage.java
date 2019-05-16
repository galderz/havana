package j.aeron.eventsystem;

import io.aeron.Publication;
import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import org.agrona.BufferUtil;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static j.aeron.eventsystem.Aeron.AERON;
import static j.aeron.eventsystem.Constants.CACHE_IN_STREAM;
import static org.agrona.BitUtil.CACHE_LINE_LENGTH;

final class CacheStage implements FragmentHandler {

   private final Sink sink = new Sink();
   private final Thread thread;
   private final BytesCache bytesCache = new BytesCache();

   public CacheStage() {
      thread = new Thread(new SubscriptionRunnable(this, CACHE_IN_STREAM));
      thread.start();
   }

   Sink sink() {
      return sink;
   }

   @Override
   public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
      int index = offset;

      int msgId = buffer.getInt(index);
      index += 4;

      byte method = buffer.getByte(index);
      index++;

      int keyLength = buffer.getInt(index);
      index += 4;

      byte[] key = new byte[keyLength];
      buffer.getBytes(index, key, 0, key.length);
      index += key.length;

      int valueLength = buffer.getInt(index);
      index += 4;

      byte[] value = new byte[valueLength];
      buffer.getBytes(index, value, 0, value.length);

      switch (method) {
         case 0:
            System.out.printf("[msgId=%d] putIfAbsent(key=%s, value=%s)%n", msgId, Arrays.toString(key), Arrays.toString(value));
            boolean success = bytesCache.putIfAbsent(key, value);
            EventSystem.cacheBridge().sink().complete(success, msgId);
            return;
         default:
            System.err.println("Unexpected method: " + method);
      }
   }

   public void stop() throws InterruptedException {
      thread.join();
   }

   static final class Sink {

      private final Publication publication;
      private final UnsafeBuffer buffer;

      public Sink() {
         publication = AERON.aeron.addPublication(Constants.CHANNEL, CACHE_IN_STREAM);

         final ByteBuffer byteBuffer = BufferUtil.allocateDirectAligned(
            publication.maxMessageLength(), CACHE_LINE_LENGTH);

         buffer = new UnsafeBuffer(byteBuffer);
      }

      void putIfAbsent(byte[] key, byte[] value, int msgId) {
         // TODO if offer returns false, don't retry and just send false

         int index = 0;
         do {
            buffer.putInt(msgId, msgId);
            index += 4;

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
         } while (publication.offer(buffer, 0, index) < 0);
      }

   }

}
