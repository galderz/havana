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
package g.java.v8.inaction.ch06;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;
import static java.util.stream.Collectors.*;

public class Partitioning {

   public static void main(String... args) {
      System.out.println("Dishes partitioned by vegetarian: " + partitionByVegeterian());
      System.out.println("Vegetarian Dishes by type: " + vegetarianDishesByType());
      System.out.println("Most caloric dishes by vegetarian: " + mostCaloricPartitionedByVegetarian());

      System.out.println("Numbers partitioned in prime and non-prime: " + partitionPrimes(100));
      System.out.println("Numbers partitioned in prime and non-prime: " + partitionPrimesWithCustomCollector(100));

      System.out.println("Partitioning done in: " + execute(Partitioning::partitionPrimes) + " msecs");
      System.out.println("Partitioning done in: " + execute(Partitioning::partitionPrimesWithCustomCollector) + " msecs" );
   }

   private static long execute(Consumer<Integer> primePartitioner) {
      long fastest = Long.MAX_VALUE;
      for (int i = 0; i < 10; i++) {
         long start = System.nanoTime();
         primePartitioner.accept(1_000_000);
         long duration = (System.nanoTime() - start) / 1_000_000;
         if (duration < fastest) fastest = duration;
         System.out.println("done in " + duration);
      }
      return fastest;
   }

   public static Map<Boolean, List<Integer>> partitionPrimes(int n) {
      return IntStream.rangeClosed(2, n).boxed()
         .collect(partitioningBy(Partitioning::isPrime));
   }

   public static boolean isPrime(int candidate) {
      return IntStream.rangeClosed(2, candidate-1)
         .limit((long) Math.floor(Math.sqrt((double) candidate)) - 1)
         .noneMatch(i -> candidate % i == 0);
   }

   public static Map<Boolean, List<Integer>> partitionPrimesWithCustomCollector(int n) {
      return IntStream.rangeClosed(2, n).boxed().collect(new PrimeNumbersCollector());
   }

   public static boolean isPrime(List<Integer> primes, Integer candidate) {
      double candidateRoot = Math.sqrt((double) candidate);
      //return primes.stream().filter(p -> p < candidateRoot).noneMatch(p -> candidate % p == 0);
      return takeWhile(primes, i -> i <= candidateRoot).stream().noneMatch(i -> candidate % i == 0);
   }

   public static <A> List<A> takeWhile(List<A> list, Predicate<A> p) {
      int i = 0;
      for (A item : list) {
         if (!p.test(item)) {
            return list.subList(0, i);
         }
         i++;
      }
      return list;
   }

   public static class PrimeNumbersCollector
      implements Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> {

      @Override
      public Supplier<Map<Boolean, List<Integer>>> supplier() {
         return () -> new HashMap<Boolean, List<Integer>>() {{
            put(true, new ArrayList<Integer>());
            put(false, new ArrayList<Integer>());
         }};
      }

      @Override
      public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
         return (Map<Boolean, List<Integer>> acc, Integer candidate) -> {
            acc.get(isPrime(acc.get(true), candidate))
               .add(candidate);
         };
      }

      // Note that in reality this collector can’t be used in parallel,
      // because the algorithm is inherently sequential. This means the
      // combiner method won’t ever be invoked, and you could leave its
      // implementation empty (or better, throw an UnsupportedOperation-Exception).
      // We decided to implement it anyway only for completeness.
      @Override
      public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
         return (Map<Boolean, List<Integer>> map1, Map<Boolean, List<Integer>> map2) -> {
            map1.get(true).addAll(map2.get(true));
            map1.get(false).addAll(map2.get(false));
            return map1;
         };
      }

      @Override
      public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
         return Function.identity();
         // return i -> i; // <- also valid!
      }

      @Override
      public Set<Characteristics> characteristics() {
         return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH));
      }
   }

   public Map<Boolean, List<Integer>> partitionPrimesWithInlineCollector(int n) {
      return Stream.iterate(2, i -> i + 1).limit(n)
         .collect(
            () -> new HashMap<Boolean, List<Integer>>() {{
               put(true, new ArrayList<Integer>());
               put(false, new ArrayList<Integer>());
            }},
            (acc, candidate) -> {
               acc.get( isPrime(acc.get(true), candidate) )
                  .add(candidate);
            },
            (map1, map2) -> {
               map1.get(true).addAll(map2.get(true));
               map1.get(false).addAll(map2.get(false));
            });
   }

   private static Map<Boolean, List<Dish>> partitionByVegeterian() {
      return Dish.menu.stream().collect(partitioningBy(Dish::isVegetarian));
   }

   private static Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType() {
      return Dish.menu.stream().collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType)));
   }

   private static Object mostCaloricPartitionedByVegetarian() {
      return Dish.menu.stream().collect(
         partitioningBy(Dish::isVegetarian,
            collectingAndThen(
               maxBy(comparingInt(Dish::getCalories)),
               Optional::get)));
   }

   public static class Dish {

      private final String name;
      private final boolean vegetarian;
      private final int calories;
      private final Type type;

      public Dish(String name, boolean vegetarian, int calories, Type type) {
         this.name = name;
         this.vegetarian = vegetarian;
         this.calories = calories;
         this.type = type;
      }

      public String getName() {
         return name;
      }

      public boolean isVegetarian() {
         return vegetarian;
      }

      public int getCalories() {
         return calories;
      }

      public Type getType() {
         return type;
      }

      public enum Type {MEAT, FISH, OTHER}

      @Override
      public String toString() {
         return name;
      }

      public static final List<Dish> menu =
         Arrays.asList(new Dish("pork", false, 800, Dish.Type.MEAT),
            new Dish("beef", false, 700, Dish.Type.MEAT),
            new Dish("chicken", false, 400, Dish.Type.MEAT),
            new Dish("french fries", true, 530, Dish.Type.OTHER),
            new Dish("rice", true, 350, Dish.Type.OTHER),
            new Dish("season fruit", true, 120, Dish.Type.OTHER),
            new Dish("pizza", true, 550, Dish.Type.OTHER),
            new Dish("prawns", false, 400, Dish.Type.FISH),
            new Dish("salmon", false, 450, Dish.Type.FISH));
   }

}

