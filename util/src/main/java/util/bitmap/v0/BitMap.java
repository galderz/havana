package util.bitmap.v0;

import java.math.BigInteger;
import java.util.HexFormat;

//  0                   1                   2                   3                   4                   5                   6
//  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3
// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
// |                       High Bits                           |             Low Bits                                        | N/A |
// +-----------------------------------------------------------+-------------------------------------------------------------+-----+
public class BitMap
{
    final int objectAlignment;
    final int logAlignment;
    final int lowShift;
    final long lowSize;
    final long lowMask;

    public BitMap(int objectAlignment, int lowShift)
    {
        this.objectAlignment = objectAlignment;
        this.logAlignment = log2(objectAlignment);
        this.lowShift = lowShift;
        this.lowSize = 1L << lowShift;
        this.lowMask = lowSize - 1;
    }

    int getHighBits(long num)
    {
        return (int) (num >> (lowShift + logAlignment));
    }

    int getLowBits(long num)
    {
        return (int) ((num >> logAlignment) & lowMask);
    }

    public static void main(String[] args)
    {
        final HexFormat hex = HexFormat.of();
        // 000000010295e360
        // 0000 0001 0295 e360
        // [0, 0, 0, 1, 2, -107, -29, 96]
        // 4338344800
        final String axHex = "000000010295e360";
        final byte[] asBytes = hex.parseHex(axHex);
        long asNum = new BigInteger(axHex, 16).longValue();

        // 64
        // -3 for alignment
        // 61 = 26 + 25

        long granularityShift = 26;
        long granularitySize = 1 << granularityShift; // 0000000004000000
        // System.out.println(hex.toHexDigits(granularitySize, 16));
        long granularityMask = granularitySize - 1;   // 0000000003ffffff
        // System.out.println(hex.toHexDigits(granularityMask, 16));

        // 0000 0001 0295 e360 &
        // 0000 0000 03ff ffff =
        // 0000 0000 0295 e360
        //    0    2    9    5    e    3    6    0
        // 0000 0010 1001 0101 1110 0011 0110 0000
        // System.out.println(hex.toHexDigits(asNum & granularityMask, 16));
        // System.out.println(Long.toBinaryString(asNum & granularityMask));

        //    0    2    9    5    e    3    6    0
        // 0000 0010 1001 0101 1110 0011 0110 0000 >> 3
        // 0000 0000    5    2    b    c    6    c
        // 0000 0000 0101 0010 1011 1100 0110 1100
        int alignment = 8;
        int logAlignment = log2(alignment); // 3
        // System.out.println(hex.toHexDigits((asNum & granularityMask) >> logAlignment));

        // 000000010295e360
        // 0000 0001 0295 e360 >> 3
        // 0000 0000 2052 bc6c
        long asNumAligned = asNum >> logAlignment;
        System.out.println(hex.toHexDigits((asNumAligned)));
        System.out.println(asNumAligned);

        // 64 - 3 = 61
        // 29 & 32
        long lowShift = 32;
        long lowSize = 1L << lowShift;
        long lowMask = lowSize - 1;
        System.out.println(hex.toHexDigits(asNumAligned & lowMask));

        long highShift = 64 - lowShift - logAlignment;
        System.out.println(highShift);
        System.out.println(asNumAligned >> lowShift);
    }

    static int log2(int num)
    {
        return 31 - Integer.numberOfLeadingZeros(num);
    }
}
