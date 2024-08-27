import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class NonReduction
{
    static final int RANGE = 16*1024;
    static final int ITER = 100_000;

    static void init(long[] a, long[] b) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        for (int j = 0; j < RANGE; j++)
        {
            a[j] = rand.nextLong();
            b[j] = rand.nextLong();
        }
    }

    static long[] test(long[] a, long[] b, long[] result)
    {
        for (int i = 0; i < RANGE; i++)
        {
            result[i] = Math.max(a[i], b[i]);
        }
        return result;
    }

    public static void main(String[] args)
    {
        long[] a = new long[RANGE];
        long[] b = new long[RANGE];
        long[] result = new long[RANGE];
        init(a, b);
        long[] gold = test(a, b, result);
        for (int i = 0; i < ITER; i++)
        {
            result = test(a, b, result);
            blackhole(result);
        }
    }

    static void blackhole(Object obj)
    {
        if (obj.hashCode() == System.nanoTime())
        {
            System.out.println(obj);
        }
    }
}
