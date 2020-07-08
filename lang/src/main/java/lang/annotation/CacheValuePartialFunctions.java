package lang.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CacheValuePartialFunctions
{
    @CacheStore
    List<Toy> updateToys(Country country)
    {
        return new ArrayList<>();
    }

    @CacheStore
    List<Toy> appendToy(@CacheKey Country country, Toy toy)
    {
        return new ArrayList<>();
    }

    public static <T, U, R> Function<U, R> partial(BiFunction<T, U, R> f, T x)
    {
        return (y) -> f.apply(x, y);
    }


    public static void main(String[] args)
    {
        BiFunction<Integer, Integer, Integer> minus = (x, y) -> x - y;

    }

//    Function<Country, List<Toy>> prependToy(Toy toy) {
//        @CacheStore
//        return country -> {
//            List<Toy> toys = getHotToys();
//            ... // update the list with toy parameter
//            return toys;
//        };
//    }

//    Function<Country, List<Toy>> prependToy(Toy toy) {
//        return new Function<Country, List<Toy>>()
//        {
//            @Override
//            @CacheStore
//            public List<Toy> apply(Country country)
//            {
//                return new ArrayList<>();
//            }
//        };
//    }

    class Toy
    {
    }

    class Country
    {
    }
}
