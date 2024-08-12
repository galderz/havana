import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class IntMax
{
    static final int RANGE = 16*1024;
    static final int ITER = 100_000;

    static int init(int[] a, int[] b, int[] c) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        for (int j = 0; j < RANGE; j++)
        {
            a[j] = rand.nextInt();
            b[j] = rand.nextInt();
            c[j] = rand.nextInt();
        }
        return rand.nextInt();
    }

    static int test(int[] a, int[] b, int[] c, int total)
    {
        for (int i = 0; i < RANGE; i++)
        {
            int v = (a[i] * b[i]) + (a[i] * c[i]) + (b[i] * c[i]);
            total = Math.max(total, v);
        }
        return total;
    }

    public static void main(String[] args)
    {
        int[] a = new int[RANGE];
        int[] b = new int[RANGE];
        int[] c = new int[RANGE];
        int start = init(a, b, c);
        int gold = test(a, b, c, start);
        for (int i = 0; i < ITER; i++)
        {
            int total = test(a, b, c, start);
            verify("int max", total, gold);
        }
    }

    static void verify(String context, int total, int gold)
    {
        if (total != gold) {
            throw new RuntimeException("Wrong result for " + context + ": " + total + " != " + gold);
        }
    }
}