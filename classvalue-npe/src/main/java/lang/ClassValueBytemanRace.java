package lang;

import org.junit.Test;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

public class ClassValueRace
{
    @Test
    public void test() throws Throwable
    {
        final var objectArrayGetter = new ObjectArrayGetter();
        objectArrayGetter.start();
        objectArrayGetter.join();

        final var slowIntArrayGetter = new SlowIntArrayGetter();
        slowIntArrayGetter.start();

        final var fastIntArrayGetter = new FastIntArrayGetter();
        fastIntArrayGetter.start();

        fastIntArrayGetter.join();
        slowIntArrayGetter.join();

        objectArrayGetter.checkUncaught();
        fastIntArrayGetter.checkUncaught();
        slowIntArrayGetter.checkUncaught();
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

        public UncaughtThread(String name)
        {
            super(name);
            uncaughtHandler = new UncaughtHandler();
            setUncaughtExceptionHandler(uncaughtHandler);
        }

        void checkUncaught() throws Throwable
        {
            if (uncaughtHandler.throwable != null)
            {
                throw uncaughtHandler.throwable;
            }
        }
    }

    static final class FastIntArrayGetter extends UncaughtThread
    {
        public FastIntArrayGetter()
        {
            super("fast-thread");
        }

        @Override
        public void run()
        {
            ClassValueRace.sleepShort();
            log(MethodHandles.arrayElementGetter(int[].class));
        }
    }

    static final class SlowIntArrayGetter extends UncaughtThread
    {
        public SlowIntArrayGetter()
        {
            super("slow-thread");
        }

        @Override
        public void run()
        {
            log(MethodHandles.arrayElementGetter(int[].class));
        }
    }

    static final class ObjectArrayGetter extends UncaughtThread
    {
        public ObjectArrayGetter()
        {
            super("object-thread");
        }

        @Override
        public void run()
        {
            ClassValueRace.sleepShort();
            log(MethodHandles.arrayElementGetter(Object[].class));
        }
    }

    static void log(Object... args)
    {
        final var threadName = Thread.currentThread().getName();
        final var msg = String.format("%s", args);
        System.out.printf("[%s] %s%n", threadName, msg);
    }

    static void sleepShort()
    {
        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}
