package j.v8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

// https://leanpub.com/whatsnewinjava8/read
public class Streams {

   public static void main(String... args) {
      // Create an infinite Random number supply
      Stream.generate(() -> Math.random());
      // Creating ranges of numbers
      IntStream.range(1, 11).forEach(System.out::println);
   }

   void mapReduce() {
      List<String> names = new ArrayList<>();
      PlayerPoints highestPlayer =
         names.stream().map(name -> new PlayerPoints(name, 1L))
         .reduce(new PlayerPoints("", 0),
            (s1, s2) -> (s1.points > s2.points) ? s1 : s2);
   }

   void partitioning(List<Dragon> dragons) {
      // Group by whether or not the dragon is green
      Map<Boolean, List<Dragon>> map = dragons.stream()
            .collect(partitioningBy(Dragon::isGreen));
   }

   void grouping(List<Dragon> dragons) {
      // Group by first letter of name
      Map<Character, List<Dragon>> map = dragons.stream()
            .collect(groupingBy(dragon -> dragon.getName().charAt(0)));
   }

   void maxMinCount2() throws IOException {
      IntSummaryStatistics stats = Files.lines(Paths.get("Nio.java"))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .mapToInt(String::length)
            .summaryStatistics();
   }

   void maxMinCount() throws IOException {
      IntSummaryStatistics stats = Files.lines(Paths.get("Nio.java"))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(summarizingInt(String::length));

      System.out.println(stats.getAverage());
      System.out.println("count=" + stats.getCount());
      System.out.println("max=" + stats.getMax());
      System.out.println("min=" + stats.getMin());
   }

   void average() throws IOException {
      System.out.println("\n----->Average line length:");
      System.out.println(
            Files.lines(Paths.get("Nio.java"))
                  .map(String::trim)
                  .filter(s -> !s.isEmpty())
                  .collect(averagingInt(String::length))
      );
   }

   void simpleCollectors(List<Dragon> dragons) {
      // Accumulate names into a List
      List<String> list = dragons.stream()
            .map(Dragon::getName)
            .collect(Collectors.toList());

      // Accumulate names into a TreeSet
      Set<String> set = dragons.stream()
            .map(Dragon::getName)
            .collect(Collectors.toCollection(TreeSet::new));
   }

   void sortAndLimit() throws IOException {
      // first 5 java file names
      Files.list(Paths.get("."))
            .map(Path::getFileName) // still a path
            .map(Path::toString) // convert to Strings
            .filter(name -> name.endsWith(".java"))
            .sorted() // sort them alphabetically
            .limit(5) // first 5
            .forEach(System.out::println);
   }

   void streamTextPatterns() {
      Pattern patt = Pattern.compile(",");
      patt.splitAsStream("a,b,c")
            .forEach(System.out::println);
   }

   void readLinesFiles() throws IOException {
      try (Stream st = Files.lines(Paths.get("file"))) {
         st.forEach(System.out::println);
      }
   }

   void readLinesBufferedReader() throws IOException {
      try (FileReader fr = new FileReader("file");
           BufferedReader br = new BufferedReader(fr)) {
         br.lines().forEach(System.out::println);
      }
   }

   public static class Dragon {

      String name;

      public String getName() {
         return name;
      }

      public static <T> boolean isGreen(T t) {
         return false;
      }
   }


   public static class PlayerPoints {
      public final String name;
      public final long points;

      public PlayerPoints(String name, long points) {
         this.name = name;
         this.points = points;
      }

      public String toString() {
         return name + ":" + points;
      }
   }

}
