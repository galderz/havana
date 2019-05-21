package nonblocking.aeron;

import io.aeron.Subscription;
import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import nonblocking.BinaryCache;
import org.agrona.DirectBuffer;
import org.agrona.LangUtil;
import org.agrona.collections.MutableInteger;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.NanoClock;

import static io.aeron.Aeron.NULL_VALUE;
import static nonblocking.aeron.AeronSystem.AERON;
import static nonblocking.aeron.Constants.CACHE_OUT_STREAM;
import static nonblocking.aeron.Constants.CHANNEL;

public final class AeronCache implements BinaryCache {

   private final CacheProxy cacheProxy;
   private final NanoClock nanoClock;
   private final long messageTimeoutNs;

   public AeronCache() {
      this.cacheProxy = new CacheProxy();
      this.nanoClock = AERON.aeron.context().nanoClock();
      this.messageTimeoutNs = Constants.MESSAGE_TIMEOUT_NS;
   }

   @Override
   public boolean putIfAbsent(byte[] key, byte[] value) {
      final long correlationId = AERON.aeron.nextCorrelationId();

      // Could not publish message so could not put it
      if (!cacheProxy.putIfAbsent(key, value, correlationId))
         return false;

      // If deadline for wait passed, return false to indicate no put
      return pollForResponse(correlationId);
   }

   private boolean pollForResponse(final long correlationId) {
      try (Subscription subs =
              AERON.aeron.addSubscription(CHANNEL, CACHE_OUT_STREAM)) {

         // TODO wrap in image fragment handler? or deal individually?
         final CorrelatingFragmentHandler handler =
            new CorrelatingFragmentHandler(correlationId);

         final IdleStrategy idleStrategy = Constants.cacheOutIdleStrategy();

         final long deadlineNs = nanoClock.nanoTime() + messageTimeoutNs;
         do {
            if (subs.poll(handler, 1) == 0) {
               if (Thread.interrupted())
                  LangUtil.rethrowUnchecked(new InterruptedException());

               if (deadlineNs - nanoClock.nanoTime() < 0)
                  return false;

               idleStrategy.idle();
            }
         } while (NULL_VALUE == handler.result.intValue());

         return handler.result.intValue() == 1;
      }
   }

   static final class CorrelatingFragmentHandler implements FragmentHandler {

      private final MutableInteger result = new MutableInteger(NULL_VALUE);
      private final long correlationId;

      CorrelatingFragmentHandler(long correlationId) {
         this.correlationId = correlationId;
      }

      @Override
      public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
         int index = offset;

         long id = buffer.getLong(index);
         index += 8;

         if (correlationId == id) {
            result.set(buffer.getByte(index));
         }
      }

   }

}
