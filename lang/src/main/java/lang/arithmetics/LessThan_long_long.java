package lang.arithmetics;

public class LessThan_long_long
{
    public static void main(String[] args)
    {
        putchar(true == lessThan_long_long(1l, 2l) ? '.' : 'F');
        putchar(false == lessThan_long_long(2l, 1l) ? '.' : 'F');
    }

    static boolean lessThan_long_long(long v1, long v2) {
        return v1 < v2;
    }

    private static void putchar(char c)
    {
        System.out.print(c);
    }
}
