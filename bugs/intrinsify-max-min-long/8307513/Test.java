import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class Test
{
    static final int RANGE = 1024;
    static final int ITER = 10_000;

    static void init(long[] data)
    {
        for (int i = 0; i < RANGE; i++)
        {
            data[i] = i + 1;
        }
    }

    static long test(long[] data, long sum)
    {
        for (int i = 0; i < RANGE; i++)
        {
            final long v = 11 * data[i];
            sum = Math.max(sum, v);
        }
        return sum;
    }

    public static void main(String[] args)
    {
        long[] data = new long[RANGE];
        init(data);
        for (long i = 0; i < ITER; i++)
        {
            test(data, i);
        }
    }
}