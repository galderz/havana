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
package g.java.v8.inaction.ch05;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

// From Java 8 in Action book
public class StreamProcessing {

   public static void main(String...args){
      // Filtering w/ predicate
      List<Dish> vegetarianDishes =
            Dish.menu.stream()
                  .filter(Dish::isVegetarian)
                  .collect(toList());
      System.out.println(vegetarianDishes);

      // Filtering unique elements
      List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
      numbers.stream()
            .filter(i -> i % 2 == 0)
            .distinct()
            .forEach(System.out::println);

      // Truncating a stream
      List<Dish> highCaloriesDishes = Dish.menu.stream()
            .filter(d -> d.getCalories() > 300)
            .limit(3) // Do not assume ordering in limit!
            .collect(toList());
      System.out.println(highCaloriesDishes);

      // Skipping elements
      List<Dish> skippedDishes = Dish.menu.stream()
            .filter(d -> d.getCalories() > 300)
            .skip(2)
            .collect(toList());
      System.out.println(skippedDishes);

      // How would you use streams to filter the first two meat dishes?
      List<Dish> meatDishes = Dish.menu.stream()
            .filter(d -> d.getType() == Dish.Type.MEAT)
            .limit(2)
            .collect(toList());
      System.out.println(meatDishes);

      // Mapping
      List<String> dishNames = Dish.menu.stream()
            .map(Dish::getName)
            .collect(toList());
      System.out.println(dishNames);

      List<String> words = Arrays.asList("Java8", "Lambdas", "In", "Action");
      List<Integer> wordLengths = words.stream()
            .map(String::length)
            .collect(toList());
      System.out.println(wordLengths);

      List<Integer> dishNamesLengths = Dish.menu.stream()
            .map(d -> d.getName().length())
            // .map(Dish::getName)
            // .map(String::length)
            .collect(toList());
      System.out.println(dishNamesLengths);

      // Flattening
      List<String> distinctLetters = words.stream()
            .map(w -> w.split(""))
            .flatMap(Arrays::stream) // flats all string arrays returned by split
            .distinct()
            .collect(toList());
      System.out.println(distinctLetters);

      List<Integer> numbers2 = Arrays.asList(1, 2, 3, 4, 5);
      List<Integer> squares = numbers2.stream().map(i -> i * i).collect(toList());
      System.out.println(squares);

      List<Integer> l1 = Arrays.asList(1, 2, 3);
      List<Integer> l2 = Arrays.asList(3, 4);
      // Return “[(1, 3), (1, 4), (2, 3), (2, 4), (3, 3), (3, 4)]”
      Stream<int[]> pairs = l1.stream().flatMap(x -> l2.stream().map(y -> new int[]{x, y}));
      pairs.forEach(p -> System.out.println(Arrays.toString(p)));

      System.out.println("---------------------------------------------------");

      // “(2, 4) and (3, 3)”
      Stream<int[]> pairs2 = l1.stream()
            .flatMap(x -> l2.stream()
                  .filter(y -> (x + y) % 3 == 0).map(y -> new int[]{x, y}));
      pairs2.forEach(p -> System.out.println(Arrays.toString(p)));

      // anyMatch
      boolean anyVegetarian = Dish.menu.stream().anyMatch(Dish::isVegetarian);
      // allMatch
      boolean isHealthy = Dish.menu.stream().allMatch(d -> d.calories < 1000);
      // noneMatch
      boolean isHealthy2 = Dish.menu.stream().noneMatch(d -> d.getCalories() >= 1000);

      // Finding an element
      Optional<Dish> dish = Dish.menu.stream().filter(Dish::isVegetarian).findAny();

      // Using Optional to print
      Dish.menu.stream().filter(Dish::isVegetarian)
            .findAny().ifPresent(d -> System.out.println(d.getName()));

      // Sum via reduce
      Integer sum = numbers.stream().reduce(0, (a, b) -> a + b);
      System.out.println(sum);

      // Sum via reduce with method reference
      Integer sum2 = numbers.stream().reduce(0, Integer::sum);
      System.out.println(sum2);

      // Multiply via reduce
      Integer product = numbers.stream().reduce(1, (a, b) -> a * b);
      System.out.println(product);

      // Optional reduce
      List<Integer> maybeNums = Arrays.asList();
      Optional<Integer> maybeSum = maybeNums.stream().reduce((a, b) -> (a + b));
      System.out.println(maybeSum);

      // Max and min via reduce
      Optional<Integer> max = numbers.stream().reduce(Integer::max);
      System.out.println(max);
      Optional<Integer> min = numbers.stream().reduce(Integer::min);
      System.out.println(min);

      // Count number of dishes in a stream using map and reduce
      Integer numOfDishes = Dish.menu.stream().map(d -> 1).reduce(0, Integer::sum);
      System.out.println(numOfDishes);

      // Primitive stream specializations
      int calories = Dish.menu.stream().mapToInt(Dish::getCalories).sum();
      System.out.println(calories);

      // Converting back to a stream of objects
      IntStream intStream = Dish.menu.stream().mapToInt(Dish::getCalories);
      Stream<Integer> stream = intStream.boxed();

      // Default values: OptionalInt
      OptionalInt maybeMaxCalories = Dish.menu.stream().mapToInt(Dish::getCalories).max();
      int maxInt = maybeMaxCalories.orElse(1);
      System.out.println(maxInt);

      // Numeric ranges
      IntStream evenNumbersClosed = IntStream.rangeClosed(1, 100).filter(n -> n % 2 == 0);
      System.out.println(evenNumbersClosed.count()); // expect 50
      IntStream evenNumbers = IntStream.range(1, 100).filter(n -> n % 2 == 0);
      System.out.println(evenNumbers.count()); // expect 49

      // Pythagorean Triples
      Stream<int[]> pythagoreanTriples =  IntStream.rangeClosed(1, 100).boxed().flatMap(
         a -> IntStream.rangeClosed(a, 100).filter(b -> Math.sqrt(a * a + b * b) % 1 == 0).mapToObj(
            b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)})
      );

      pythagoreanTriples.limit(5)
         .forEach(t ->
            System.out.println(t[0] + ", " + t[1] + ", " + t[2]));

      // More efficient Pythagorean Triples by calculating square root only once
      Stream<double[]> pythagoreanTriples2 = IntStream.rangeClosed(1, 100).boxed().flatMap(
         a -> IntStream.rangeClosed(a, 100).mapToObj(
            b -> new double[]{a, b, Math.sqrt(a * a + b * b)}).filter(t -> t[2] % 1 == 0));

      pythagoreanTriples2.limit(5)
         .forEach(t ->
            System.out.println(t[0] + ", " + t[1] + ", " + t[2]));
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

      public enum Type { MEAT, FISH, OTHER }

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
