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
package g.java.v8.inaction.ch04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

// From Java 8 in Action book
public class BasicStreams {

   public static void main(String...args){
      // Java 7
      getLowCaloricDishesNamesInJava7(Dish.menu).forEach(System.out::println);

      System.out.println("---");

      // Java 8
      getLowCaloricDishesNamesInJava8(Dish.menu).forEach(System.out::println);

      List<String> title = Arrays.asList("Java 8", "in", "Action");
      Stream<String> s = title.stream();
      s.forEach(System.out::println);
      // s.forEach(System.out::println); <- throws IllegalStateException
   }

   public static List<String> getLowCaloricDishesNamesInJava7(List<Dish> dishes){
      List<Dish> lowCaloricDishes = new ArrayList<>();
      for(Dish d: dishes){
         if(d.getCalories() > 400){
            lowCaloricDishes.add(d);
         }
      }
      List<String> lowCaloricDishesName = new ArrayList<>();
      Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
         public int compare(Dish d1, Dish d2) {
            return Integer.compare(d1.getCalories(), d2.getCalories());
         }
      });
      for(Dish d: lowCaloricDishes){
         lowCaloricDishesName.add(d.getName());
      }
      return lowCaloricDishesName;
   }

   public static List<String> getLowCaloricDishesNamesInJava8(List<Dish> dishes){
      return dishes.stream()
            .filter(d -> {
               System.out.printf("filtering %s%n", d.getName());
               return d.getCalories() > 400;
            })
            .sorted(comparing(Dish::getCalories))
            .map(d -> {
               System.out.printf("mapping %s%n", d.getName());
               return d.getName();
            })
            .limit(3)
            .collect(toList());
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
