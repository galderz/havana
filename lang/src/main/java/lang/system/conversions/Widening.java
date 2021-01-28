package lang.system.conversions;

import static java.lang.System.out;

public class Widening
{
    public static void main()
    {
        {
            putchar('w');
            putchar('.');
            putchar('b');
            putchar('y');
            putchar('t');
            putchar('e');
            putchar('\t');
            putchar('\t');

            byte low = Byte.MIN_VALUE + 1;
            byte high = Byte.MAX_VALUE - 1;
            putchar(low - low == 0 ? '.' : 'F');
            putchar(high - high == 0 ? '.' : 'F');
            putchar(low - (byte)((short) low) == 0 ? '.' : 'F');
            putchar(high - (byte)((short) high) == 0 ? '.' : 'F');
            putchar(low - (byte)((int) low) == 0 ? '.' : 'F');
            putchar(high - (byte)((int) high) == 0 ? '.' : 'F');
            putchar(low - (byte)((long) low) == 0 ? '.' : 'F');
            putchar(high - (byte)((long) high) == 0 ? '.' : 'F');
            putchar(low - (byte)((float) low) == 0 ? '.' : 'F');
            putchar(high - (byte)((float) high) == 0 ? '.' : 'F');
            putchar(low - (byte)((double) low) == 0 ? '.' : 'F');
            putchar(high - (byte)((double) high) == 0 ? '.' : 'F');
            low = Byte.MIN_VALUE;
            high = Byte.MAX_VALUE;
            putchar(low - low == 0 ? '.' : 'F');
            putchar(high - high == 0 ? '.' : 'F');
            putchar(low - (byte)((short) low) == 0 ? '.' : 'F');
            putchar(high - (byte)((short) high) == 0 ? '.' : 'F');
            putchar(low - (byte)((int) low) == 0 ? '.' : 'F');
            putchar(high - (byte)((int) high) == 0 ? '.' : 'F');
            putchar(low - (byte)((long) low) == 0 ? '.' : 'F');
            putchar(high - (byte)((long) high) == 0 ? '.' : 'F');
            putchar(low - (byte)((float) low) == 0 ? '.' : 'F');
            putchar(high - (byte)((float) high) == 0 ? '.' : 'F');
            putchar(low - (byte)((double) low) == 0 ? '.' : 'F');
            putchar(high - (byte)((double) high) == 0 ? '.' : 'F');
            putchar('\n');
        }

        {
            putchar('w');
            putchar('.');
            putchar('s');
            putchar('h');
            putchar('o');
            putchar('r');
            putchar('t');
            putchar('\t');
            putchar('\t');

            short low = Short.MIN_VALUE + 1;
            short high = Short.MAX_VALUE - 1;
            putchar(low - low == 0 ? '.' : 'F');
            putchar(high - high == 0 ? '.' : 'F');
            putchar(low - (short)((int) low) == 0 ? '.' : 'F');
            putchar(high - (short)((int) high) == 0 ? '.' : 'F');
            putchar(low - (short)((long) low) == 0 ? '.' : 'F');
            putchar(high - (short)((long) high) == 0 ? '.' : 'F');
            putchar(low - (short)((float) low) == 0 ? '.' : 'F');
            putchar(high - (short)((float) high) == 0 ? '.' : 'F');
            putchar(low - (short)((double) low) == 0 ? '.' : 'F');
            putchar(high - (short)((double) high) == 0 ? '.' : 'F');
            low = Short.MIN_VALUE;
            high = Short.MAX_VALUE;
            putchar(low - low == 0 ? '.' : 'F');
            putchar(high - high == 0 ? '.' : 'F');
            putchar(low - (short)((int) low) == 0 ? '.' : 'F');
            putchar(high - (short)((int) high) == 0 ? '.' : 'F');
            putchar(low - (short)((long) low) == 0 ? '.' : 'F');
            putchar(high - (short)((long) high) == 0 ? '.' : 'F');
            putchar(low - (short)((float) low) == 0 ? '.' : 'F');
            putchar(high - (short)((float) high) == 0 ? '.' : 'F');
            putchar(low - (short)((double) low) == 0 ? '.' : 'F');
            putchar(high - (short)((double) high) == 0 ? '.' : 'F');
            putchar('\n');
        }

        {
            putchar('w');
            putchar('.');
            putchar('c');
            putchar('h');
            putchar('a');
            putchar('r');
            putchar('\t');
            putchar('\t');

            char low = Character.MIN_VALUE + 1;
            char high = Character.MAX_VALUE - 1;
            putchar(low - low == 0 ? '.' : 'F');
            putchar(high - high == 0 ? '.' : 'F');
            putchar(low - (char)((int) low) == 0 ? '.' : 'F');
            putchar(high - (char)((int) high) == 0 ? '.' : 'F');
            putchar(low - (char)((long) low) == 0 ? '.' : 'F');
            putchar(high - (char)((long) high) == 0 ? '.' : 'F');
            putchar(low - (char)((float) low) == 0 ? '.' : 'F');
            putchar(high - (char)((float) high) == 0 ? '.' : 'F');
            putchar(low - (char)((double) low) == 0 ? '.' : 'F');
            putchar(high - (char)((double) high) == 0 ? '.' : 'F');
            low = Character.MIN_VALUE;
            high = Character.MAX_VALUE;
            putchar(low - low == 0 ? '.' : 'F');
            putchar(high - high == 0 ? '.' : 'F');
            putchar(low - (char)((int) low) == 0 ? '.' : 'F');
            putchar(high - (char)((int) high) == 0 ? '.' : 'F');
            putchar(low - (char)((long) low) == 0 ? '.' : 'F');
            putchar(high - (char)((long) high) == 0 ? '.' : 'F');
            putchar(low - (char)((float) low) == 0 ? '.' : 'F');
            putchar(high - (char)((float) high) == 0 ? '.' : 'F');
            putchar(low - (char)((double) low) == 0 ? '.' : 'F');
            putchar(high - (char)((double) high) == 0 ? '.' : 'F');
            putchar('\n');
        }

        {
            putchar('w');
            putchar('.');
            putchar('i');
            putchar('n');
            putchar('t');
            putchar(' ');
            putchar('\t');
            putchar('\t');

            int low = Integer.MIN_VALUE + 1;
            int high = Integer.MAX_VALUE - 1;
            putchar(low - low == 0 ? '.' : 'F');
            putchar(high - high == 0 ? '.' : 'F');
            putchar(low - (int)((long) low) == 0 ? '.' : 'F');
            putchar(high - (int)((long) high) == 0 ? '.' : 'F');
            putchar(low - (int)((float) low) == 1 ? '.' : 'F'); // loss of precision
            putchar(high - (int)((float) high) == -1 ? '.' : 'F'); // loss of precision
            putchar(low - (int)((double) low) == 0 ? '.' : 'F');
            putchar(high - (int)((double) high) == 0 ? '.' : 'F');
            low = Integer.MIN_VALUE;
            high = Integer.MAX_VALUE;
            putchar(low - low == 0 ? '.' : 'F');
            putchar(high - high == 0 ? '.' : 'F');
            putchar(low - (int)((long) low) == 0 ? '.' : 'F');
            putchar(high - (int)((long) high) == 0 ? '.' : 'F');
            putchar(low - (int)((float) low) == 0 ? '.' : 'F');
            putchar(high - (int)((float) high) == 0 ? '.' : 'F');
            putchar(low - (int)((double) low) == 0 ? '.' : 'F');
            putchar(high - (int)((double) high) == 0 ? '.' : 'F');
            putchar('\n');
        }

        {
            putchar('w');
            putchar('.');
            putchar('l');
            putchar('o');
            putchar('n');
            putchar('g');
            putchar('\t');
            putchar('\t');

            long low = Long.MIN_VALUE + 1;
            long high = Long.MAX_VALUE - 1;
            putchar(low - low == 0 ? '.' : 'F');
            putchar(high - high == 0 ? '.' : 'F');
            putchar(low - (long)((float) low) == 1 ? '.' : 'F'); // loss of precision
            putchar(high - (long)((float) high) == -1 ? '.' : 'F'); // loss of precision
            putchar(low - (long)((double) low) == 1 ? '.' : 'F'); // loss of precision
            putchar(high - (long)((double) high) == -1 ? '.' : 'F'); // loss of precision
            low = Long.MIN_VALUE;
            high = Long.MAX_VALUE;
            putchar(low - low == 0 ? '.' : 'F');
            putchar(high - high == 0 ? '.' : 'F');
            putchar(low - (long)((float) low) == 0 ? '.' : 'F');
            putchar(high - (long)((float) high) == 0 ? '.' : 'F');
            putchar(low - (long)((double) low) == 0 ? '.' : 'F');
            putchar(high - (long)((double) high) == 0 ? '.' : 'F');
            putchar('\n');
        }

        {
            putchar('w');
            putchar('.');
            putchar('f');
            putchar('l');
            putchar('o');
            putchar('a');
            putchar('t');
            putchar('\t');
            putchar('\t');

            float low = Float.MIN_VALUE + 1;
            float high = Float.MAX_VALUE - 1;
            putchar(low - low == 0 ? '.' : 'F');
            putchar(high - high == 0 ? '.' : 'F');
            putchar(low - (float)((double) low) == 0 ? '.' : 'F');
            putchar(high - (float)((double) high) == 0 ? '.' : 'F');
            low = Float.MIN_VALUE;
            high = Float.MAX_VALUE;
            putchar(low - low == 0 ? '.' : 'F');
            putchar(high - high == 0 ? '.' : 'F');
            putchar(low - (float)((double) low) == 0 ? '.' : 'F');
            putchar(high - (float)((double) high) == 0 ? '.' : 'F');
            low = Float.NEGATIVE_INFINITY;
            high = Float.POSITIVE_INFINITY;
            putchar(low - low != low - low ? '.' : 'F');
            putchar(high - high != high - high ? '.' : 'F');
            putchar(low - (float)((double) low) != low - (float)((double) low) ? '.' : 'F');
            putchar(high - (float)((double) high) != high - (float)((double) high) ? '.' : 'F');
            low = Float.NaN;
            high = Float.NaN;
            putchar(low - low != low - low ? '.' : 'F');
            putchar(high - high != high - high ? '.' : 'F');
            putchar(low - (float)((double) low) != low - (float)((double) low) ? '.' : 'F');
            putchar(high - (float)((double) high) != high - (float)((double) high) ? '.' : 'F');

            putchar('\n');
        }
    }

    private static void putchar(char c)
    {
        out.print(c);
    }
}
