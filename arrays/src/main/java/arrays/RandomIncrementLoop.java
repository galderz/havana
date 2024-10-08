package arrays;

import java.util.Random;
import java.util.stream.LongStream;

public class RandomIncrementLoop
{
    static long[][] randomIncrement(int size, int probability)
    {
        final Random random = new Random();

        long[][] result;
        int aboveCount;
        int abovePercent;
        int belowCount;

        // Iterate until you find a set that matches the requirement probability
        do {
            long max = random.nextLong(10);
            result = new long[2][size];
            result[0][0] = max;
            result[1][0] = max - 1;

            aboveCount = 0;
            belowCount = 0;
            for (int i = 1; i < result[0].length; i++)
            {
                long value;

                if (random.nextLong(101) <= probability)
                {
                    long increment = random.nextLong(10);
                    value = max + increment;
                    // System.out.printf("Above, increment: %d, max: %d%n", increment, max);
                    aboveCount++;
                }
                else
                {
                    // Decrement by at least 1
                    long decrement = random.nextLong(10) + 1;
                    value = max - decrement;
                    // System.out.printf("Below, decrement: %d, max: %d%n", decrement, max);
                    belowCount++;
                }

                result[0][i] = value;
                result[1][i] = max;
                max = Math.max(max, value);
            }

            abovePercent = ((aboveCount + 1) * 100) / size;
            // System.out.printf("Probability: %d, above obtained: %d%n", probability, abovePercent);

        } while (abovePercent != probability);

        System.out.printf("Size: %d, above max: %d, below max: %d, arrays: %n", size, aboveCount, belowCount);
        // System.out.printf("a: %s, b: %s%n", Arrays.toString(result[0]), Arrays.toString(result[1]));

        return result;
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

    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();

        testMinReduction(negate(randomIncrement(10, 50)[0]), 50);
        testMinReduction(negate(randomIncrement(10, 60)[0]), 60);
        testMinReduction(negate(randomIncrement(10, 80)[0]), 80);
        testMinReduction(negate(randomIncrement(10, 100)[0]), 100);

        testMaxReduction(randomIncrement(10, 50)[0], 50);
        testMaxReduction(randomIncrement(10, 60)[0], 60);
        testMaxReduction(randomIncrement(10, 80)[0], 80);
        testMaxReduction(randomIncrement(10, 100)[0], 100);

        testMaxReduction(randomIncrement(100, 50)[0], 50);
        testMaxReduction(randomIncrement(100, 60)[0], 60);
        testMaxReduction(randomIncrement(100, 75)[0], 75);
        testMaxReduction(randomIncrement(100, 80)[0], 80);
        testMaxReduction(randomIncrement(100, 100)[0], 100);

        testMaxReduction(randomIncrement(1000, 50)[0], 50);
        testMaxReduction(randomIncrement(1000, 60)[0], 60);
        testMaxReduction(randomIncrement(1000, 75)[0], 75);
        testMaxReduction(randomIncrement(1000, 80)[0], 80);
        testMaxReduction(randomIncrement(1000, 100)[0], 100);

        testMaxReduction(randomIncrement(10000, 50)[0], 50);
        testMaxReduction(randomIncrement(10000, 60)[0], 60);
        testMaxReduction(randomIncrement(10000, 75)[0], 75);
        testMaxReduction(randomIncrement(10000, 80)[0], 80);
        testMaxReduction(randomIncrement(10000, 100)[0], 100);

        testMaxLoop(randomIncrement(10, 50), 50);
        testMaxLoop(randomIncrement(10, 60), 60);
        testMaxLoop(randomIncrement(10, 80), 80);
        testMaxLoop(randomIncrement(10, 100), 100);

        testMaxLoop(randomIncrement(100, 50), 50);
        testMaxLoop(randomIncrement(100, 60), 60);
        testMaxLoop(randomIncrement(100, 75), 75);
        testMaxLoop(randomIncrement(100, 80), 80);
        testMaxLoop(randomIncrement(100, 100), 100);

        testMaxLoop(randomIncrement(1000, 50), 50);
        testMaxLoop(randomIncrement(1000, 60), 60);
        testMaxLoop(randomIncrement(1000, 75), 75);
        testMaxLoop(randomIncrement(1000, 80), 80);
        testMaxLoop(randomIncrement(1000, 100), 100);

        testMaxLoop(randomIncrement(10000, 50), 50);
        testMaxLoop(randomIncrement(10000, 60), 60);
        testMaxLoop(randomIncrement(10000, 75), 75);
        testMaxLoop(randomIncrement(10000, 80), 80);
        testMaxLoop(randomIncrement(10000, 100), 100);
    }
}
