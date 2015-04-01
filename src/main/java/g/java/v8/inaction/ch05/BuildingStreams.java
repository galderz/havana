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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BuildingStreams {

   public static void main(String[] args) throws IOException {
      Stream<String> stream = Stream.of("Java 8 ", "Lambdas ", "In ", "Action");
      stream.map(String::toUpperCase).forEach(System.out::println);

      Stream<String> emptyStream = Stream.empty();

      int[] numbers = {2, 3, 5, 7, 11, 13};
      int sum = Arrays.stream(numbers).sum();
      System.out.println(sum);

      long uniqueWords = Files.lines(Paths.get("src/javag/v8/inaction/ch05/data.txt"), Charset.defaultCharset())
         .flatMap(line -> Arrays.stream(line.split(" ")))
         .distinct()
         .count();
      System.out.println(uniqueWords);

      // Creating infinite streams - Iterate
      // In general, you should use iterate when you need to produce a
      // sequence of successive values, for example, a date followed by its
      // next date: January 31, February 1, and so on
      Stream<Integer> limit = Stream.iterate(0, n -> n + 2).limit(10);
      limit.forEach(System.out::println);
      // Fibonacci
      Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
         .limit(20)
         .forEach(t -> System.out.println("(" + t[0] + "," + t[1] +")"));

      // Creating infinite streams - Generate
      Stream.generate(Math::random)
         .limit(5)
         .forEach(System.out::println);

      // Fibonacci with generate, requires a stateful function
      // (not recommended since it cannot be paralellized safely, but as example)
      IntSupplier fib = new IntSupplier(){
         private int previous = 0;
         private int current = 1;
         public int getAsInt(){
            int oldPrevious = this.previous;
            int nextValue = this.previous + this.current;
            this.previous = this.current;
            this.current = nextValue;
            return oldPrevious;
         }
      };
      IntStream.generate(fib).limit(10).forEach(System.out::println);
   }

}
