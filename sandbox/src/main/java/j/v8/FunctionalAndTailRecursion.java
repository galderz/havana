package j.v8;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.stream.Stream;

final public class FunctionalAndTailRecursion {

   public static void main(String... args) {
      Function<Integer, String> f = Function.<Integer>identity()
            .andThen(i -> 2 * i).andThen(i -> "str" + i);
      System.out.println(f.apply(1));
      System.out.println(f.apply(10));
      System.out.println(fastFactorial(10));
   }

   public static Long fastFactorial(int n) {
      return fastFactorial(1L, n).invoke();
   }

   private static Tail<Long> fastFactorial(long x, int n) {
      return () -> {
         switch (n) {
            case 1:
               return done(x);
            default:
               return fastFactorial(x * n, n - 1);
         }
      };
   }

   static <T> Tail<T> done(final T value) {
      return new Tail<T>() {
         @Override
         public T result() {
            return value;
         }

         @Override
         public boolean isDone() {
            return true;
         }

         @Override
         public Tail<T> apply() {
            throw new UnsupportedOperationException("Not supported.");
         }
      };
   }

   @FunctionalInterface
   public interface Tail<T> {

      Tail<T> apply();

      default boolean isDone() {
         return false;
      }

      default T result() {
         throw new UnsupportedOperationException("Not done yet.");
      }

      default T invoke() {
         return Stream.iterate(this, Tail::apply)
               .filter(Tail::isDone)
               .findFirst()
               .get()
               .result();
      }
   }

   public Function<LocalDate, LocalDateTime> dateTimeFunction(final Function<LocalDate, LocalDate> f) {
      return f.andThen(d -> d.atTime(2, 2));
   }

//   public Streams.Dragon closestDragon(Location location) {
//      AtomicReference<DragonDistance> closest = new AtomicReference<>(DragonDistance.worstMatch());
//      CountDownLatch latch = new CountDownLatch(dragons.size());
//      dragons.forEach(dragon -> {
//         CompletableFuture.supplyAsync(() -> dragon.distance(location))
//               .thenAccept(result -> {
//                  closest.accumulateAndGet(result, DragonDistance::closest);
//                  latch.countDown();
//               });
//      });
//      try {
//         latch.await();
//      } catch (InterruptedException e) {
//         throw new RuntimeException("Interrupted during calculations", e);
//      }
//      return closest.get().getDragon();
//   }
//
//   public Streams.Dragon closestDragon(Location location) {
//      return dragons.parallelStream()
//            .map(dragon -> dragon.distance(location))
//            .reduce(DistancePair.worstMatch(), DragonDistance::closest)
//            .getDragon();
//   }
}
