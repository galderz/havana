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
package g.java.v8.inaction.ch02;

import java.util.Arrays;
import java.util.List;

// From Java 8 in Action book
public class MoreLambdas {

   public static void main(String... args) {
      List<Apple> inventory = Arrays.asList(new Apple(80, "green"),
            new Apple(155, "green"),
            new Apple(120, "red"));

      prettyPrintApple(inventory, (Apple a) -> a.toString());
      prettyPrintApple(inventory, Apple::toString);
   }

   public static void prettyPrintApple(List<Apple> inventory, ApplePrinter p){
      for(Apple apple: inventory) {
         String output = p.print(apple);
         System.out.println(output);
      }
   }

   public interface ApplePrinter {
      String print(Apple a);
   }

   public static class Apple {
      private int weight = 0;
      private String color = "";

      public Apple(int weight, String color){
         this.weight = weight;
         this.color = color;
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
