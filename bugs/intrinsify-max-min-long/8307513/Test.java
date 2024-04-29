import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class Test
{
    static final int RANGE = 16*1024;

    public static void main(String[] args)
    {
        int iter_warmup = 2_000;
        int iter_perf = 100_000;

        long[] aLong = new long[RANGE];
        long[] bLong = new long[RANGE];
        long[] cLong = new long[RANGE];

        long start, stop;

        long startLongMax = init(aLong, bLong, cLong);
        long goldLongMax = testLongMax(aLong, bLong, cLong, startLongMax);
        for (int j = 0; j < iter_warmup; j++)
        {
            long total = testLongMax(aLong, bLong, cLong, startLongMax);
            verify("long max", total, goldLongMax);
        }
        start = System.currentTimeMillis();
        for (int j = 0; j < iter_perf; j++)
        {
            testLongMax(aLong, bLong, cLong, startLongMax);
        }
        stop = System.currentTimeMillis();
        System.out.println("long max   " + (stop - start));
    }

    static long testLongMax(long[] a, long[] b, long[] c, long total)
    {
        for (int i = 0; i < RANGE; i++)
        {
            long v = (a[i] * b[i]) + (a[i] * c[i]) + (b[i] * c[i]);
            total = Math.max(total, v);
        }
        return total;
    }

    static long init(long[] a, long[] b, long[] c)
    {
        final ThreadLocalRandom rand = ThreadLocalRandom.current();
        for (int j = 0; j < RANGE; j++)
        {
            a[j] = rand.nextLong();
            b[j] = rand.nextLong();
            c[j] = rand.nextLong();
        }
        return rand.nextLong();
    }

    static void verify(String context, long total, long gold)
    {
        if (total != gold)
        {
            throw new RuntimeException("Wrong result for " + context + ": " + total + " != " + gold);
        }
    }
}