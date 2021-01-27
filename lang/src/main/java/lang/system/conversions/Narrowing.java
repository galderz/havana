package lang.system.conversions;

import static java.lang.System.out;

public class Narrowing
{
    public static void main()
    {
        narrowShort();
    }

    private static void narrowShort()
    {
        putchar('n');
        putchar('.');
        putchar('s');
        putchar('h');
        putchar('o');
        putchar('r');
        putchar('t');
        putchar('\t');

        short low = Short.MIN_VALUE + 1;
        short high = Short.MAX_VALUE - 1;
        putchar(low - low == 0 ? '.' : 'F');
        putchar(high - high == 0 ? '.' : 'F');
        putchar(low - ((byte) low) == Short.MIN_VALUE ? '.' : 'F');
        putchar(high - ((byte) high) == Short.MAX_VALUE + 1 ? '.' : 'F');
        putchar(low - ((char) low) == Short.MIN_VALUE << 1 ? '.' : 'F');
        putchar(high - ((char) high) == 0 ? '.' : 'F');
        low = Short.MIN_VALUE;
        high = Short.MAX_VALUE;
        putchar(low - low == 0 ? '.' : 'F');
        putchar(high - high == 0 ? '.' : 'F');
        putchar(low - ((byte) low) == Short.MIN_VALUE ? '.' : 'F');
        putchar(high - ((byte) high) == Short.MAX_VALUE + 1 ? '.' : 'F');
        putchar(low - ((char) low) == Short.MIN_VALUE << 1 ? '.' : 'F');
        putchar(high - ((char) high) == 0 ? '.' : 'F');
        putchar('\n');
    }

    private static void putchar(char c)
    {
        out.print(c);
    }
}
