import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

class TestArrayCloneOOME
{
    static int[][] leak;

    public static void main(String[] args)
    {
        final int size = 10_000;
        System.out.println("Size is: " + size);
        final int[] ints = new int[size];
        for (int i = 0; i < ints.length; i++)
        {
            ints[i] = ThreadLocalRandom.current().nextInt();
        }

        System.out.println("Clone...");
        int cloneCount = 1_000;
        leak = new int[cloneCount][];
        for (int i = 0; i < cloneCount; i++)
        {
            int[] result = test(ints);
            // System.out.println(Arrays.toString(result));
            leak[i] = result;
            blackhole(result);
        }
    }

    static int[] test(int[] ints)
    {
        return ints.clone();
    }

    static void blackhole(Object obj)
    {
        if (obj.hashCode() == System.nanoTime())
        {
            System.out.println(obj);
        }
    }
}