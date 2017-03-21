package j.v8;

import java.util.function.BiFunction;
import java.util.function.Function;

// Taken from http://java.dzone.com/articles/higher-order-functions
public class HighOrderCompositionCurrying {

   public static void main(String... args) {
      ConverterCurry converter = new ConverterCurry();

      // Farenheit to Celsius
      Function<Double, Double> farenheit2celsiusConverter =
            converter.compose2((Double n) -> n - 32).curry1(5.0/9);
      double tenFInC = farenheit2celsiusConverter.apply(10.0);
      double twentyFInC = farenheit2celsiusConverter.apply(20.0);
      double fiftyFInC = farenheit2celsiusConverter.apply(50.0);

      // Celsius to Farenheit
      Function<Double, Double> celsius2farenheitConverter =
            converter.curry1(9.0/5).andThen(n -> n + 32);
      double tenCInF = celsius2farenheitConverter.apply(10.0);
      double twentyCInF = celsius2farenheitConverter.apply(20.0);
      double fiftyCInF = celsius2farenheitConverter.apply(50.0);

      Function<Double, Double> mi2kmConverter = converter.curry1(1.609);
      double tenMilesInKm = mi2kmConverter.apply(10.0);
      double twentyMilesInKm = mi2kmConverter.apply(20.0);
      double fiftyMilesInKm = mi2kmConverter.apply(50.0);
      Function<Double, Double> ou2grConverter = converter.curry1(28.345);
      double tenOuncesInGr = ou2grConverter.apply(10.0);
      double twentyOuncesInGr = ou2grConverter.apply(20.0);
      double fiftyOuncesInGr = ou2grConverter.apply(50.0);

      // Simple converter, but obliged to repeat the same conversion rate for all the invocation of our converter
      // Converter converter = new Converter();
      // double tenMilesInKm = converter.apply(1.609, 10.0);
      // double twentyMilesInKm = converter.apply(1.609, 20.0);
      // double fiftyMilesInKm = converter.apply(1.609, 50.0);
   }


   public static class ConverterCurry implements ExtendedBiFunction<Double, Double, Double> {
      @Override
      public Double apply(Double conversionRate, Double value) {
         return conversionRate * value;
      }
   }

   @FunctionalInterface
   public interface ExtendedBiFunction<T, U, R>
         extends BiFunction<T, U, R> {

      default Function<U, R> curry1(T t) {
         return u -> apply(t, u);
      }

      default Function<T, R> curry2(U u) {
         return t -> apply(t, u);
      }

      default <V> ExtendedBiFunction<V, U, R> compose1(Function<? super V, ? extends T> before) {
         return (v, u) -> apply(before.apply(v), u);
      }

      default <V> ExtendedBiFunction<T, V, R> compose2(Function<? super V, ? extends U> before) {
         return (t, v) -> apply(t, before.apply(v));
      }
   }

   public static class Converter implements BiFunction<Double, Double, Double> {
      @Override
      public Double apply(Double conversionRate, Double value) {
         return conversionRate * value;
      }
   }

}
