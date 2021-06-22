package lang.system;

public class UpperLimits
{
    public static void main(String[] args)
    {
//        final var floatUpper = Math.scalb(1.0, 32 - 1) - 1.0;
//        System.out.println(floatUpper);
//        System.out.println(Long.toHexString(Double.doubleToRawLongBits(floatUpper)));
//        System.out.println((long) Double.longBitsToDouble(0x41e0000000000000L));
//        System.out.println(Double.longBitsToDouble(0x41e0000000000000L));
//        System.out.println(Double.longBitsToDouble(0x41dfffffffc00000L));
//        System.out.println((long) Double.longBitsToDouble(0x41dfffffffc00000L));
//        System.out.println();

        System.out.println(Long.MAX_VALUE);
        System.out.println(Long.toHexString(Double.doubleToRawLongBits(Long.MAX_VALUE)));
        System.out.println(Long.MAX_VALUE - 1);
        System.out.println(0x43e0000000000000L);

        final var doubleUpper = Math.scalb(1.0, 64 - 1) - 1.0;
        System.out.println(doubleUpper);
        System.out.println(Long.toHexString(Double.doubleToRawLongBits(doubleUpper)));
        final var doubleMax = Math.scalb(1.0, 64 - 1);
        System.out.println(doubleMax);
        System.out.println(Long.toHexString(Double.doubleToRawLongBits(doubleMax)));
    }
}
