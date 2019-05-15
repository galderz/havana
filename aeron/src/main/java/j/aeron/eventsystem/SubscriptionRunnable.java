package j.aeron.eventsystem;

import io.aeron.ImageFragmentAssembler;
import io.aeron.Subscription;
import io.aeron.logbuffer.FragmentHandler;
import org.agrona.LangUtil;
import org.agrona.concurrent.BackoffIdleStrategy;
import org.agrona.concurrent.IdleStrategy;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static j.aeron.eventsystem.Constants.FRAGMENT_COUNT_LIMIT;

final class SubscriptionRunnable implements Runnable {

   final Subscription subscription;
   final FragmentHandler fragmentHandler;

   SubscriptionRunnable(FragmentHandler fragmentHandler, int streamId) {
      this.fragmentHandler = new ImageFragmentAssembler(fragmentHandler);
      this.subscription = Aeron.AERON.aeron.addSubscription(Constants.CHANNEL, streamId);
   }

   @Override
   public void run() {
      // Avoid busy spin idle strategy to give CPU some rest
      final IdleStrategy idleStrategy =
         new BackoffIdleStrategy(1, 1, 1, 1);

      subscriberLoop(fragmentHandler, FRAGMENT_COUNT_LIMIT, Aeron.AERON.running, idleStrategy)
         .accept(subscription);

      System.out.println();
      System.out.println("Shutting down...");
   }

   private static Consumer<Subscription> subscriberLoop(
      final FragmentHandler fragmentHandler,
      final int limit,
      final AtomicBoolean running,
      final IdleStrategy idleStrategy) {

      return
         (subscription) ->
         {
            try {
               while (running.get()) {
                  final int fragmentsRead = subscription.poll(fragmentHandler, limit);
                  idleStrategy.idle(fragmentsRead);
               }
            } catch (final Exception ex) {
               LangUtil.rethrowUnchecked(ex);
            }
         };
   }

}
