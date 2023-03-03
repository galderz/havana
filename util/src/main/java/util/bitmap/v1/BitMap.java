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
    final int highBitsCount;
    final int lowBitsCount;

    public BitMap()
    {
        this(8, 30, 30);
    }

    public BitMap(int objectAlignment, int highBitsCount, int lowBitsCount)
    {
        this.objectAlignment = objectAlignment;
        this.logAlignment = log2(objectAlignment);
        this.highBitsCount = highBitsCount;
        this.lowBitsCount = lowBitsCount;
    }

    int getSignum(long number)
    {
        return Long.signum(number);
    }

    static int log2(int num)
    {
        return 31 - Integer.numberOfLeadingZeros(num);
    }
}
