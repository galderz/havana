import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class LongAdd
{
    static final int RANGE = 16*1024;
    static final int ITER = 100_000;

    static long init(long[] a, long[] b, long[] c)
    {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        for (int j = 0; j < RANGE; j++)
        {
            a[j] = rand.nextLong();
            b[j] = rand.nextLong();
            c[j] = rand.nextLong();
        }
        return rand.nextLong();
    }

    static long test(long[] a, long[] b, long[] c, long total)
    {
        for (int i = 0; i < RANGE; i++)
        {
            long v = (a[i] * b[i]) + (a[i] * c[i]) + (b[i] * c[i]);
            total += v;
        }
        return total;
    }

    public static void main(String[] args)
    {
        long[] a = new long[RANGE];
        long[] b = new long[RANGE];
        long[] c = new long[RANGE];
        long start = init(a, b, c);
        long gold = test(a, b, c, start);
        for (int j = 0; j < ITER; j++)
        {
            long total = test(a, b, c, start);
            verify("long add", total, gold);
        }
    }

    static void verify(String context, long total, long gold)
    {
        if (total != gold)
        {
            throw new RuntimeException("Wrong result for " + context + ": " + total + " != " + gold);
        }
    }
}
