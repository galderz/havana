import java.util.concurrent.ThreadLocalRandom;

class TestArrayCopy
{
    public static void main(String[] args)
    {
        final int size = 10;
        final int[] ints = new int[size];
        for (int i = 0; i < ints.length; i++)
        {
            ints[i] = ThreadLocalRandom.current().nextInt();
        }

        for (int i = 0; i < 1_000; i++)
        {
            int[] copy = new int[ints.length];
            int[] result = test(ints, copy);
            blackhole(result);
        }
    }

    static int[] test(int[] ints, int[] copy)
    {
        System.arraycopy(ints, 0, copy, 0, ints.length);
        return copy;
    }

    static void blackhole(Object obj)
    {
        if (obj.hashCode() == System.nanoTime())
        {
            System.out.println(obj);
        }
    }
}