package j.rx;

import io.reactivex.Flowable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RxJava2 {

   public static void main(String[] args) {
      final Flowable<Integer> flowable = Flowable.fromArray(1, 2, 3);

      CompletableFuture<String> future = new CompletableFuture<>();

      flowable.subscribe(new Subscriber<Integer>() {
         @Override public void onSubscribe(Subscription s) {
            // IMPORTANT: Required
            s.request(Long.MAX_VALUE);
         }

         @Override
         public void onNext(Integer v) {
            System.out.println(v);
         }

         @Override public void onError(Throwable t) {}

         @Override
         public void onComplete() {
            System.out.println("Complete");
            future.complete("Complete");
         }
      });

      try {
         future.get(5, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
         e.printStackTrace();  // TODO: Customise this generated block
      } catch (ExecutionException e) {
         e.printStackTrace();  // TODO: Customise this generated block
      } catch (TimeoutException e) {
         e.printStackTrace();  // TODO: Customise this generated block
      }
   }

}
