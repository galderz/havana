class Test
{
    public static void main(String[] args)
    {
        float a = 5.6f;
        float b = 5.6f;
        for (int i = 0; i < 10_000; i++)
        {
            blackhole(
                test(a, b)
            );
        }
    }

    static float test(float a, float b)
    {
        return Math.max(a, b);
    }

    static void blackhole(Object obj)
    {
        if (obj.hashCode() == System.nanoTime())
        {
            System.out.println(obj);
        }
    }
}