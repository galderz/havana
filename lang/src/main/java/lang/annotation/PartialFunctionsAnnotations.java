package j.annotations;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PartialFunctionsAnnotations
{
//    public static <T, U, R> Function<U, R> partialAppend(BiFunction<T, U, R> f, T x)
//    {
//        @CacheStore
//        return (y) -> f.apply(x, y);
//    }

    public static <T, U, R> Function<U, R> partial(BiFunction<T, U, R> f, T x)
    {
        return new Function<U, R>() {
            @Override
            @CacheStore
            public R apply(U y)
            {
                return f.apply(x, y);
            }
        };
    }

    public static void main(String[] args)
    {
        BiFunction<Toy, Country, List<Toy>> append = (toy, country) -> {
            List<Toy> toys = getHotToys(country);
            toys.add(toy);
            return toys;
        };

        Function<Country, List<Toy>> appendTo = partial(append, new Toy());
        final List<Toy> toys = appendTo.apply(new Country());
    }

    @CacheLoad(cacheName="toys")
    public static List<Toy> getHotToys(Country country) {
        // return expensiveService.getHotToys(country);
        return null;
    }

    static class Toy
    {
    }

    static class Country
    {
    }

}
