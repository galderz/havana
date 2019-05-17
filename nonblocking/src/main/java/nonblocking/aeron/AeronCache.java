package nonblocking.aeron;

import io.aeron.exceptions.TimeoutException;
import nonblocking.BinaryCache;
import org.agrona.LangUtil;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.NanoClock;

import java.util.function.Function;

import static nonblocking.aeron.AeronSystem.AERON;

public final class AeronCache implements BinaryCache {

   private static final Function<Long, Boolean> FALSE_ON_DEADLINE = x -> false;
   private static final Function<Long, Boolean> TIMEOUT_ON_DEADLINE =
      correlationId -> {
         throw new TimeoutException("awaiting response - correlationId=" + correlationId);
      };

   private final CacheProxy cacheProxy;
   private final NanoClock nanoClock;
   private final long messageTimeoutNs;
   private final IdleStrategy idleStrategy;
   private final ResponsePoller responsePoller;

   public AeronCache() {
      this.cacheProxy = new CacheProxy();
      this.nanoClock = AERON.aeron.context().nanoClock();
      this.messageTimeoutNs = Constants.MESSAGE_TIMEOUT_NS;
      this.idleStrategy = Constants.CACHE_OUT_IDLE_STRATEGY;
      this.responsePoller = new ResponsePoller();
   }

   @Override
   public boolean putIfAbsent(byte[] key, byte[] value) {
      final long correlationId = AERON.aeron.nextCorrelationId();

      // Could not publish message so could not put it
      if (!cacheProxy.putIfAbsent(key, value, correlationId))
         return false;

      // If deadline for wait passed, return false to indicate no put
      return pollForResponse(correlationId, FALSE_ON_DEADLINE);
   }

   private <T> T pollForResponse(
      final long correlationId,
      final Function<Long, Boolean> deadlineFun) {

      final long deadlineNs = nanoClock.nanoTime() + messageTimeoutNs;
      final ResponsePoller poller = responsePoller;

      while (true) {
         final Function<Long, Object> deadlineAction =
            pollNextResponse(deadlineNs, poller, deadlineFun);

         if (deadlineAction != null)
            return (T) deadlineAction.apply(correlationId);

//         if (poller.controlSessionId() != controlSessionId ||
//            poller.templateId() != ControlResponseDecoder.TEMPLATE_ID) {
//            invokeAeronClient();
//            continue;
//         }

         if (poller.correlationId() == correlationId)
            return (T) poller.result();

//         final ControlResponseCode code = poller.code();
//         if (ControlResponseCode.ERROR == code) {
//            final ArchiveException ex = new ArchiveException("response for correlationId=" + correlationId +
//               ", error: " + poller.errorMessage(), (int) poller.relevantId());
//
//            if (poller.correlationId() == correlationId) {
//               throw ex;
//            } else if (context.errorHandler() != null) {
//               context.errorHandler().onError(ex);
//            }
//         } else if (poller.correlationId() == correlationId) {
//            if (ControlResponseCode.OK != code) {
//               throw new ArchiveException("unexpected response code: " + code);
//            }
//
//            return (T) poller.result();
//         }
      }
   }

   private <T> Function<Long, T> pollNextResponse(
      final long deadlineNs,
      final ResponsePoller poller,
      final Function<Long, Boolean> deadlineFun) {

      idleStrategy.reset();

      while (true) {
         final int fragments = poller.poll();

         if (poller.isPollComplete()) {
            break;
         }

         if (fragments > 0) {
            continue;
         }

//         if (!poller.subscription().isConnected()) {
//            throw new RuntimeException("subscription to cache is not connected");
//         }

         Function<Long, T> deadlineAction = checkDeadline(deadlineNs, deadlineFun);
         if (deadlineAction != null)
            return deadlineAction;

         idleStrategy.idle();
      }

      return null;
   }

   private <T> Function<Long, T> checkDeadline(
      final long deadlineNs,
      final Function<Long, Boolean> deadlineFun) {

      if (Thread.interrupted())
         LangUtil.rethrowUnchecked(new InterruptedException());

      if (deadlineNs - nanoClock.nanoTime() < 0)
         return (Function<Long, T>) deadlineFun;

      return null;
   }

}
