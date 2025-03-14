import java.util.HexFormat;

public class Alignment
{
    public static void main(String[] args)
    {
        final int alignment = 32; // 0x20

        System.out.println("Fast:");
        checkAlignment("0000000081181780", alignment);
        checkAlignment("000000008119ac58", alignment);
        checkAlignment("0000000081243048", alignment);

        System.out.println("Slow:");
        checkAlignment("00000000811558d0", alignment);
        checkAlignment("000000008116a770", alignment);
        checkAlignment("000000008122f7b8", alignment);
    }

    private static void checkAlignment(String number, int alignment)
    {
        final long modulo = Long.parseLong(number, 16) % alignment;
        System.out.printf("%s %% %s = %s%n", number, HexFormat.of().toHexDigits(alignment), modulo);
    }
}
