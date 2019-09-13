package j.annotations;

import java.time.Duration;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PartialFunctionFallback
{
    public static ReactiveResult<List<Toy>> getHotToys()
    {
        return null;
    }

    public static ReactiveFunction<String, List<Toy>> backupToys()
    {
        return null;
    }

    public static void main(String[] args)
    {
        getHotToys()
            .withTimeout(Duration.ofSeconds(5))
            .recoverWith(partial(backupToys(), "blah"));
    }

    public static <T, R> ReactiveResult<R> partial(ReactiveFunction<? super T, ? extends R> f, T x)
    {
        return () -> f.apply(x);
    }

    static class Toy
    {
    }

    @FunctionalInterface
    interface ReactiveResult<T>
    {
        default <R> ReactiveFunction<T, R> withTimeout(Duration duration)
        {
            return null;
        }

        T get();
    }

    class ReactiveFunction<T, R>
    {
        ReactiveFunction<T, R> withTimeout(Duration duration)
        {
            return null;
        }

        ReactiveResult<R> recoverWith(ReactiveResult<R> r)
        {
            return null;
        }

        R apply(T t)
        {
            return null;
        }
    }

}
