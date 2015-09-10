package g.java.rx;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;

public class RxJava {

   public static void main(String[] args) throws Exception {
      String[] names = {"uno", "dos", "tres"};
      Observable.from(names).subscribe(s -> System.out.printf("[%s] Hola %s!%n", Thread.currentThread().getName(), s));
      Thread.sleep(2000);

      Observable.from(names).subscribe(new Observer<String>() {
         @Override
         public void onCompleted() {
            System.out.printf("[%s] Completed%n", Thread.currentThread().getName());
         }

         @Override
         public void onError(Throwable e) {
            e.printStackTrace();
         }

         @Override
         public void onNext(String s) {
            System.out.printf("[%s] Hola %s!%n", Thread.currentThread().getName(), s);
         }
      });
      //Thread.sleep(2000);

      Observable<Integer> count = Observable.from(names).count();
      System.out.println(count.toBlocking().first());

      Observable.from(names).subscribe(new Subscriber<String>() {
         @Override
         public void onCompleted() {
            System.out.println("completed");
         }

         @Override
         public void onError(Throwable e) {
            // TODO: Customise this generated block
         }

         @Override
         public void onNext(String s) {
            System.out.println(s);
            //this.unsubscribe();
         }
      });

      //Observable.from(names).
   }

}
