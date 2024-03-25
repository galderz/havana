class Test
{
    public static void main(String[] args)
    {
        float value = 5.6f;
        for (int i = 0; i < 10_000; i++)
        {
            blackhole(
                test(value)
            );
        }
    }

    static float test(float a)
    {
        return Math.max(a, a);
    }

    static void blackhole(Object obj)
    {
        if (obj.hashCode() == System.nanoTime())
        {
            System.out.println(obj);
        }
    }
}