package j.aeron.eventsystem;

public class Example {

   public static void main(String[] args) throws InterruptedException {
      //final AeronSubscriber subscriber = new AeronSubscriber();
      //final AeronPublisher publisher = new AeronPublisher();

      final AsyncCache asyncCache = new AsyncCache();

      // Not fully an event driven system
      // since there's a requirement to tie in with sync apis.
      // Separate APIs in:
      // CacheIn.putIfAbsent returns a CompletionStage
      // CacheOut.complete() returns void

      asyncCache.putIfAbsent("123".getBytes(), "456".getBytes());
      Thread.sleep(60000);
   }

}
