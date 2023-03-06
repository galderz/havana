package util.bitmap.v1;

/**
 * A fixed-size, allocation free, bit set.
 */
class NoAllocationBitSet
{
    static final int ADDRESS_BITS_PER_WORD = 6;
    static final long WORD_MASK = 0xffffffffffffffffL;
    static final int BITS_PER_WORD = 1 << ADDRESS_BITS_PER_WORD;
    final long[] words;
    int wordsInUse = 0;

    NoAllocationBitSet(int numberOfBits)
    {
        words = new long[wordIndex(numberOfBits - 1) + 1];
    }

    /**
     * Sets the bit at the specified index to true.
     * It returns false if the bit set is not big enough to set the specified index,
     * otherwise returns true.
     */
    boolean set(int bitIndex)
    {
        assert bitIndex >= 0 : "bit index can't be negative";

        int wordIndex = wordIndex(bitIndex);
        int wordsRequired = wordIndex + 1;
        if (wordsInUse < wordsRequired) {
            if (words.length < wordsRequired) {
                return false; // not enough space
            }
            wordsInUse = wordsRequired;
        }

        words[wordIndex] |= (1L << bitIndex);
        return true;
    }

    int nextSetBit(int fromIndex)
    {
        assert fromIndex >= 0 : "from index can't be negative";

        int index = wordIndex(fromIndex);
        if (index >= wordsInUse)
        {
            return -1;
        }

        long word = words[index] & (WORD_MASK << fromIndex);

        while (true)
        {
            if (word != 0)
            {
                return (index * BITS_PER_WORD) + Long.numberOfTrailingZeros(word);
            }
            if (++index == wordsInUse)
            {
                return -1;
            }

            word = words[index];
        }
    }

    private static int wordIndex(int bitIndex)
    {
        return bitIndex >> ADDRESS_BITS_PER_WORD;
    }
}
