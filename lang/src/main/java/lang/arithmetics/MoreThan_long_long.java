package lang.arithmetics;

public class MoreThan_long_long
{
    public static void main(String[] args)
    {
        putchar(false == moreThan_long_long(1l, 2l) ? '.' : 'F');
        putchar(true == moreThan_long_long(2l, 1l) ? '.' : 'F');
    }

    static boolean moreThan_long_long(long v1, long v2) {
        return v1 > v2;
    }

    private static void putchar(char c)
    {
        System.out.print(c);
    }
}
