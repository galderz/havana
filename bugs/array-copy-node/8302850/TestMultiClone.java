import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class TestMultiClone
{
    public static void main(String[] args)
    {
        final int size = 10;
        final int[] ints = new int[size];
        final ArrayList<Integer> lints = new ArrayList<>(size);
        for (int i = 0; i < ints.length; i++)
        {
            final int value = ThreadLocalRandom.current().nextInt();
            ints[i] = value;
            lints.add(value);
        }

        for (int i = 0; i < 1_000; i++)
        {
            try
            {
                ArrayList<Integer> result1 = test1(lints);
                System.out.println(result1);
                blackhole(result1);

                int[] result2 = test2(ints);
                System.out.println(Arrays.toString(result2));
                blackhole(result2);
            }
            catch (Exception ex)
            {
                throw new AssertionError(ex);
            }
        }
    }

    static ArrayList<Integer> test1(ArrayList lints)
    {
        return (ArrayList<Integer>) lints.clone();
    }

    static int[] test2(int[] ints)
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