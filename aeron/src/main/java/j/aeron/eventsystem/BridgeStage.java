package j.aeron.eventsystem;

import io.aeron.Publication;
import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import org.agrona.BufferUtil;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import static j.aeron.eventsystem.Aeron.AERON;
import static j.aeron.eventsystem.Constants.CACHE_IN_STREAM;
import static j.aeron.eventsystem.Constants.CACHE_OUT_STREAM;
import static org.agrona.BitUtil.CACHE_LINE_LENGTH;

class BridgeStage implements FragmentHandler {

   private final AtomicInteger msgId = new AtomicInteger();
   private final ConcurrentMap<Integer, CompletableFuture> completions = new ConcurrentHashMap<>();
   private final Sink sink = new Sink();
   private final Thread thread;

   public BridgeStage() {
      thread = new Thread(new SubscriptionRunnable(this, CACHE_OUT_STREAM));
      thread.start();
   }

   CompletionStage putIfAbsent(byte[] key, byte[] value) {
      final CompletableFuture future = new CompletableFuture();
      final int msgId = this.msgId.getAndIncrement();
      completions.put(msgId, future);
      EventSystem.cacheStage().sink().putIfAbsent(key, value, msgId);
      return future;
   }

   Sink sink() {
      return sink;
   }

   public void stop() throws InterruptedException {
      thread.join();
   }

   @Override
   public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
      //System.out.printf("[cacheBridge] onFragment(offset=%d, length=%d)", offset, length);
      int index = offset;

      int msgId = buffer.getInt(index);
      index += 4;

      boolean success = buffer.getByte(index) == 1;
      final CompletableFuture future = completions.remove(msgId);
      future.complete(success);
   }

   public class Sink {

      private final Publication publication;
      private final UnsafeBuffer buffer;

      public Sink() {
         publication = AERON.aeron.addPublication(Constants.CHANNEL, CACHE_OUT_STREAM);

         final ByteBuffer byteBuffer = BufferUtil.allocateDirectAligned(
            publication.maxMessageLength(), CACHE_LINE_LENGTH);

         buffer = new UnsafeBuffer(byteBuffer);
      }

      void complete(boolean success, int msgId) {
         int index = 0;
         do {
            buffer.putInt(index, msgId);
            index += 4;

            buffer.putByte(index, (byte) (success ? 1 : 0));
            index += 1;
         } while (publication.offer(buffer, 0, index) < 0);
      }

   }

}
