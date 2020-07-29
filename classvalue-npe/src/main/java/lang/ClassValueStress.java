package lang;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClassValueStress
{
    static final int NUM_ITERATIONS = Integer.getInteger("num.iterations", Integer.MAX_VALUE);
    static final int NUM_CORES = Integer.getInteger("num.cores", Runtime.getRuntime().availableProcessors() - 1);

    public static void main(String[] args) throws Exception
    {
        needEnabledAsserts();
        System.out.printf("Number of iterations: %d%n", NUM_ITERATIONS);
        System.out.printf("Number of cores: %d%n", NUM_CORES);

        final var executor = Executors.newFixedThreadPool(NUM_CORES);
        IntStream.range(0, NUM_ITERATIONS).forEach(runIteration(executor));

        executor.shutdown();
    }

    private static IntConsumer runIteration(ExecutorService executor)
    {
        return i ->
        {
            if (i % 1000 == 0)
                System.out.printf("Iteration %d%n", i);

            final var barrier = new CyclicBarrier(NUM_CORES + 1);

            final var futures =
                IntStream.range(0, NUM_CORES)
                    .mapToObj(x -> fireArrayGetter(barrier, executor))
                    .collect(Collectors.toList());

            // Wait for all threads to reach starting point
            barrierAwait(barrier);

            futures.forEach(ClassValueStress::assertMethodHandle);
        };
    }

    private static void assertMethodHandle(Future<MethodHandle> future)
    {
        final var methodHandle = futureGet(future);
        assert methodHandle != null;
    }

    private static Future<MethodHandle> fireArrayGetter(CyclicBarrier barrier, ExecutorService executor)
    {
        return executor.submit(new ArrayGetter(barrier));
    }

    static class ArrayGetter implements Callable<MethodHandle>
    {
        final CyclicBarrier barrier;

        ArrayGetter(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public MethodHandle call() throws Exception
        {
            final Class<?> type = newDynamicArrayType();
            barrier.await();
            return MethodHandles.arrayElementGetter(type);
        }

        private Class<?> newDynamicArrayType()
        {
            DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .make();

            final Class<?> loadedClass = dynamicType
                .load(ClassValueStress.class.getClassLoader())
                .getLoaded();

            return Array.newInstance(loadedClass, 1).getClass();
        }
    }

    @SuppressWarnings({"AssertWithSideEffects", "ConstantConditions"})
    static void needEnabledAsserts()
    {
        boolean enabled = false;
        assert enabled = true;
        if (!enabled)
            throw new AssertionError("assert not enabled");
    }

//        IntStream.range(0, 10)
//            .forEach(i ->
//            {
//                DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
//                    .subclass(Object.class)
//                    .make();
//
//                final Class<?> loadedClass = dynamicType
//                    .load(ClassValueStress.class.getClassLoader())
//                    .getLoaded();
//
//                // System.out.println(loadedClass);
//                final var array = Array.newInstance(loadedClass, 1);
//
//                final var mh = MethodHandles.arrayElementGetter(array.getClass());
//                // System.out.println(mh);
//                assert mh != null;
//            });

    private static void barrierAwait(CyclicBarrier barrier)
    {
        try
        {
            barrier.await();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        catch (BrokenBarrierException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static MethodHandle futureGet(Future<MethodHandle> future)
    {
        try
        {
            return future.get();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            return null;
        }
        catch (ExecutionException e)
        {
            throw new RuntimeException(e);
        }
    }
}
