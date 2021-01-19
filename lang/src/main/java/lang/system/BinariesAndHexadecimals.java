package lang.system;

public class BinariesAndHexadecimals
{
    public static void main(String[] args)
    {
        System.out.println(Integer.toHexString(0b0111_1111_1111_1111_1111_1111_0000_1111));
        System.out.println(0b0111_1111_1111_1111_1111_1111_0000_1111);

        System.out.println(Integer.toHexString(0b0111_1111_1111_1111_1111_1111_0000_0000));
        System.out.println(0b0111_1111_1111_1111_1111_1111_0000_0000);
        // 2147483392
        // 1234567890
        // System.out.println(Integer.toHexString(0b0000_0000_1000_0000_0000_0000_0000_0000));
        // System.out.println(Integer.toHexString(0b0000_0000_0111_1111_1111_1111_1111_1111));

        // System.out.println(Integer.toHexString(0b0000_0000_1000_0000_0000_0000_0000_0000));
        // System.out.println(Integer.toHexString(0b0000_0000_0111_1111_1111_1111_1111_1111));
        // 7fffff
        // System.out.println(0b0000_0000_0111_1111_1111_1111_1111_1111);
        // 8388607
        // 1234567890

        int high = Integer.MAX_VALUE - 1;
        System.out.println(high);
        System.out.println(Float.toHexString(high));
        System.out.println(Integer.toBinaryString(Float.floatToIntBits(high)));
        System.out.println(Integer.toHexString((int)(float) high));
        System.out.println();
        int big = 1234567890;
        System.out.println(Integer.toHexString(big));
        System.out.println(Integer.toBinaryString(big));
        float approx = big;
        System.out.println(approx);
        System.out.println(Float.toHexString(approx));
        System.out.println(Integer.toHexString((int) approx));
        System.out.println(big - (int)approx);
        System.out.println(0x41e0000000000000L);
        System.out.println(Integer.MAX_VALUE - 1);
        System.out.println(0xc1e0000000000000L);
    }
}
