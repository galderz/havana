package lang.system;

public class ScalbAndCast
{
    public static void main(String[] args)
    {
        int minBits = 32;
        double upper = Math.scalb(1.0, minBits - 1) - 1.0;
        System.out.printf("upper(inclusive) = %s | %s%n", upper, Long.toHexString(Double.doubleToRawLongBits(upper)));
        float upperCast = (float) upper;
        System.out.printf("upper cast (exclusive) = %s | %s%n", upperCast, Long.toHexString(Double.doubleToRawLongBits(upperCast)));
    }
}
