package nonblocking.aeron;

import io.aeron.Aeron;
import io.aeron.ControlledFragmentAssembler;
import io.aeron.Subscription;
import io.aeron.logbuffer.ControlledFragmentHandler;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;

import static nonblocking.aeron.AeronSystem.AERON;

public class ResponsePoller implements ControlledFragmentHandler {

   private final Subscription subscription;
   private final int fragmentLimit;

   private long correlationId = Aeron.NULL_VALUE;
   private boolean pollComplete = false;
   private Object result;

   private final ControlledFragmentAssembler fragmentAssembler =
      new ControlledFragmentAssembler(this);

   public ResponsePoller() {
      this.subscription = AERON.aeron.addSubscription(Constants.CHANNEL, Constants.CACHE_OUT_STREAM);
      this.fragmentLimit = Constants.FRAGMENT_LIMIT;
   }

   public int poll() {
      correlationId = -1;
      pollComplete = false;

      return subscription.controlledPoll(fragmentAssembler, fragmentLimit);
   }

   Object result() {
      return result;
   }

   /**
    * Get the {@link Subscription} used for polling responses.
    *
    * @return the {@link Subscription} used for polling responses.
    */
   Subscription subscription()
   {
      return subscription;
   }

   /**
    * Has the last polling action received a complete message?
    *
    * @return true if the last polling action received a complete message?
    */
   boolean isPollComplete() {
      return pollComplete;
   }

   /**
    * Correlation id of the last polled message or {@link Aeron#NULL_VALUE} if poll returned nothing.
    *
    * @return correlation id of the last polled message or {@link Aeron#NULL_VALUE} if poll returned nothing.
    */
   public long correlationId()
   {
      return correlationId;
   }

   @Override
   public Action onFragment(
      final DirectBuffer buffer,
      final int offset,
      final int length,
      final Header header) {

      if (pollComplete) {
         return Action.ABORT;
      }

      int index = offset;

      correlationId = buffer.getLong(index);
      index += 4;

      result = buffer.getByte(index) == 0;
      pollComplete = true;

      return Action.BREAK;
   }

}
