package time;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import static java.time.temporal.TemporalAdjusters.*;

// https://leanpub.com/whatsnewinjava8/read
public class DateAndTime {
   public static void main(String... args) throws Exception {
      // In Java 7...
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.HOUR, 8);
      cal.getTime(); // actually returns a Date

      System.out.println(System.currentTimeMillis());
      System.out.println(Instant.now().toEpochMilli());

      // In Java 8, you can more simply write the following:
      LocalTime now = LocalTime.now();
      LocalTime later = now.plus(8, ChronoUnit.HOURS);
      LocalDate today = LocalDate.now();
      LocalDate thirtyDaysFromNow = today.plusDays(30);
      LocalDate nextMonth = today.plusMonths(1);
      LocalDate aMonthAgo = today.minusMonths(1);

      // Creation
      LocalDate date = LocalDate.of(2014, 3, 15);
      date = LocalDate.of(2014, Month.MARCH, 15);
      LocalTime time = LocalTime.of(12, 15, 0);
      LocalDateTime datetime = date.atTime(time);

      // Enums
      LocalDate nextWeek = today.plus(1, ChronoUnit.WEEKS);
      nextMonth = today.plus(1, ChronoUnit.MONTHS);
      LocalDate nextYear = today.plus(1, ChronoUnit.YEARS);
      LocalDate nextDecade = today.plus(1, ChronoUnit.DECADES);

      // Clock
      time = LocalTime.now(Clock.systemDefaultZone());

      // Period and Duration
      Period p = Period.between(thirtyDaysFromNow, nextMonth);
      Duration d = Duration.between(now, later);
      Duration twoHours = Duration.ofHours(2);
      Duration tenMinutes = Duration.ofMinutes(10);
      Duration thirtySecs = Duration.ofSeconds(30);
      LocalTime t2 = time.plus(twoHours);

      // Temporal Adjusters
      LocalDate nextTuesday = LocalDate.now().with(next(DayOfWeek.TUESDAY));

      // Time Zones
      ZoneId mountainTime = ZoneId.of("America/Denver");
      ZoneId myZone = ZoneId.systemDefault();
      System.out.println(ZoneId.getAvailableZoneIds());

      // Backwards Compatibility
      Date ddate = new Date();
      Instant nnow = ddate.toInstant();
      LocalDateTime dateTime = LocalDateTime.ofInstant(nnow, myZone);
      ZonedDateTime zdt = ZonedDateTime.ofInstant(nnow, myZone);
   }
}