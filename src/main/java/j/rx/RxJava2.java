package j.rx;

import io.reactivex.Flowable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class RxJava2 {

   public static void main(String[] args) {
      final Flowable<Integer> flowable = Flowable.fromArray(1, 2, 3);

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
         }
      });

//      flowable.subscribe(
//         name -> System.out.println(name)
//      );

      try {
         Thread.sleep(10000);
      } catch (InterruptedException e) {
         e.printStackTrace();  // TODO: Customise this generated block
      }
   }

}
