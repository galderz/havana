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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Comparator.comparing;

// From Java 8 in Action book
public class OtherLambdaExprs {

   public static void main(String... args) {
      List<Apple> inventory = new ArrayList<>();
      inventory.addAll(Arrays.asList(new Apple(80, "green"),
            new Apple(155, "green"), new Apple(120, "white"),
            new Apple(120, "red")));

      // Normal sorting
      inventory.sort(comparing(Apple::getWeight));
      System.out.println(inventory);

      // Reversed
      inventory.sort(comparing(Apple::getWeight).reversed());
      System.out.println(inventory);

      // Fallback comparison
      inventory.sort(comparing(Apple::getWeight)
            .reversed()
            .thenComparing(Apple::getColor));
      System.out.println(inventory);

      // Predicate helpers
      Predicate<Apple> redApple = a -> a.getColor().equals("red");
      Predicate<Apple> notRedApple = redApple.negate();
      Predicate<Apple> redAndHeavyApple = redApple.and(a -> a.getWeight() > 150);
      Predicate<Apple> redAndHeavyAppleOrGreen = redApple
            .and(a -> a.getWeight() > 150)
            .or(a -> a.getColor().equals("green"));

      // Function helpers
      Function<Integer, Integer> f = x -> x + 1;
      Function<Integer, Integer> g = x -> x * 2;
      Function<Integer, Integer> h = f.andThen(g);
      int result = h.apply(1);
      System.out.println(result);

      Function<Integer, Integer> h2 = f.compose(g);
      int result2 = h2.apply(1);
      System.out.println(result2);

      Function<String, String> addHeader = OtherLambdaExprs::addHeader;
      Function<String, String> transformationPipeline
            = addHeader.andThen(OtherLambdaExprs::checkSpelling)
            .andThen(OtherLambdaExprs::addFooter);

      String rtxt = transformationPipeline.apply("labda in action");
      System.out.println(rtxt);
   }

   public static String addHeader(String text){
      return "From Raoul, Mario and Alan: " + text;
   }

   public static String addFooter(String text){
      return text + " Kind regards";
   }

   public static String checkSpelling(String text){
      return text.replaceAll("labda", "lambda");
   }

   public static class Apple {
      private Integer weight = 0;
      private String color = "";

      public Apple(Integer weight, String color){
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
