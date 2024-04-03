public class Constant
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
        float v = 5.6f;
        return Math.max(v, v);
    }

    static void blackhole(Object obj)
    {
        if (obj.hashCode() == System.nanoTime())
        {
            System.out.println(obj);
        }
    }
}
