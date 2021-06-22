package lang.system;

public strictfp class IntFloatInt
{
    /**
     * max:			2147483647
     * float max:	2147483392
     */
    public static void main(String[] args)
    {
        // An int contains 32 bits, 1 sign and 31 for the elements.
        System.out.printf("%H : max : 0b0111_1111_1111_1111_1111_1111_1111_1111%n", 0b0111_1111_1111_1111_1111_1111_1111_1111);
        System.out.printf("%H : max : 0x7FFF_FFFF%n", 0x7FFF_FFFF);
        System.out.println();

        // A float contains 32 bits:
        // 1 sign | 8 exponent | 23 bits mantissa
        // The highest positive number it can represent is
        System.out.printf("%H : float max value : 0b0111_1111_1111_1111_1111_1111_0000_0000%n", 0b0111_1111_1111_1111_1111_1111_0000_0000);
        System.out.printf("%H : float max value : 0x7FFF_FF00%n", 0x7FFF_FF00);
        System.out.printf("%H : float max value : (int)(float)0x7FFF_FF00%n", (int)(float)0x7FFF_FF00);
        System.out.println();

        // Round to nearest in action:
        System.out.printf("%H : 0x7FFF_FF01%n", 0x7FFF_FF01);
        System.out.printf("%H : (int)(float)0x7FFF_FF01%n", (int)(float)0x7FFF_FF01);
        System.out.printf("%s : 0x7FFF_FF00 == (int)(float)0x7FFF_FF01%n", 0x7FFF_FF00 == (int)(float)0x7FFF_FF01);
        System.out.println();

        System.out.printf("%H : 0x7FFF_FF00%n", 0x7FFF_FF00);
        System.out.printf("%s : (int)(float) 0x7FFF_FF40%n", Integer.toHexString((int)(float) 0x7FFF_FF40));
        System.out.printf("%H : (int)(float) 0x7FFF_FF3F%n", (int)(float) 0x7FFF_FF3F);
        System.out.printf("%H : 0x7FFF_FF80%n", 0x7FFF_FF80);
        System.out.printf("%H : (int)(float) 0x7FFF_FF41%n", (int)(float) 0x7FFF_FF41);
        System.out.println();

        System.out.println(Integer.toHexString(Float.floatToRawIntBits(Integer.MAX_VALUE - 1)));
        System.out.println(Integer.toHexString(Float.floatToRawIntBits(Integer.MAX_VALUE)));
        System.out.println(Integer.toHexString(Float.floatToRawIntBits((float)(double) (Integer.MAX_VALUE - 1))));
        System.out.printf("%H%n", Float.intBitsToFloat(0x4f000000));
        System.out.println();

        // For n=31 => Integers between 2^n and 2^n+1 round to a multiple of 2^(n-23)
        // n=31 2^31 and 2^32 round to a multiple of 2^(31-23)/2^8/256
        // n=30 2^30 and 2^31 round to a multiple of 2^(30-23)/2^7/128
        System.out.println((int)(float) (0x7FFF_FFFE));
        System.out.println(Integer.toHexString(Float.floatToRawIntBits(0x7FFF_FFFE))); // 4f000000
        System.out.println();
        System.out.println(0x7FFF_FF7F); // 0x7FFF_FF80
        System.out.println(Integer.toHexString((int)(float) (0x7FFF_FF7F))); // 0x7FFF_FF80
        System.out.println(Integer.toHexString(Float.floatToRawIntBits(0x7FFF_FF7F))); // 0x4effffff
        System.out.println();
        System.out.println(Integer.toHexString((int)(float) (0x7FFF_FF80))); // 0x7FFF_FF80
        System.out.println(Integer.toHexString(Float.floatToRawIntBits(0x7FFF_FF80))); // 0x4effffff
        System.out.println();
        System.out.println(Integer.toHexString((int)(float) (0x7FFF_FF81))); // 0x7FFF_FF80
        System.out.println(Integer.toHexString(Float.floatToRawIntBits(0x7FFF_FF81))); // 0x4effffff
        System.out.println();
        System.out.println(Integer.toHexString((int)(float) (0x7FFF_FFC0))); // 0x7FFF_FFFF
        System.out.println(Integer.toHexString(Float.floatToRawIntBits(0x7FFF_FFC0))); // 4f000000
        System.out.println();
        System.out.println(Integer.toHexString((int)(float) (0x7FFF_FFBF))); // 0x7FFF_FF80
        System.out.println(Integer.toHexString(Float.floatToRawIntBits(0x7FFF_FFBF))); // 0x4effffff

        System.out.println(0x7FFF_FFFE);
    }
}
