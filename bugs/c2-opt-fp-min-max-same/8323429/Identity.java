public class Identity
{
    public static void main(String[] args)
    {
        for (int i = 0; i < 10_000; i++)
        {
            blackhole(
                test()
            );
        }
    }

    static float test()
    {
        float x = 5.6f;
        float y = 3.2f;
        return (x - y) + y;
    }

    static void blackhole(Object obj)
    {
        if (obj.hashCode() == System.nanoTime())
        {
            System.out.println(obj);
        }
    }
}
