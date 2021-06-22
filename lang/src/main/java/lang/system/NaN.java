package lang.system;

import static java.lang.System.out;

public class NaN
{
    public static void main(String[] args)
    {
        float f = Float.NEGATIVE_INFINITY;
        putchar(f - f != f - f ? '.' : 'F');
        putchar('\n');

        out.println(Long.toHexString(Double.doubleToRawLongBits(Float.NEGATIVE_INFINITY)));
        out.println(Long.toBinaryString(Double.doubleToRawLongBits(Float.NEGATIVE_INFINITY)));
        out.println(Long.toHexString(Double.doubleToRawLongBits(Double.NaN)));
        out.println(Long.toBinaryString(Double.doubleToRawLongBits(Double.NaN)));
    }

    private static void putchar(char c)
    {
        out.print(c);
    }
}
