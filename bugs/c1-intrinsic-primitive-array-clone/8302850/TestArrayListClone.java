import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestArrayListClone
{
    public static void main(String[] args)
    {
        final int size = 10;
        final ArrayList<Integer> ints = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
        {
            ints.add(ThreadLocalRandom.current().nextInt());
        }

        for (int i = 0; i < 1_000; i++)
        {
            ArrayList<Integer> result = test(ints);
            System.out.println(result);
            blackhole(result);
        }
    }

    static ArrayList<Integer> test(ArrayList<Integer> ints)
    {
        return (ArrayList<Integer>) ints.clone();
    }

    static void blackhole(Object obj)
    {
        if (obj.hashCode() == System.nanoTime())
        {
            System.out.println(obj);
        }
    }
}
