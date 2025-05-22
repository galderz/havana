package arrays;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;

public class RandomIncrementEquals
{
    static long[][] distributeLongRandomIncrement(int size, int probability)
    {
        long[][] result;
        int aboveCount, abovePercent;

        // This algorithm generates 2 arrays of numbers.
        // The first array is created such that as the array is iterated,
        // there is P probability of finding a new min/max value,
        // and 100-P probability of not finding a new min/max value.
        // This first array is used on its own for tests that iterate an array to reduce it to a single value,
        // e.g. the min or max value in the array.
        // The second array is loaded with values relative to the first array,
        // such that when the values in the same index are compared for min/max,
        // the probability that a new min/max value is found has the probability P.
        do
        {
            long max = ThreadLocalRandom.current().nextLong(10);
            result = new long[2][size];
            result[0][0] = max;
            result[1][0] = max - 1;

            aboveCount = 0;
            for (int i = 1; i < result[0].length; i++)
            {
                long value;
                if (ThreadLocalRandom.current().nextLong(101) <= probability)
                {
                    long increment = ThreadLocalRandom.current().nextLong(10);
                    value = max + increment;
                    aboveCount++;
                }
                else
                {
                    // Decrement by at least 1
                    long diffToMax = ThreadLocalRandom.current().nextLong(10) + 1;
                    value = max - diffToMax;
                }
                result[0][i] = value;
                result[1][i] = max;
                max = Math.max(max, value);
            }

            abovePercent = ((aboveCount + 1) * 100) / size;
        } while (abovePercent != probability);

        return result;
    }

    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();

        testMinReduction(negate(distributeLongRandomIncrement(10, 50)[0]), 50);
        testMinReduction(negate(distributeLongRandomIncrement(10, 60)[0]), 60);
        testMinReduction(negate(distributeLongRandomIncrement(10, 80)[0]), 80);
        testMinReduction(negate(distributeLongRandomIncrement(10, 100)[0]), 100);

        testMaxReduction(distributeLongRandomIncrement(10, 50)[0], 50);
        testMaxReduction(distributeLongRandomIncrement(10, 60)[0], 60);
        testMaxReduction(distributeLongRandomIncrement(10, 80)[0], 80);
        testMaxReduction(distributeLongRandomIncrement(10, 100)[0], 100);

        testMaxReduction(distributeLongRandomIncrement(100, 50)[0], 50);
        testMaxReduction(distributeLongRandomIncrement(100, 60)[0], 60);
        testMaxReduction(distributeLongRandomIncrement(100, 75)[0], 75);
        testMaxReduction(distributeLongRandomIncrement(100, 80)[0], 80);
        testMaxReduction(distributeLongRandomIncrement(100, 100)[0], 100);

        testMaxReduction(distributeLongRandomIncrement(1000, 50)[0], 50);
        testMaxReduction(distributeLongRandomIncrement(1000, 60)[0], 60);
        testMaxReduction(distributeLongRandomIncrement(1000, 75)[0], 75);
        testMaxReduction(distributeLongRandomIncrement(1000, 80)[0], 80);
        testMaxReduction(distributeLongRandomIncrement(1000, 100)[0], 100);

        testMaxReduction(distributeLongRandomIncrement(10000, 50)[0], 50);
        testMaxReduction(distributeLongRandomIncrement(10000, 60)[0], 60);
        testMaxReduction(distributeLongRandomIncrement(10000, 75)[0], 75);
        testMaxReduction(distributeLongRandomIncrement(10000, 80)[0], 80);
        testMaxReduction(distributeLongRandomIncrement(10000, 100)[0], 100);

        testMaxLoop(distributeLongRandomIncrement(10, 50), 50);
        testMaxLoop(distributeLongRandomIncrement(10, 60), 60);
        testMaxLoop(distributeLongRandomIncrement(10, 80), 80);
        testMaxLoop(distributeLongRandomIncrement(10, 100), 100);

        testMaxLoop(distributeLongRandomIncrement(100, 50), 50);
        testMaxLoop(distributeLongRandomIncrement(100, 60), 60);
        testMaxLoop(distributeLongRandomIncrement(100, 75), 75);
        testMaxLoop(distributeLongRandomIncrement(100, 80), 80);
        testMaxLoop(distributeLongRandomIncrement(100, 100), 100);

        testMaxLoop(distributeLongRandomIncrement(1000, 50), 50);
        testMaxLoop(distributeLongRandomIncrement(1000, 60), 60);
        testMaxLoop(distributeLongRandomIncrement(1000, 75), 75);
        testMaxLoop(distributeLongRandomIncrement(1000, 80), 80);
        testMaxLoop(distributeLongRandomIncrement(1000, 100), 100);

        testMaxLoop(distributeLongRandomIncrement(10000, 50), 50);
        testMaxLoop(distributeLongRandomIncrement(10000, 60), 60);
        testMaxLoop(distributeLongRandomIncrement(10000, 75), 75);
        testMaxLoop(distributeLongRandomIncrement(10000, 80), 80);
        testMaxLoop(distributeLongRandomIncrement(10000, 100), 100);
    }

    private static void testMaxLoop(long[][] nums, int expectedAboveOrEqualMax)
    {
        final long[] a = nums[0];
        final long[] b = nums[1];
        int above = 0;

        for (int i = 0; i < a.length; i++) {
            if (a[i] >= b[i]) {
                above++;
                // System.out.printf("a[%d] = %d, b[%d] = %d, a side%n", i, a[i], i, b[i]);
            }
            if (a[i] < b[i]) {
                // System.out.printf("a[%d] = %d, b[%d] = %d, b side%n", i, a[i], i, b[i]);
            }

        }

        int aboveOrEqualMaxPercentage = (above * 100) / a.length;
        int belowMaxPercentage = 100 - aboveOrEqualMaxPercentage;

        System.out.printf("Percentage above or equal max value: %d%% from above %d and and array size %d%n", aboveOrEqualMaxPercentage, above, a.length);
        System.out.printf("Percentage below to max value: %d%%%n", belowMaxPercentage);

        assert aboveOrEqualMaxPercentage == expectedAboveOrEqualMax : String.format("Expected %d%% above or equal max but got %d%%", expectedAboveOrEqualMax, aboveOrEqualMaxPercentage);
    }

    private static void testMaxReduction(long[] nums, int expectedAboveOrEqualMax)
    {
        long max = Long.MIN_VALUE;
        int above = 0;

        for (int i = 0; i < nums.length; i++) {
            final long v = 11 * nums[i];
            if (v >= max) {
                above++;
                max = v;
                // System.out.println("Element: " + nums[i] + ", Current Max: " + max + ", Max");
            }
            if (v < max) {
                // System.out.println("Element: " + nums[i] + ", Current Max: " + max + ", Below");
            }
        }

        int aboveOrEqualMaxPercentage = (above * 100) / nums.length;
        int belowMaxPercentage = 100 - aboveOrEqualMaxPercentage;

        System.out.printf("Percentage above or equal max value: %d%% from above %d and and array size %d%n", aboveOrEqualMaxPercentage, above, nums.length);
        System.out.printf("Percentage below to max value: %d%%%n", belowMaxPercentage);

        assert aboveOrEqualMaxPercentage == expectedAboveOrEqualMax : String.format("Expected %d%% above or equal max but got %d%%", expectedAboveOrEqualMax, aboveOrEqualMaxPercentage);
    }

    private static void testMinReduction(long[] nums, int expectedBelowOrEqualMin)
    {
        long min = Long.MAX_VALUE;
        int below = 0;

        for (int i = 0; i < nums.length; i++) {
            final long v = 11 * nums[i];
            if (v <= min) {
                below++;
                min = v;
                System.out.println("Element: " + v + ", Current Min: " + min + ", Below");
            }
            if (v > min) {
                System.out.println("Element: " + v + ", Current Min: " + min + ", Above");
            }

        }

        int belowOrEqualMinPercentage = (below * 100) / nums.length;
        int aboveMinPercentage = 100 - belowOrEqualMinPercentage;

        System.out.printf("Percentage above or equal max value: %d%% from above %d and and array size %d%n", belowOrEqualMinPercentage, below, nums.length);
        System.out.printf("Percentage below to max value: %d%%%n", aboveMinPercentage);

        assert belowOrEqualMinPercentage == expectedBelowOrEqualMin : String.format("Expected %d%% above or equal max but got %d%%", expectedBelowOrEqualMin, belowOrEqualMinPercentage);
    }

    static long[] negate(long[] nums)
    {
        return LongStream.of(nums).map(l -> -l).toArray();
    }
}
