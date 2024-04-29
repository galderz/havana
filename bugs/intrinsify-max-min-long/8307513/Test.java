import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class Test
{
    static final int RANGE = 1024;
    static final int ITER = 10_000;

    static void init(int[] data)
    {
        for (int i = 0; i < RANGE; i++)
        {
            data[i] = i + 1;
        }
    }

    static int test(int[] data, int sum)
    {
        for (int i = 0; i < RANGE; i++)
        {
            sum += 11 * data[i];
        }
        return sum;
    }

    public static void main(String[] args)
    {
        int[] data = new int[RANGE];
        init(data);
        for (int i = 0; i < ITER; i++)
        {
            test(data, i);
        }
    }
}