package concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CloneArrayVisibility
{
    static final int ARRAY_SIZE = 100_000;
    static int[] src;

    static long sum;

    static
    {
//        src = new int[1];
//        src[0] = -1;

        src = new int[ARRAY_SIZE];
        for (int i = 0; i < src.length; i++)
        {
            src[i] = i;
            sum = sum + i;
        }
    }

    int[] copy;

    public static void main(String[] args) throws Exception
    {
        Asserts.needEnabledAsserts();

        try (final ExecutorService exec = Executors.newCachedThreadPool())
        {
            final long start = System.nanoTime();
            final long end = start + TimeUnit.SECONDS.toNanos(10);
            int iterations = 0;

            while (System.nanoTime() < end)
            {
                final CloneArrayVisibility shared = new CloneArrayVisibility();
                final CyclicBarrier barrier = new CyclicBarrier(3);
                final Future<Result> resultFuture = exec.submit(new Reader(shared, barrier));
                exec.submit(new Cloner(shared, barrier));
                // System.out.println("Wait for threads to be ready");
                barrier.await();
                // System.out.println("Wait for threads to finish");
                barrier.await();
                // System.out.println("Threads finished");
                final Result result = resultFuture.get();
                // final Void unused = clonerResult.get();
                // System.out.println("Validate result");
                assert result.typeEquals : "type not equals";
                assert result.arrayLength == 0 || result.arrayLength == ARRAY_SIZE : "array length not equals";
                assert result.sum == -1 || result.sum == sum : "unexpected array content";

                iterations++;
                if (iterations % 1000 == 0)
                {
                    System.out.println(iterations + " iterations");
                }
            }
        }
    }

    static final class Reader implements Callable<Result>
    {
        final CloneArrayVisibility shared;
        final CyclicBarrier barrier;

        Reader(CloneArrayVisibility result, CyclicBarrier barrier)
        {
            this.shared = result;
            this.barrier = barrier;
        }

        @Override
        public Result call() throws Exception
        {
            barrier.await();
            try
            {
                int[] t = shared.copy;
                if (t != null)
                {
                    return new Result((t.getClass() == int[].class), t.length, sum(t));
                }

                return new Result(true, 0, -1);
            }
            finally
            {
                barrier.await();
            }
        }
    }

    static long sum(int[] t) {
        long sum = 0;

        for (int i : t)
        {
            sum = sum + i;
        }

        return sum;
    }

    static final class Cloner implements Callable<Void>
    {
        final CloneArrayVisibility shared;
        final CyclicBarrier barrier;

        Cloner(CloneArrayVisibility result, CyclicBarrier barrier)
        {
            this.shared = result;
            this.barrier = barrier;
        }

        @Override
        public Void call() throws Exception
        {
            barrier.await();
            try
            {
                shared.copy = src.clone();
                return null;
            }
            finally
            {
                barrier.await();
            }
        }
    }

    record Result(boolean typeEquals, int arrayLength, long sum) {}
}







