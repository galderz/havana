package util.bitmap.v1;

import java.util.BitSet;

//  0                   1                   2                   3                   4                   5                   6
//  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3
// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
// |S|                       High Bits                           |                     Low Bits                              | N/A |
// +-------------------------------------------------------------+-----------------------------------------------------------+-----+
public class BitMap
{
    final int objectAlignment;
    final int logAlignment;
    final int lowBitsCount;
    final long lowSize;
    final long lowMask;
    final int highBitsShift;

    public BitMap()
    {
        this(8);
    }

    public BitMap(int objectAlignment)
    {
        this.objectAlignment = objectAlignment;
        this.logAlignment = log2(objectAlignment);
        this.lowBitsCount = 30;
        this.lowSize = 1L << lowBitsCount;
        this.lowMask = lowSize - 1;
        this.highBitsShift = lowBitsCount + logAlignment;
    }

    int getSignum(long number)
    {
        return Long.signum(number);
    }

    int getLowBits(long num)
    {
        return (int) ((num >> logAlignment) & lowMask);
    }

    int getHighBits(long number)
    {
        return (int) (Math.abs(number) >> (highBitsShift));
    }

    static int log2(int num)
    {
        return 31 - Integer.numberOfLeadingZeros(num);
    }
}
