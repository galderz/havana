package g.java.v8;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

// https://jacobsvanroy.be/blog/jdk8-lazy-infinite-fibonacci-stream/
public class FibonacciStreams {

   public static void main(String... args) {
      // Without limit of numbers, but rather with a limit predicate
      BigInteger ceiling = new BigInteger("4000000");
      BigInteger two = new BigInteger("2");
      BigInteger result = getStream(x -> x.compareTo(ceiling) < 0)
            .filter(x -> x.remainder(two).equals(BigInteger.ZERO))
            .reduce(BigInteger.ZERO, BigInteger::add);
      System.out.println("Final sum is: " + result);

      // With limit of numbers
      Stream.generate(new FibonacciSupplier())
            .limit(15)
            .forEach(System.out::println);

      // sumWithIntermediateResults();
   }

   public static Stream<BigInteger> getStream(Predicate<BigInteger> predicate) {
      FibonacciIterator iterator = new FibonacciIterator(predicate);
      Spliterator<BigInteger> sp = Spliterators.spliteratorUnknownSize(iterator,
            Spliterator.IMMUTABLE | Spliterator.NONNULL);
      return StreamSupport.stream(sp, false);
   }

   public static class FibonacciSupplier implements Supplier<BigInteger> {
      BigInteger x, y;

      public FibonacciSupplier() {
         x = new BigInteger("-1");
         y = BigInteger.ONE;
      }

      @Override
      public BigInteger get() {
         BigInteger result = x.add(y);
         x = y;
         y = result;
         return result;
      }
   }

   public static class FibonacciIterator implements Iterator<BigInteger> {
      Predicate<BigInteger> predicate;
      BigInteger x, y;

      public FibonacciIterator(Predicate<BigInteger> predicate) {
         this.predicate = predicate;
         x = new BigInteger("-1");
         y = BigInteger.ONE;
      }

      @Override
      public BigInteger next() {
         BigInteger result = x.add(y);
         x = y;
         y = result;
         return result;
      }

      @Override
      public boolean hasNext() {
         return predicate.test(x.add(y));
      }
   }

   public static void sumWithIntermediateResults() {
      int finalResult =
            IntStream
                  .iterate(0, x -> x + 1)
                  .limit(5)
                  .reduce(0, (x, y) -> {
                     int sum = x + y;
                     System.out.println("Intermediate result for sum: " + sum);
                     return sum;
                  });
      System.out.println(finalResult);
   }

}
