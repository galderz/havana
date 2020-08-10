package lang;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Array;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * To get logging and verify it's exercising the right thing:
 * -ea -Dnum.iterations=5 -Dnum.threads=5 -javaagent:/opt/byteman/lib/byteman.jar=boot:/opt/byteman/lib/byteman.jar,script:/Users/g/1/havana/classvalue-npe/src/main/resources/logging.btm -Dorg.jboss.byteman.transform.all=true
 *
 * Otherwise, just run it with:
 * -ea
 */
public class ClassValueStress
{
    static final int NUM_ITERATIONS = Integer.getInteger("num.iterations", Integer.MAX_VALUE - 8);
    static final int NUM_THREADS = Integer.getInteger("num.threads", Runtime.getRuntime().availableProcessors() - 1);

    static final int BATCH_SIZE = Integer.getInteger("batch.size", 100_000);
    static final Class<?>[] types = new Class<?>[BATCH_SIZE];

    public static void main(String[] args) throws Exception
    {
        needEnabledAsserts();
        System.out.printf("Number of iterations: %d%n", NUM_ITERATIONS);
        System.out.printf("Number of threads: %d%n", NUM_THREADS);
        System.out.printf("JVM arguments: %s%n", getJvmArguments());

        // Get Object[] out of the picture for other threads
        MethodHandles.arrayElementGetter(Object[].class);

        final var executor = Executors.newFixedThreadPool(NUM_THREADS);
        IntStream.range(0, NUM_ITERATIONS).forEach(runIteration(executor));

        executor.shutdown();
    }

    private static void generateDynamicTypes(int size)
    {
        System.out.printf("Generate %d dynamic types%n", size);
        final var start = System.nanoTime();
        IntStream.range(0, size).forEach(addDynamicType());
        System.out.printf(
            "Dynamic types generated in %d seconds %n"
            , TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start)
        );
    }

    private static IntConsumer addDynamicType()
    {
        return i -> types[i] = newDynamicArrayType();
    }

    private static Object getJvmArguments()
    {
        final var runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return runtimeMXBean.getInputArguments();
    }

    private static IntConsumer runIteration(ExecutorService executor)
    {
        return i ->
        {
            if (i < 1000 || i % 1000 == 0)
                System.out.printf("Iteration %d%n", i);

            if (i == 0 || i % BATCH_SIZE == 0)
                generateDynamicTypes(BATCH_SIZE);

            final var barrier = new CyclicBarrier(NUM_THREADS + 1);

            int typeIndex = i % BATCH_SIZE;

            final var futures =
                IntStream.range(0, NUM_THREADS)
                    .mapToObj(x -> fireArrayGetter(types[typeIndex], barrier, executor))
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

    private static Future<MethodHandle> fireArrayGetter(Class<?> type, CyclicBarrier barrier, ExecutorService executor)
    {
        return executor.submit(new ArrayGetter(type, barrier));
    }

    private static Class<?> newDynamicArrayType()
    {
        DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
            .subclass(Object.class)
            .make();

        final Class<?> loadedClass = dynamicType
            .load(ClassValueStress.class.getClassLoader())
            .getLoaded();

        return Array.newInstance(loadedClass, 1).getClass();
    }

    static class ArrayGetter implements Callable<MethodHandle>
    {
        final Class<?> type;
        final CyclicBarrier barrier;

        ArrayGetter(Class<?> type, CyclicBarrier barrier) {
            this.type = type;
            this.barrier = barrier;
        }

        @Override
        public MethodHandle call() throws Exception
        {
            barrier.await();
            return MethodHandles.arrayElementGetter(type);
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
