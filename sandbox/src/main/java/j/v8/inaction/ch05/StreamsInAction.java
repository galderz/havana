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
package j.v8.inaction.ch05;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class StreamsInAction {

   public static void main(String[] args) {
      Trader raoul = new Trader("Raoul", "Cambridge");
      Trader mario = new Trader("Mario","Milan");
      Trader alan = new Trader("Alan","Cambridge");
      Trader brian = new Trader("Brian","Cambridge");

      List<Transaction> transactions = Arrays.asList(
            new Transaction(brian, 2011, 300),
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950)
      );

      // 1. Find all transactions in the year 2011 and sort them by value (small to high).
      List<Transaction> orderedTxs2011 = transactions.stream()
            .filter(t -> t.year == 2011)
            .sorted(comparing(Transaction::getValue))
            .collect(toList());
      System.out.println(orderedTxs2011);

      System.out.println("--");

      // 2. What are all the unique cities where the traders work?
      transactions.stream().map(t -> t.getTrader().getCity()).distinct()
            .forEach(System.out::println);
      // Alternative way, by taking advance of toSet()
      Object uniqueCities = transactions.stream().map(t -> t.getTrader().getCity())
            .collect(toSet());
      System.out.println(uniqueCities);

      System.out.println("--");

      // 3. Find all traders from Cambridge and sort them by name.
      transactions.stream().map(Transaction::getTrader)
            .distinct()
            .filter(t -> t.getCity().equals("Cambridge"))
            .sorted(comparing(Trader::getName))
            .forEach(System.out::println);

      System.out.println("--");

      // 4. Return a string of all traders’ names sorted alphabetically.
      String allNames = transactions.stream().map(Transaction::getTrader)
            .distinct()
            .map(Trader::getName)
            .sorted()
            .reduce("", (a, b) -> a + "," + b);
      System.out.println(allNames);
      // More efficient way to do it with joining(), which uses a string builder behind it
      String joiningAllNames = transactions.stream().map(Transaction::getTrader)
            .distinct()
            .map(Trader::getName)
            .sorted()
            .collect(joining());
      System.out.println(joiningAllNames);

      System.out.println("--");

      // 5. Are any traders based in Milan?
      transactions.stream().map(Transaction::getTrader)
            .distinct()
            .filter(t -> t.getCity().equals("Milan"))
            .forEach(System.out::println);
      // With anyMatch
      boolean anyMilan = transactions.stream().anyMatch(t -> t.getTrader().getCity().equals("Milan"));
      System.out.println(anyMilan);

      System.out.println("--");

      // 6. Print all transactions’ values from the traders living in Cambridge.
      transactions.stream().filter(t -> t.getTrader().getCity().equals("Cambridge"))
            .forEach(t2 -> System.out.println(t2.getValue()));

      System.out.println("--");

      // 7. What’s the highest value of all the transactions?
      Integer maxValue = transactions.stream().map(Transaction::getValue)
            .reduce(-1, Integer::max);
      System.out.println(maxValue);

      System.out.println("--");

      // 8. Find the transaction with the smallest value.
      Optional<Transaction> smallestValueTransaction = transactions.stream().reduce((ta, tb) -> {
         if (ta.getValue() < tb.getValue()) return ta;
         else return tb;
      });
      System.out.println(smallestValueTransaction);
      // Better, using comparing
      Optional<Transaction> smallestValueTransaction2 = transactions.stream()
            .min(comparing(Transaction::getValue));
      System.out.println(smallestValueTransaction2);

   }

   public static class Transaction {
      private Trader trader;
      private int year;
      private int value;

      public Transaction(Trader trader, int year, int value) {
         this.trader = trader;
         this.year = year;
         this.value = value;
      }

      public Trader getTrader() {
         return this.trader;
      }

      public int getYear() {
         return this.year;
      }

      public int getValue() {
         return this.value;
      }

      public String toString() {
         return "{" + this.trader + ", " +
               "year: " + this.year + ", " +
               "value:" + this.value + "}";
      }
   }

   public static class Trader {

      private String name;
      private String city;

      public Trader(String n, String c) {
         this.name = n;
         this.city = c;
      }

      public String getName() {
         return this.name;
      }

      public String getCity() {
         return this.city;
      }

      public void setCity(String newCity) {
         this.city = newCity;
      }

      public String toString() {
         return "Trader:" + this.name + " in " + this.city;
      }
   }

}
