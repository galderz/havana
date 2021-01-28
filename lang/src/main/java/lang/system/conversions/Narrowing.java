package lang.system.conversions;

import static java.lang.System.out;

public class Narrowing
{
    public static void main()
    {
        narrowShort();
        narrowChar();
        narrowInt();
        narrowLong();
        narrowFloat();
        narrowDouble();
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

    private static void narrowChar()
    {
        putchar('n');
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
        putchar(low - ((byte) low) == 0 ? '.' : 'F');
        putchar(high - ((byte) high) == (Character.MAX_VALUE + 1) ? '.' : 'F'); // 0x0001_0000
        putchar(low - ((short) low) == 0 ? '.' : 'F');
        putchar(high - ((short) high) == (Character.MAX_VALUE + 1) ? '.' : 'F'); // 0x0001_0000
        low = Character.MIN_VALUE;
        high = Character.MAX_VALUE;
        putchar(low - low == 0 ? '.' : 'F');
        putchar(high - high == 0 ? '.' : 'F');
        putchar(low - ((byte) low) == 0 ? '.' : 'F');
        putchar(high - ((byte) high) == (Character.MAX_VALUE + 1) ? '.' : 'F'); // 0x0001_0000
        putchar(low - ((short) low) == 0 ? '.' : 'F');
        putchar(high - ((short) high) == (Character.MAX_VALUE + 1) ? '.' : 'F'); // 0x0001_0000

        putchar('\n');
    }

    private static void narrowInt()
    {
        putchar('n');
        putchar('.');
        putchar('i');
        putchar('n');
        putchar('t');
        putchar('\t');
        putchar('\t');

        int low = Integer.MIN_VALUE + 1;
        int high = Integer.MAX_VALUE - 1;
        putchar(low - low == 0 ? '.' : 'F');
        putchar(high - high == 0 ? '.' : 'F');
        putchar(low - ((byte) low) == Integer.MIN_VALUE ? '.' : 'F');
        putchar(high - ((byte) high) == Integer.MIN_VALUE ? '.' : 'F');
        putchar(low - ((short) low) == Integer.MIN_VALUE ? '.' : 'F');
        putchar(high - ((short) high) == Integer.MIN_VALUE ? '.' : 'F');
        putchar(low - ((char) low) == Integer.MIN_VALUE ? '.' : 'F');
        putchar(high - ((char) high) == 0x7FFF_0000 ? '.' : 'F');
        low = Integer.MIN_VALUE;
        high = Integer.MAX_VALUE;
        putchar(low - low == 0 ? '.' : 'F');
        putchar(high - high == 0 ? '.' : 'F');
        putchar(low - ((byte) low) == Integer.MIN_VALUE ? '.' : 'F');
        putchar(high - ((byte) high) == Integer.MIN_VALUE ? '.' : 'F');
        putchar(low - ((short) low) == Integer.MIN_VALUE ? '.' : 'F');
        putchar(high - ((short) high) == Integer.MIN_VALUE ? '.' : 'F');
        putchar(low - ((char) low) == Integer.MIN_VALUE ? '.' : 'F');
        putchar(high - ((char) high) == 0x7FFF_0000 ? '.' : 'F');

        putchar('\n');
    }

    private static void narrowLong()
    {
        putchar('n');
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
        putchar(low - ((byte) low) == Long.MIN_VALUE ? '.' : 'F');
        putchar(high - ((byte) high) == Long.MIN_VALUE ? '.' : 'F');
        putchar(low - ((short) low) == Long.MIN_VALUE ? '.' : 'F');
        putchar(high - ((short) high) == Long.MIN_VALUE ? '.' : 'F');
        putchar(low - ((char) low) == Long.MIN_VALUE ? '.' : 'F');
        putchar(high - ((char) high) == 0x7FFF_FFFF_FFFF_0000L ? '.' : 'F');
        putchar(low - ((int) low) == Long.MIN_VALUE ? '.' : 'F');
        putchar(high - ((int) high) == Long.MIN_VALUE ? '.' : 'F');
        low = Long.MIN_VALUE;
        high = Long.MAX_VALUE;
        putchar(low - low == 0 ? '.' : 'F');
        putchar(high - high == 0 ? '.' : 'F');
        putchar(low - ((byte) low) == Long.MIN_VALUE ? '.' : 'F');
        putchar(high - ((byte) high) == Long.MIN_VALUE ? '.' : 'F');
        putchar(low - ((short) low) == Long.MIN_VALUE ? '.' : 'F');
        putchar(high - ((short) high) == Long.MIN_VALUE ? '.' : 'F');
        putchar(low - ((char) low) == Long.MIN_VALUE ? '.' : 'F');
        putchar(high - ((char) high) == 0x7FFF_FFFF_FFFF_0000L ? '.' : 'F');
        putchar(low - ((int) low) == Long.MIN_VALUE ? '.' : 'F');
        putchar(high - ((int) high) == Long.MIN_VALUE ? '.' : 'F');

        putchar('\n');
    }

    private static void narrowFloat()
    {
        putchar('n');
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
        putchar(low - ((byte) low) == 0 ? '.' : 'F');
        putchar(high - ((byte) high) == Float.MAX_VALUE ? '.' : 'F');
        putchar(low - ((short) low) == 0 ? '.' : 'F');
        putchar(high - ((short) high) == Float.MAX_VALUE ? '.' : 'F');
        putchar(low - ((char) low) == 0 ? '.' : 'F');
        putchar(high - ((char) high) == Float.MAX_VALUE ? '.' : 'F');
        putchar(low - ((int) low) == 0 ? '.' : 'F');
        putchar(high - ((int) high) == Float.MAX_VALUE ? '.' : 'F');
        putchar(low - ((long) low) == 0 ? '.' : 'F');
        putchar(high - ((long) high) == Float.MAX_VALUE ? '.' : 'F');
        low = Float.MIN_VALUE;
        high = Float.MAX_VALUE;
        putchar(low - low == 0 ? '.' : 'F');
        putchar(high - high == 0 ? '.' : 'F');
        putchar(low - ((byte) low) == Float.MIN_VALUE ? '.' : 'F');
        putchar(high - ((byte) high) == Float.MAX_VALUE ? '.' : 'F');
        putchar(low - ((short) low) == Float.MIN_VALUE ? '.' : 'F');
        putchar(high - ((short) high) == Float.MAX_VALUE ? '.' : 'F');
        putchar(low - ((char) low) == Float.MIN_VALUE ? '.' : 'F');
        putchar(high - ((char) high) == Float.MAX_VALUE ? '.' : 'F');
        putchar(low - ((int) low) == Float.MIN_VALUE ? '.' : 'F');
        putchar(high - ((int) high) == Float.MAX_VALUE ? '.' : 'F');
        putchar(low - ((long) low) == Float.MIN_VALUE ? '.' : 'F');
        putchar(high - ((long) high) == Float.MAX_VALUE ? '.' : 'F');
        low = Float.NEGATIVE_INFINITY;
        high = Float.POSITIVE_INFINITY;
        putchar(low - low != low - low ? '.' : 'F');
        putchar(high - high != high - high ? '.' : 'F');
        putchar(low - ((byte) low) == Float.NEGATIVE_INFINITY ? '.' : 'F');
        putchar(high - ((byte) high) == Float.POSITIVE_INFINITY ? '.' : 'F');
        putchar(low - ((short) low) == Float.NEGATIVE_INFINITY ? '.' : 'F');
        putchar(high - ((short) high) == Float.POSITIVE_INFINITY ? '.' : 'F');
        putchar(low - ((char) low) == Float.NEGATIVE_INFINITY ? '.' : 'F');
        putchar(high - ((char) high) == Float.POSITIVE_INFINITY ? '.' : 'F');
        putchar(low - ((int) low) == Float.NEGATIVE_INFINITY ? '.' : 'F');
        putchar(high - ((int) high) == Float.POSITIVE_INFINITY ? '.' : 'F');
        putchar(low - ((long) low) == Float.NEGATIVE_INFINITY ? '.' : 'F');
        putchar(high - ((long) high) == Float.POSITIVE_INFINITY ? '.' : 'F');
        low = Float.NaN;
        high = Float.NaN;
        putchar(low - low != low - low ? '.' : 'F');
        putchar(high - high != high - high ? '.' : 'F');
        putchar(low - ((byte) low) != low - ((byte) low) ? '.' : 'F');
        putchar(high - ((byte) high) != high - ((byte) high) ? '.' : 'F');
        putchar(low - ((short) low) != low - ((short) low) ? '.' : 'F');
        putchar(high - ((short) high) != high - ((short) high) ? '.' : 'F');
        putchar(low - ((char) low) != low - ((char) low) ? '.' : 'F');
        putchar(high - ((char) high) != high - ((char) high) ? '.' : 'F');
        putchar(low - ((int) low) != low - ((int) low) ? '.' : 'F');
        putchar(high - ((int) high) != high - ((int) high) ? '.' : 'F');
        putchar(low - ((long) low) != low - ((long) low) ? '.' : 'F');
        putchar(high - ((long) high) != high - ((long) high) ? '.' : 'F');

        putchar('\n');
    }

    private static void narrowDouble()
    {
        putchar('n');
        putchar('.');
        putchar('d');
        putchar('o');
        putchar('u');
        putchar('b');
        putchar('l');
        putchar('e');
        putchar('\t');

        double low = Double.MIN_VALUE + 1;
        double high = Double.MAX_VALUE - 1;
        putchar(low - low == 0 ? '.' : 'F');
        putchar(high - high == 0 ? '.' : 'F');
        putchar(low - ((byte) low) == 0 ? '.' : 'F');
        putchar(high - ((byte) high) == Double.MAX_VALUE ? '.' : 'F');
        putchar(low - ((short) low) == 0 ? '.' : 'F');
        putchar(high - ((short) high) == Double.MAX_VALUE ? '.' : 'F');
        putchar(low - ((char) low) == 0 ? '.' : 'F');
        putchar(high - ((char) high) == Double.MAX_VALUE ? '.' : 'F');
        putchar(low - ((int) low) == 0 ? '.' : 'F');
        putchar(high - ((int) high) == Double.MAX_VALUE ? '.' : 'F');
        putchar(low - ((long) low) == 0 ? '.' : 'F');
        putchar(high - ((long) high) == Double.MAX_VALUE ? '.' : 'F');
        putchar(low - ((float) low) == 0 ? '.' : 'F');
        putchar(high - ((float) high) == Double.NEGATIVE_INFINITY ? '.' : 'F');
        low = Double.MIN_VALUE;
        high = Double.MAX_VALUE;
        putchar(low - low == 0 ? '.' : 'F');
        putchar(high - high == 0 ? '.' : 'F');
        putchar(low - ((byte) low) == Double.MIN_VALUE ? '.' : 'F');
        putchar(high - ((byte) high) == Double.MAX_VALUE ? '.' : 'F');
        putchar(low - ((short) low) == Double.MIN_VALUE ? '.' : 'F');
        putchar(high - ((short) high) == Double.MAX_VALUE ? '.' : 'F');
        putchar(low - ((char) low) == Double.MIN_VALUE ? '.' : 'F');
        putchar(high - ((char) high) == Double.MAX_VALUE ? '.' : 'F');
        putchar(low - ((int) low) == Double.MIN_VALUE ? '.' : 'F');
        putchar(high - ((int) high) == Double.MAX_VALUE ? '.' : 'F');
        putchar(low - ((long) low) == Double.MIN_VALUE ? '.' : 'F');
        putchar(high - ((long) high) == Double.MAX_VALUE ? '.' : 'F');
        putchar(low - ((float) low) == Double.MIN_VALUE ? '.' : 'F');
        putchar(high - ((float) high) == Double.NEGATIVE_INFINITY ? '.' : 'F');
        low = Double.NEGATIVE_INFINITY;
        high = Double.POSITIVE_INFINITY;
        putchar(low - low != low - low ? '.' : 'F');
        putchar(high - high != low - low ? '.' : 'F');
        putchar(low - ((byte) low) == Double.NEGATIVE_INFINITY ? '.' : 'F');
        putchar(high - ((byte) high) == Double.POSITIVE_INFINITY ? '.' : 'F');
        putchar(low - ((short) low) == Double.NEGATIVE_INFINITY ? '.' : 'F');
        putchar(high - ((short) high) == Double.POSITIVE_INFINITY ? '.' : 'F');
        putchar(low - ((char) low) == Double.NEGATIVE_INFINITY ? '.' : 'F');
        putchar(high - ((char) high) == Double.POSITIVE_INFINITY ? '.' : 'F');
        putchar(low - ((int) low) == Double.NEGATIVE_INFINITY ? '.' : 'F');
        putchar(high - ((int) high) == Double.POSITIVE_INFINITY ? '.' : 'F');
        putchar(low - ((long) low) == Double.NEGATIVE_INFINITY ? '.' : 'F');
        putchar(high - ((long) high) == Double.POSITIVE_INFINITY ? '.' : 'F');
        putchar(low - ((float) low) != low - ((float) low) ? '.' : 'F');
        putchar(high - ((float) high) != high - ((float) high) ? '.' : 'F');
        low = Double.NaN;
        high = Double.NaN;
        putchar(low - low != low - low ? '.' : 'F');
        putchar(high - high != low - low ? '.' : 'F');
        putchar(low - ((byte) low) != low - ((byte) low) ? '.' : 'F');
        putchar(high - ((byte) high) != high - ((byte) high) ? '.' : 'F');
        putchar(low - ((short) low) != low - ((short) low) ? '.' : 'F');
        putchar(high - ((short) high) != high - ((short) high) ? '.' : 'F');
        putchar(low - ((char) low) != low - ((char) low) ? '.' : 'F');
        putchar(high - ((char) high) != high - ((char) high) ? '.' : 'F');
        putchar(low - ((int) low) != low - ((int) low) ? '.' : 'F');
        putchar(high - ((int) high) != high - ((int) high) ? '.' : 'F');
        putchar(low - ((long) low) != low - ((long) low) ? '.' : 'F');
        putchar(high - ((long) high) != high - ((long) high) ? '.' : 'F');
        putchar(low - ((float) low) != low - ((float) low) ? '.' : 'F');
        putchar(high - ((float) high) != high - ((float) high) ? '.' : 'F');

        putchar('\n');
    }

    private static void putchar(char c)
    {
        out.print(c);
    }
}
