import java.util.Arrays;
import java.util.UUID;

class TestUUIDArrayClone
{
    public static void main(String[] args)
    {
        final int size = 10;
        final UUID[] uuids = new UUID[size];
        for (int i = 0; i < uuids.length; i++)
        {
            uuids[i] = UUID.randomUUID();
        }

        for (int i = 0; i < 1_000; i++)
        {
            UUID[] result = test(uuids);
            System.out.println(Arrays.toString(result));
            blackhole(result);
        }
    }

    static UUID[] test(UUID[] ints)
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