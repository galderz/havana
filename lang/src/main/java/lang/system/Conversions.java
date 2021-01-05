package lang.system;

import static java.lang.System.out;

public class Conversions
{
    public static void main(String[] args)
    {
        wideningPrimitives();
    }

    private static void wideningPrimitives()
    {
        wideningByte();
        wideningShort();
        wideningChar();
        wideningInt();
        wideningLong();
    }

    private static void wideningByte()
    {
        byte low = Byte.MIN_VALUE + 1;
        byte high = Byte.MAX_VALUE - 1;
        out.print(low - low == 0 ? '1' : '0');
        out.print(high - high == 0 ? '1' : '0');
        out.print(low - (byte)((short) low) == 0 ? '1' : '0');
        out.print(high - (byte)((short) high) == 0 ? '1' : '0');
        out.print(low - (byte)((int) low) == 0 ? '1' : '0');
        out.print(high - (byte)((int) high) == 0 ? '1' : '0');
        out.print(low - (byte)((long) low) == 0 ? '1' : '0');
        out.print(high - (byte)((long) high) == 0 ? '1' : '0');
        out.print(low - (byte)((float) low) == 0 ? '1' : '0');
        out.print(high - (byte)((float) high) == 0 ? '1' : '0');
        out.print(low - (byte)((double) low) == 0 ? '1' : '0');
        out.print(high - (byte)((double) high) == 0 ? '1' : '0');
    }

    private static void wideningShort()
    {
        short low = Short.MIN_VALUE + 1;
        short high = Short.MAX_VALUE - 1;
        out.print(low - low == 0 ? '1' : '0');
        out.print(high - high == 0 ? '1' : '0');
        out.print(low - (short)((int) low) == 0 ? '1' : '0');
        out.print(high - (short)((int) high) == 0 ? '1' : '0');
        out.print(low - (short)((long) low) == 0 ? '1' : '0');
        out.print(high - (short)((long) high) == 0 ? '1' : '0');
        out.print(low - (short)((float) low) == 0 ? '1' : '0');
        out.print(high - (short)((float) high) == 0 ? '1' : '0');
        out.print(low - (short)((double) low) == 0 ? '1' : '0');
        out.print(high - (short)((double) high) == 0 ? '1' : '0');
    }

    private static void wideningChar()
    {
        char low = Character.MIN_VALUE + 1;
        char high = Character.MAX_VALUE - 1;
        out.print(low - low == 0 ? '1' : '0');
        out.print(high - high == 0 ? '1' : '0');
        out.print(low - (char)((int) low) == 0 ? '1' : '0');
        out.print(high - (char)((int) high) == 0 ? '1' : '0');
        out.print(low - (char)((long) low) == 0 ? '1' : '0');
        out.print(high - (char)((long) high) == 0 ? '1' : '0');
        out.print(low - (char)((float) low) == 0 ? '1' : '0');
        out.print(high - (char)((float) high) == 0 ? '1' : '0');
        out.print(low - (char)((double) low) == 0 ? '1' : '0');
        out.print(high - (char)((double) high) == 0 ? '1' : '0');
    }

    private static void wideningInt()
    {
        int low = Integer.MIN_VALUE + 1;
        int high = Integer.MAX_VALUE - 1;
        out.print(low - low == 0 ? '1' : '0');
        out.print(high - high == 0 ? '1' : '0');
        out.print(low - (int)((long) low) == 0 ? '1' : '0');
        out.print(high - (int)((long) high) == 0 ? '1' : '0');
        out.print(low - (int)((float) low) == 1 ? '1' : '0'); // loss of precision
        out.print(high - (int)((float) high) == -1 ? '1' : '0'); // loss of precision
        out.print(low - (int)((double) low) == 0 ? '1' : '0');
        out.print(high - (int)((double) high) == 0 ? '1' : '0');
    }

    private static void wideningLong()
    {
        long low = Long.MIN_VALUE + 1;
        long high = Long.MAX_VALUE - 1;
        out.print(low - low == 0 ? '1' : '0');
        out.print(high - high == 0 ? '1' : '0');
        out.print(low - (long)((float) low) == 1 ? '1' : '0'); // loss of precision
        out.print(high - (long)((float) high) == -1 ? '1' : '0'); // loss of precision
        out.print(low - (long)((double) low) == 1 ? '1' : '0'); // loss of precision
        out.print(high - (long)((double) high) == -1 ? '1' : '0'); // loss of precision
    }
}
