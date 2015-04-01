package g.java.v8;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// https://leanpub.com/whatsnewinjava8/read
public class ComparisonsToJava7 {

   public static void main(String... args) {
      // createActionListener()
   }

   void joiningStrings() {
      // Java 7 using commons-util
      List<Person> persons = new LinkedList<>();
      // Java 8
      String names = persons.stream()
           .map(Person::getFirstName)
           .collect(Collectors.joining(","));
   }

   void printNumbers() {
      // Java 7
      for (int i = 1; i < 11; i++) {
         System.out.println(i);
      }
      // Java 8
      IntStream.range(1, 11)
            .forEach(System.out::println);
      //or
      Stream.iterate(1, i -> i + 1).limit(10)
            .forEach(System.out::println);
   }

   void average(List<Double> list) {
      double total = 0;
      double ave = 0;
      // Java 7
      for (Double d : list) {
         total += d;
      }
      ave = total / ((double) list.size());
      //Java 8
      ave = list.stream().mapToDouble(Number::doubleValue).average().getAsDouble();
   }

   void findMax(List<Double> list) {
      // Java 7
      double max = 0;

      for (Double d : list) {
         if (d > max) {
            max = d;
         }
      }
      //Java 8
      max = list.stream().reduce(0.0, Math::max);
      // or
      max = list.stream().mapToDouble(Number::doubleValue).max().getAsDouble();
   }

   void sortPersons(List<Person> list) {
      // Java 7
      Collections.sort(list, new Comparator<Person>() {
         @Override
         public int compare(Person p1, Person p2) {
            int n = p1.getLastName().compareTo(p2.getLastName());
            if (n == 0) {
               return p1.getFirstName().compareTo(p2.getFirstName());
            }
            return n;
         }
      });
      // Java 8
      list.sort(Comparator.comparing(Person::getLastName)
            .thenComparing(Person::getFirstName));
   }

   void sortListStrings(List<String> list) {
      // Java 7
      Collections.sort(list, new Comparator<String>() {
         @Override
         public int compare(String s1, String s2) {
            return s1.length() - s2.length();
         }
      });
      //Java 8
      Collections.sort(list, (s1, s2) -> s1.length() - s2.length());
      // or
      list.sort(Comparator.comparingInt(String::length));
   }

   void printListStrings(List<String> list) {
      // Java 7
      for (String s : list) {
         System.out.println(s);
      }
      //Java 8
      list.forEach(System.out::println);
   }

   void createActionListener() {
      // Java 7
      ActionListener al = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            System.out.println(e.getActionCommand());
         }
      };
      // Java 8
      ActionListener al8 = e -> System.out.println(e.getActionCommand());
   }

   public static class Person {

      String firstName;
      String lastName;

      public String getFirstName() {
         return firstName;
      }

      public String getLastName() {
         return lastName;
      }
   }

}
