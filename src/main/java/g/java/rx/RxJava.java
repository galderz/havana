package g.java.rx;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

public class RxJava {

   public static void main(String[] args) throws Exception {
      String[] names = {"uno", "dos", "tres"};
      Observable.from(names).subscribe(s -> System.out.println("Hola " + s + "!"));
      Thread.sleep(2000);

      Observable.from(names).subscribe(new Observer<String>() {
         @Override
         public void onCompleted() {
            System.out.println("Completed");
         }

         @Override
         public void onError(Throwable e) {
            e.printStackTrace();
         }

         @Override
         public void onNext(String s) {
            System.out.println("Hola " + s + "!");
         }
      });
      Thread.sleep(2000);
   }

}
