package j.time;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Time {

   public static void main(String[] args) {
      Date d1 = new Date(1456761616108L);
      System.out.println(d1);
      Date d2 = new Date(1456761614603L);
      System.out.println(d2);

      System.out.println(Duration.ofSeconds(60).toNanos());
      System.out.println(Duration.ofMillis(1).toNanos());

      System.out.println(ChronoUnit.FOREVER.getDuration());
      System.out.println(ChronoUnit.FOREVER.getDuration().equals(ChronoUnit.FOREVER.getDuration()));
   }

}
