package j.aeron.eventsystem;

import org.agrona.concurrent.SigInt;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class Example {

   public static void main(String[] args) throws InterruptedException, ExecutionException {
      SigInt.register(() -> Aeron.AERON.running.set(false));

      // Not fully an event driven system
      // since there's a requirement to tie in with sync apis.
      // Separate APIs in:
      // CacheIn.putIfAbsent returns a CompletionStage
      // CacheOut.complete() returns void

      final CompletionStage future = EventSystem.cacheBridge().putIfAbsent("123".getBytes(), "456".getBytes());
      final boolean success = (boolean) future.toCompletableFuture().get();
      System.out.println("putIfAbsent: " + success);

      EventSystem.stop();
   }

}
