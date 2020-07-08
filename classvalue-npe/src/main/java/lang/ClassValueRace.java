package lang;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

public class ClassValueRace
{
    public static void main(String[] args) throws Exception
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
    }

    static final class FastIntArrayGetter extends Thread
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

    static final class SlowIntArrayGetter extends Thread
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

    static final class ObjectArrayGetter extends Thread
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
            Thread.sleep(TimeUnit.SECONDS.toMillis(3));
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}
