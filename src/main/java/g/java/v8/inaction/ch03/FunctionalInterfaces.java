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
package g.java.v8.inaction.ch03;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntBiFunction;

// From Java 8 in Action book
public class FunctionalInterfaces {

   public static void main(String... args) throws IOException {
      List<String> names = Arrays.asList("algorta", "", "getxo", "");
      List<String> nonempty = filter(names, (String s) -> !s.isEmpty());
      System.out.println(nonempty);

      forEach(Arrays.asList(1, 2, 3, 4, 5), (Integer i) -> System.out.println(i));

      List<Integer> listAsNumberOfChars = map(Arrays.asList("athletic", "club", "bilbao"), (String s) -> s.length());
      System.out.println(listAsNumberOfChars);

      // Primitive versions
      IntPredicate evenNumbers = (int i) -> i % 2 == 0; // no boxing
      System.out.println(evenNumbers.test(1000));
      Predicate<Integer> oddNumbers = (Integer i) -> i % 2 == 1; // boxing
      System.out.println(oddNumbers.test(1000));

      // Type inference
      forEach(Arrays.asList(1, 2, 3, 4, 5), i -> System.out.println(i));
      String[] alphabet = {"b", "c", "a", "d"};
      Comparator<String> c1 = (String s1, String s2) -> s1.compareTo(s2);
      ToIntBiFunction<String, String> c2 = (s1, s2) -> s1.compareTo(s2);
      BiFunction<String, String, Integer> c3 = (s1, s2) -> s1.compareTo(s2);
      Arrays.sort(alphabet, c1);
      System.out.println(Arrays.toString(alphabet));

      // Local variables
      int portNumber = 1234;
      Runnable r = () -> System.out.println(portNumber);
      r.run();

      // Method references
      Comparator<String> c4 = String::compareTo;
      Arrays.sort(alphabet, c1);
      System.out.println(Arrays.toString(alphabet));

      // More method references
      List<String> str = Arrays.asList("a", "b", "A", "B");
      str.sort(String::compareToIgnoreCase);
      // str.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
      System.out.println(str);

      // Constructor references
      Supplier<StringBuilder> cr1 = StringBuilder::new;
      StringBuilder sb1 = cr1.get();

      Function<Integer, Apple> cr2 = Apple::new;
      Apple a2 = cr2.apply(100);

      List<Integer> weights = Arrays.asList(7, 2, 90, 10);
      List<Apple> apples = map(weights, Apple::new);
      System.out.println(apples);

      BiFunction<String, Integer, Apple> cr3 = Apple::new;
      Apple a3 = cr3.apply("green", 100);

      Map<String, Function<Integer, Fruit>> map = new HashMap<>();
      map.put("apple", Apple::new);
      map.put("orange", Orange::new);
      Fruit fruit = giveMeFruit(map, "orange", 99);
      System.out.println(fruit);

      TriFunction<String, Integer, Boolean, Strawberry> strawberryFactory = Strawberry::new;
      strawberryFactory.apply("freson", 54, true);
   }

   // No such function in Java 8
   public interface TriFunction<T, U, V, R>{
      R apply(T t, U u, V v);
   }

   public static Fruit giveMeFruit(Map<String, Function<Integer, Fruit>> map, String fruit, Integer weight) {
      return map.get(fruit.toLowerCase()).apply(weight);
   }

   // Function
   public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
      List<R> result = new ArrayList<>();
      for (T t: list)
         result.add(f.apply(t));

      return result;
   }

   // Consumer
   public static <T> void forEach(List<T> list, Consumer<T> c) {
      for(T t: list){
         c.accept(t);
      }
   }

   // Predicate
   public static <T> List<T> filter(List<T> list, Predicate<T> p) {
      List<T> results = new ArrayList<>();
      for(T s: list){
         if(p.test(s)){
            results.add(s);
         }
      }
      return results;
   }

   public static class Fruit {
      int weight = 0;
      String color = "";
   }

   public static class Orange extends Fruit {
      public Orange(int weight){
         this.weight = weight;
      }
   }

   public static class Strawberry extends Fruit {
      private final boolean isSweet;

      public Strawberry(String color, int weight, boolean isSweet){
         this.weight = weight;
         this.color = color;
         this.isSweet = isSweet;
      }
   }

   public static class Apple extends Fruit {
      public Apple(String color, int weight){
         this.weight = weight;
         this.color = color;
      }

      public Apple(int weight){
         this.weight = weight;
      }

      public Integer getWeight() {
         return weight;
      }

      public void setWeight(Integer weight) {
         this.weight = weight;
      }

      public String getColor() {
         return color;
      }

      public void setColor(String color) {
         this.color = color;
      }

      public String toString() {
         return "Apple{" +
               "color='" + color + '\'' +
               ", weight=" + weight +
               '}';
      }
   }


}
