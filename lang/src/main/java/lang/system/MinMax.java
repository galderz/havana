package lang.system;

public class MinMax
{
    public static void main(String[] args)
    {
        System.out.printf("signed Integer.MAX_VALUE = %s | %s%n", Integer.MAX_VALUE, signedMaxValue(32));
        System.out.printf("signed Long.MAX_VALUE = %s | %s%n", Long.MAX_VALUE, signedMaxValue(64));
        System.out.printf("unsigned integer max = %s%n", Long.toUnsignedString(unsignedMaxValue(32)));
        System.out.printf("unsigned long max = %s%n", Long.toUnsignedString(unsignedMaxValue(64)));

        System.out.printf("signed Integer.MIN_VALUE = %s | %s%n", Integer.MIN_VALUE, signedMinValue(32));
        System.out.printf("signed Long.MIN_VALUE = %s | %s%n", Long.MIN_VALUE, signedMinValue(64));
    }

    static long signedMinValue(int minBits) {
        return ~signedMaxValue(minBits);
    }

    static long unsignedMaxValue(int minBits) {
        final var numBits = minBits - 1;
        return 1L << numBits | (1L << numBits) - 1;
    }

    static long signedMaxValue(int minBits) {
        return (1L << (minBits - 1)) - 1;
    }
}
