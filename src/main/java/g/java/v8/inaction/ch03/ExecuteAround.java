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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// From Java 8 in Action book
public class ExecuteAround {

   public static void main(String... args) throws IOException {
      String oneline = processFile((BufferedReader r) -> r.readLine());
      System.out.println(oneline);

      String twolines = processFile((BufferedReader r) -> r.readLine() + r.readLine());
      System.out.println(twolines);
   }

   public static String processFile(BufferedReaderProcessor p) throws IOException {
      try(BufferedReader br = new BufferedReader(new FileReader("javag.iml"))) {
         return p.process(br);
      }
   }

   @FunctionalInterface
    public interface BufferedReaderProcessor {
      String process(BufferedReader b) throws IOException;
   }

}
