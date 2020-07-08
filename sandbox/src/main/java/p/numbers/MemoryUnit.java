package p.numbers;

public class MemoryUnit
{
    public static void main(String[] args)
    {
        final long max = 16L * 1024 * 1024 * 1024;
        System.out.println(max);
        final long max2 = 1L << 34;
        System.out.println(max2);
        System.out.println(Long.MAX_VALUE);
    }

}
