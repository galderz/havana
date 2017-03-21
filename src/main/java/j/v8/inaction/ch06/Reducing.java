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
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.BinaryOperator;

import static java.util.stream.Collectors.*;

public class Reducing {

   public static void main(String ... args) {
      System.out.println("Nr. of dishes: " + howManyDishes());
      System.out.println("The most caloric dish is: " + findMostCaloricDish());
      System.out.println("The most caloric dish is: " + findMostCaloricDishUsingComparator());
      System.out.println("Total calories in menu: " + calculateTotalCalories());
      System.out.println("Average calories in menu: " + calculateAverageCalories());
      System.out.println("Menu statistics: " + calculateMenuStatistics());
      System.out.println("Short menu: " + getShortMenu());
      System.out.println("Short menu comma separated: " + getShortMenuCommaSeparated());
   }

   private static long howManyDishes() {
      return Dish.menu.stream().collect(counting());
   }

   private static Dish findMostCaloricDishUsingComparator() {
      Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
      BinaryOperator<Dish> moreCaloricOf = BinaryOperator.maxBy(dishCaloriesComparator);
      return Dish.menu.stream().collect(reducing(moreCaloricOf)).get();
   }

   private static Dish findMostCaloricDish() {
      return Dish.menu.stream().collect(
         reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2)).get();
   }

   private static int calculateTotalCalories() {
      return Dish.menu.stream().collect(summingInt(Dish::getCalories));
   }

   private static Double calculateAverageCalories() {
      return Dish.menu.stream().collect(averagingInt(Dish::getCalories));
   }

   private static IntSummaryStatistics calculateMenuStatistics() {
      return Dish.menu.stream().collect(summarizingInt(Dish::getCalories));
   }

   private static String getShortMenu() {
      return Dish.menu.stream().map(Dish::getName).collect(joining());
   }

   private static String getShortMenuCommaSeparated() {
      return Dish.menu.stream().map(Dish::getName).collect(joining(", "));
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
