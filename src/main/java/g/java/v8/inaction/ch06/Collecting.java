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
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;
import static java.util.stream.Collectors.groupingBy;

public class Collecting {

   public static void main(String ... args) {
      groupImperatively();
      groupFunctionally();
      System.out.println("Dishes, custom collector: " + groupDishesCustomCollector());
   }

   private static List<Dish> groupDishesCustomCollector() {
      return Dish.menu.stream().collect(new ToListCollector<Dish>());
   }

   private static void groupFunctionally() {
      Map<Currency, List<Transaction>> transactionsByCurrencies = transactions
         .stream().collect(groupingBy(Transaction::getCurrency));
      System.out.println(transactionsByCurrencies);
   }

   private static void groupImperatively() {
      Map<Currency, List<Transaction>> transactionsByCurrencies = new HashMap<>();
      for (Transaction transaction : transactions) {
         Currency currency = transaction.getCurrency();
         List<Transaction> transactionsForCurrency = transactionsByCurrencies.get(currency);
         if (transactionsForCurrency == null) {
            transactionsForCurrency = new ArrayList<>();
            transactionsByCurrencies.put(currency, transactionsForCurrency);
         }
         transactionsForCurrency.add(transaction);
      }

      System.out.println(transactionsByCurrencies);
   }

   public static List<Transaction> transactions = Arrays.asList(
      new Transaction(Currency.EUR, 1500.0),
      new Transaction(Currency.USD, 2300.0),
      new Transaction(Currency.GBP, 9900.0),
      new Transaction(Currency.EUR, 1100.0),
      new Transaction(Currency.JPY, 7800.0),
      new Transaction(Currency.CHF, 6700.0),
      new Transaction(Currency.EUR, 5600.0),
      new Transaction(Currency.USD, 4500.0),
      new Transaction(Currency.CHF, 3400.0),
      new Transaction(Currency.GBP, 3200.0),
      new Transaction(Currency.USD, 4600.0),
      new Transaction(Currency.JPY, 5700.0),
      new Transaction(Currency.EUR, 6800.0));

   public static class Transaction {
      private final Currency currency;
      private final double value;

      public Transaction(Currency currency, double value) {
         this.currency = currency;
         this.value = value;
      }

      public Currency getCurrency() {
         return currency;
      }

      public double getValue() {
         return value;
      }

      @Override
      public String toString() {
         return currency + " " + value;
      }
   }

   public static enum Currency {
      EUR, USD, JPY, GBP, CHF
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

   public static class ToListCollector<T> implements Collector<T, List<T>, List<T>> {

      @Override
      public Supplier<List<T>> supplier() {
         return ArrayList::new;
         // return () -> new ArrayList<T>(); // <- also valid
      }

      @Override
      public BiConsumer<List<T>, T> accumulator() {
         return List::add;
         // return (list, item) -> list.add(item);
      }

      @Override
      public BinaryOperator<List<T>> combiner() {
         return (list1, list2) -> {
            list1.addAll(list2);
            return list1;
         };
      }

      @Override
      public Function<List<T>, List<T>> finisher() {
         return Function.identity();
      }

      @Override
      public Set<Characteristics> characteristics() {
         return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH, CONCURRENT));
      }
   }

}
