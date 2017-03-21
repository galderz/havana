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
package j.v8.inaction.ch06;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.*;

public class Grouping {

   enum CaloricLevel { DIET, NORMAL, FAT };

   public static void main(String ... args) {
      System.out.println("Dishes grouped by type: " + groupDishesByType());
      System.out.println("Dishes grouped by caloric level: " + groupDishesByCaloricLevel());
      System.out.println("Dishes grouped by type and caloric level: " + groupDishedByTypeAndCaloricLevel());
      System.out.println("Count dishes in groups: " + countDishesInGroups());
      System.out.println("Most caloric dishes by type: " + mostCaloricDishesByType());
      System.out.println("Most caloric dishes by type: " + mostCaloricDishesByTypeWithoutOptionals());
      System.out.println("Sum calories by type: " + sumCaloriesByType());
      System.out.println("Caloric levels by type: " + caloricLevelsByType());
      System.out.println("Caloric levels by type: " + caloricLevelsByTypeToHashset());
   }

   private static Map<Dish.Type, List<Dish>> groupDishesByType() {
      return Dish.menu.stream().collect(groupingBy(Dish::getType));
   }

   private static Map<CaloricLevel, List<Dish>> groupDishesByCaloricLevel() {
      return Dish.menu.stream().collect(
         groupingBy(dish -> {
            if (dish.getCalories() <= 400) return CaloricLevel.DIET;
            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
            else return CaloricLevel.FAT;
         } ));
   }

   private static Map<Dish.Type, Map<CaloricLevel, List<Dish>>> groupDishedByTypeAndCaloricLevel() {
      return Dish.menu.stream().collect(
         groupingBy(Dish::getType,
            groupingBy((Dish dish) -> {
               if (dish.getCalories() <= 400) return CaloricLevel.DIET;
               else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
               else return CaloricLevel.FAT;
            } )
         )
      );
   }

   private static Map<Dish.Type, Long> countDishesInGroups() {
      return Dish.menu.stream().collect(groupingBy(Dish::getType, counting()));
   }

   private static Map<Dish.Type, Optional<Dish>> mostCaloricDishesByType() {
      return Dish.menu.stream().collect(
         groupingBy(Dish::getType,
            reducing((Dish d1, Dish d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2)));
   }

   private static Map<Dish.Type, Dish> mostCaloricDishesByTypeWithoutOptionals() {
      return Dish.menu.stream().collect(
         groupingBy(Dish::getType,
            collectingAndThen(
               reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2),
               Optional::get)));
   }

   private static Map<Dish.Type, Integer> sumCaloriesByType() {
      return Dish.menu.stream().collect(groupingBy(Dish::getType,
         summingInt(Dish::getCalories)));
   }

   private static Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType() {
      return Dish.menu.stream().collect(
         groupingBy(Dish::getType, mapping(
            dish -> { if (dish.getCalories() <= 400) return CaloricLevel.DIET;
            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
            else return CaloricLevel.FAT; },
            toSet() )));
   }

   private static Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByTypeToHashset() {
      return Dish.menu.stream().collect(
         groupingBy(Dish::getType, mapping(
            dish -> { if (dish.getCalories() <= 400) return CaloricLevel.DIET;
            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
            else return CaloricLevel.FAT; },
            toCollection(HashSet::new))));
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
