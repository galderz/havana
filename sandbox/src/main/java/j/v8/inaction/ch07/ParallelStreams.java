/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Raoul-Gabriel Urma, Mario Fusco, Alan Mycroft
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package j.v8.inaction.ch07;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ParallelStreams {

   public static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();

   public static void main(String[] args) {
      // Defaults to "Runtime.getRuntime().availableProcessors()" threads, but we can change that:
      // System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "16");

      System.out.println("Iterative Sum done in: " + measurePerf(ParallelStreams::iterativeSum, 10_000_000L) + " msecs");
      System.out.println("Sequential Sum done in: " + measurePerf(ParallelStreams::sequentialSum, 10_000_000L) + " msecs");
      System.out.println("Parallel forkJoinSum done in: " + measurePerf(ParallelStreams::parallelSum, 10_000_000L) + " msecs" );

      // The numeric stream is much faster than the earlier sequential version,
      // generated with the iterate factory method, because the numeric stream
      // avoids all the overhead caused by all the unnecessary autoboxing and
      // unboxing operations performed by the nonspecialized stream
      System.out.println("Range forkJoinSum done in: " + measurePerf(ParallelStreams::rangedSum, 10_000_000L) + " msecs");

      // LongStream.rangeClosed produces ranges of numbers, which can be
      // easily split into independent chunks. For example, the range 1–20 can
      // be split into 1–5, 6–10, 11–15, and 16–20. That's why it's faster
      // when parallelised.
      System.out.println("Parallel range forkJoinSum done in: " + measurePerf(ParallelStreams::parallelRangedSum, 10_000_000L) + " msecs" );

      System.out.println("ForkJoin sum done in: " + measurePerf(ForkJoinSumCalculator::forkJoinSum, 10_000_000L) + " msecs" );

      // Unfortunately, it’s irretrievably broken because it’s fundamentally
      // sequential. You have a data race on every access of total. And if you
      // try to fix that with synchronization, you’ll lose all your parallelism.
      System.out.println("SideEffect sum done in: " + measurePerf(ParallelStreams::sideEffectSum, 10_000_000L) + " msecs" );
      System.out.println("SideEffect prallel sum done in: " + measurePerf(ParallelStreams::sideEffectParallelSum, 10_000_000L) + " msecs" );
   }

   public static long iterativeSum(long n) {
      long result = 0;
      for (long i = 0; i <= n; i++) {
         result += i;
      }
      return result;
   }

   public static long sequentialSum(long n) {
      return Stream.iterate(1L, i -> i + 1).limit(n).reduce(Long::sum).get();
   }

   public static long parallelSum(long n) {
      return Stream.iterate(1L, i -> i + 1).limit(n).parallel().reduce(Long::sum).get();
   }

   public static long rangedSum(long n) {
      return LongStream.rangeClosed(1, n).reduce(Long::sum).getAsLong();
   }

   public static long parallelRangedSum(long n) {
      return LongStream.rangeClosed(1, n).parallel().reduce(Long::sum).getAsLong();
   }

   public static long sideEffectSum(long n) {
      Accumulator accumulator = new Accumulator();
      LongStream.rangeClosed(1, n).forEach(accumulator::add);
      return accumulator.total;
   }

   public static long sideEffectParallelSum(long n) {
      Accumulator accumulator = new Accumulator();
      LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
      return accumulator.total;
   }

   public static class Accumulator {
      private long total = 0;

      public void add(long value) {
         total += value;
      }
   }

   public static <T, R> long measurePerf(Function<T, R> f, T input) {
      long fastest = Long.MAX_VALUE;
      for (int i = 0; i < 10; i++) {
         long start = System.nanoTime();
         R result = f.apply(input);
         long duration = (System.nanoTime() - start) / 1_000_000;
         System.out.println("Result: " + result);
         if (duration < fastest) fastest = duration;
      }
      return fastest;
   }

   public static class ForkJoinSumCalculator extends RecursiveTask<Long> {
      public static final long THRESHOLD = 10_000;

      private final long[] numbers;
      private final int start;
      private final int end;

      public ForkJoinSumCalculator(long[] numbers) {
         this(numbers, 0, numbers.length);
      }

      private ForkJoinSumCalculator(long[] numbers, int start, int end) {
         this.numbers = numbers;
         this.start = start;
         this.end = end;
      }

      @Override
      protected Long compute() {
         int length = end - start;
         if (length <= THRESHOLD) {
            return computeSequentially();
         }
         ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length/2);
         leftTask.fork();
         ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length/2, end);
         Long rightResult = rightTask.compute();
         Long leftResult = leftTask.join();
         return leftResult + rightResult;
      }

      private long computeSequentially() {
         long sum = 0;
         for (int i = start; i < end; i++) {
            sum += numbers[i];
         }
         return sum;
      }

      public static long forkJoinSum(long n) {
         long[] numbers = LongStream.rangeClosed(1, n).toArray();
         ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
         return FORK_JOIN_POOL.invoke(task);
      }
   }

}
