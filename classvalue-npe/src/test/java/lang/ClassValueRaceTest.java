package lang;

import org.junit.Test;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class ClassValueRaceTest
{
    static final int NUM_THREADS = Integer.getInteger("num.threads", 2);

    @Test
    public void test() throws Throwable
    {
        System.out.printf("Number of threads: %d", NUM_THREADS);

        final var objectArrayGetter = new ObjectArrayGetter();
        objectArrayGetter.start();
        objectArrayGetter.join();

        final var barrier = new CyclicBarrier(NUM_THREADS + 1);
        final var getters = new ArrayList<IntArrayGetter>(NUM_THREADS);
        for (int i = 0; i < NUM_THREADS; i++)
        {
            final var getter = new IntArrayGetter(barrier);
            getters.add(getter);
            getter.start();
        }

        barrier.await();

        for (int i = 0; i < NUM_THREADS; i++)
        {
            getters.get(i).join();
        }

        for (int i = 0; i < NUM_THREADS; i++)
        {
            getters.get(i).checkUncaught();
        }
    }

    static final class UncaughtHandler implements Thread.UncaughtExceptionHandler
    {
        public volatile Throwable throwable;

        @Override
        public void uncaughtException(Thread th, Throwable ex) {
            System.err.print("Exception in thread \"" + th.getName() + "\" ");
            ex.printStackTrace(System.err);
            this.throwable = ex;
        }
    }

    static abstract class UncaughtThread extends Thread
    {
        final UncaughtHandler uncaughtHandler;
        final CyclicBarrier barrier;

        public UncaughtThread(CyclicBarrier barrier, String name)
        {
            super(name);
            this.barrier = barrier;
            this.uncaughtHandler = new UncaughtHandler();
            setUncaughtExceptionHandler(this.uncaughtHandler);
        }

        void checkUncaught() throws Throwable
        {
            if (uncaughtHandler.throwable != null)
            {
                throw uncaughtHandler.throwable;
            }
        }
    }

    static final class IntArrayGetter extends UncaughtThread
    {
        public IntArrayGetter(CyclicBarrier barrier)
        {
            super(barrier, "slow-thread");
        }

        @Override
        public void run()
        {
            try
            {
                barrier.await();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            log(MethodHandles.arrayElementGetter(int[].class));
        }
    }

    static final class ObjectArrayGetter extends UncaughtThread
    {
        public ObjectArrayGetter()
        {
            super(null, "object-thread");
        }

        @Override
        public void run()
        {
            log(MethodHandles.arrayElementGetter(Object[].class));
        }
    }

    static void log(Object... args)
    {
        final var threadName = Thread.currentThread().getName();
        final var msg = String.format("%s", args);
        if (msg.hashCode() % 41 == 0)
            System.out.printf("[%s] %s%n", threadName, msg);
    }

}
