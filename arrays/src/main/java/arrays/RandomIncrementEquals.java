package arrays;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;

public class RandomIncrementEquals
{
    static long[][] distributeLongRandomIncrement(int size, int probability, boolean includeEquals)
    {
        final long aboveOrEqualsLowerBound = includeEquals ? 0 : 1;

        long[][] result;
        int aboveOrEqualsCount, aboveOrEqualsPercent;

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

            // Assume that the first value is above or equals to the current max
            aboveOrEqualsCount = 1;
            for (int i = 1; i < result[0].length; i++)
            {
                long value;
                if (i == 1 && includeEquals)
                {
                    // If include equals make sure there is at least one element that is equals to the current min/max.
                    // So, in that case make the 2nd element in the array the same as the current min/max.
                    value = max;
                    aboveOrEqualsCount++;
                    // System.out.printf("[generate] value[1] = %d; special includeEquals value, aboveOrEqualsCount = %d%n", value, aboveOrEqualsCount);
                }
                else if (ThreadLocalRandom.current().nextLong(101) <= probability)
                {
                    long increment = ThreadLocalRandom.current().nextLong(aboveOrEqualsLowerBound, 10);
                    value = max + increment;
                    aboveOrEqualsCount++;
                    // System.out.printf("[generate] value[%d] = %d; above or equals, aboveOrEqualsCount = %d%n", i, value, aboveOrEqualsCount);
                }
                else
                {
                    // To generate a value strictly lower value, it must generate at least 1 element lower
                    long diffToMax = ThreadLocalRandom.current().nextLong(1, 10);
                    value = max - diffToMax;
                    // System.out.printf("[generate] value[%d] = %d; below, aboveOrEqualsCount = %d%n", i, value, aboveOrEqualsCount);
                }
                result[0][i] = value;
                result[1][i] = max;
                max = Math.max(max, value);
            }

            aboveOrEqualsPercent = (aboveOrEqualsCount * 100) / size;
        } while (aboveOrEqualsPercent != probability);

        return result;
    }

    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        new Test(true).test();
        new Test(false).test();
    }

    private static class Test
    {
        final boolean includesEquals;

        private Test(boolean includesEquals)
        {
            this.includesEquals = includesEquals;
        }

        void test()
        {
            testMinReduction(negate(distributeLongRandomIncrement(10, 50, includesEquals)[0]), 50, includesEquals);
            testMinReduction(negate(distributeLongRandomIncrement(10, 60, includesEquals)[0]), 60, includesEquals);
            testMinReduction(negate(distributeLongRandomIncrement(10, 80, includesEquals)[0]), 80, includesEquals);
            testMinReduction(negate(distributeLongRandomIncrement(10, 100, includesEquals)[0]), 100, includesEquals);

            testMaxReduction(distributeLongRandomIncrement(10, 50, includesEquals)[0], 50, includesEquals);
            testMaxReduction(distributeLongRandomIncrement(10, 60, includesEquals)[0], 60, includesEquals);
            testMaxReduction(distributeLongRandomIncrement(10, 80, includesEquals)[0], 80, includesEquals);
            testMaxReduction(distributeLongRandomIncrement(10, 100, includesEquals)[0], 100, includesEquals);

            testMaxReduction(distributeLongRandomIncrement(100, 50, includesEquals)[0], 50, includesEquals);
            testMaxReduction(distributeLongRandomIncrement(100, 60, includesEquals)[0], 60, includesEquals);
            testMaxReduction(distributeLongRandomIncrement(100, 75, includesEquals)[0], 75, includesEquals);
            testMaxReduction(distributeLongRandomIncrement(100, 80, includesEquals)[0], 80, includesEquals);
            testMaxReduction(distributeLongRandomIncrement(100, 100, includesEquals)[0], 100, includesEquals);

            testMaxReduction(distributeLongRandomIncrement(1000, 50, includesEquals)[0], 50, includesEquals);
            testMaxReduction(distributeLongRandomIncrement(1000, 60, includesEquals)[0], 60, includesEquals);
            testMaxReduction(distributeLongRandomIncrement(1000, 75, includesEquals)[0], 75, includesEquals);
            testMaxReduction(distributeLongRandomIncrement(1000, 80, includesEquals)[0], 80, includesEquals);
            testMaxReduction(distributeLongRandomIncrement(1000, 100, includesEquals)[0], 100, includesEquals);

            testMaxReduction(distributeLongRandomIncrement(10000, 50, includesEquals)[0], 50, includesEquals);
            testMaxReduction(distributeLongRandomIncrement(10000, 60, includesEquals)[0], 60, includesEquals);
            testMaxReduction(distributeLongRandomIncrement(10000, 75, includesEquals)[0], 75, includesEquals);
            testMaxReduction(distributeLongRandomIncrement(10000, 80, includesEquals)[0], 80, includesEquals);
            testMaxReduction(distributeLongRandomIncrement(10000, 100, includesEquals)[0], 100, includesEquals);

            testMaxLoop(distributeLongRandomIncrement(10, 50, includesEquals), 50, includesEquals);
            testMaxLoop(distributeLongRandomIncrement(10, 60, includesEquals), 60, includesEquals);
            testMaxLoop(distributeLongRandomIncrement(10, 80, includesEquals), 80, includesEquals);
            testMaxLoop(distributeLongRandomIncrement(10, 100, includesEquals), 100, includesEquals);

            testMaxLoop(distributeLongRandomIncrement(100, 50, includesEquals), 50, includesEquals);
            testMaxLoop(distributeLongRandomIncrement(100, 60, includesEquals), 60, includesEquals);
            testMaxLoop(distributeLongRandomIncrement(100, 75, includesEquals), 75, includesEquals);
            testMaxLoop(distributeLongRandomIncrement(100, 80, includesEquals), 80, includesEquals);
            testMaxLoop(distributeLongRandomIncrement(100, 100, includesEquals), 100, includesEquals);

            testMaxLoop(distributeLongRandomIncrement(1000, 50, includesEquals), 50, includesEquals);
            testMaxLoop(distributeLongRandomIncrement(1000, 60, includesEquals), 60, includesEquals);
            testMaxLoop(distributeLongRandomIncrement(1000, 75, includesEquals), 75, includesEquals);
            testMaxLoop(distributeLongRandomIncrement(1000, 80, includesEquals), 80, includesEquals);
            testMaxLoop(distributeLongRandomIncrement(1000, 100, includesEquals), 100, includesEquals);

            testMaxLoop(distributeLongRandomIncrement(10000, 50, includesEquals), 50, includesEquals);
            testMaxLoop(distributeLongRandomIncrement(10000, 60, includesEquals), 60, includesEquals);
            testMaxLoop(distributeLongRandomIncrement(10000, 75, includesEquals), 75, includesEquals);
            testMaxLoop(distributeLongRandomIncrement(10000, 80, includesEquals), 80, includesEquals);
            testMaxLoop(distributeLongRandomIncrement(10000, 100, includesEquals), 100, includesEquals);
        }
    }

    private static void testMaxLoop(long[][] nums, int expectedAboveOrEqualMax, boolean includesEquals)
    {
        final long[] a = nums[0];
        final long[] b = nums[1];
        int above = 0;
        int equals = 0;

        for (int i = 0; i < a.length; i++) {
            if (a[i] == b[i])
            {
                // System.out.printf("a[%d] = %d, b[%d] = %d, equals%n", i, a[i], i, b[i]);
                equals++;
            }
            else if (a[i] > b[i])
            {
                // System.out.printf("a[%d] = %d, b[%d] = %d, a side%n", i, a[i], i, b[i]);
                above++;
            }
            else
            {
                // System.out.printf("a[%d] = %d, b[%d] = %d, b side%n", i, a[i], i, b[i]);
            }
        }

        final int aboveOrEquals = above + equals;
        int aboveOrEqualMaxPercentage = (aboveOrEquals * 100) / a.length;
        int belowMaxPercentage = 100 - aboveOrEqualMaxPercentage;

        System.out.printf("Percentage above or equal max value: %d%% from above %d and and array size %d%n", aboveOrEqualMaxPercentage, aboveOrEquals, a.length);
        System.out.printf("Percentage below to max value: %d%%%n", belowMaxPercentage);

        assert aboveOrEqualMaxPercentage == expectedAboveOrEqualMax : String.format("Expected %d%% above or equal max but got %d%%", expectedAboveOrEqualMax, aboveOrEqualMaxPercentage);
        if (includesEquals)
        {
            assert equals > 0 : "Expected equals to be bigger than zero but was zero";
        }
        else
        {
            assert equals == 0 : String.format("Expected equals to be zero but was %d", equals);
        }
    }

    private static void testMaxReduction(long[] nums, int expectedAboveOrEqualMax, boolean includesEquals)
    {
        long max = Long.MIN_VALUE;
        int above = 0;
        int equals = 0;

        for (int i = 0; i < nums.length; i++){
            final long v = 11 * nums[i];
            if (v == max)
            {
                System.out.println("Element: " + v + ", Current Max: " + max + ", Equals");
                equals++;
            }
            else if (v > max)
            {
                System.out.println("Element: " + v + ", Current Max: " + max + ", Above");
                above++;
            }
            else
            {
                System.out.println("Element: " + v + ", Current Max: " + max + ", Below");
            }

            if (v >= max)
            {
                max = v;
            }
        }

        final int aboveOrEquals = above + equals;
        int aboveOrEqualMaxPercentage = (aboveOrEquals * 100) / nums.length;
        int belowMaxPercentage = 100 - aboveOrEqualMaxPercentage;

        System.out.printf("Percentage above or equal max value: %d%% from above or equals %d and and array size %d%n", aboveOrEqualMaxPercentage, aboveOrEquals, nums.length);
        System.out.printf("Percentage below max value: %d%%%n", belowMaxPercentage);

        assert aboveOrEqualMaxPercentage == expectedAboveOrEqualMax : String.format("Expected %d%% above or equal max but got %d%%", expectedAboveOrEqualMax, aboveOrEqualMaxPercentage);
        if (includesEquals)
        {
            assert equals > 0 : "Expected equals to be bigger than zero but was zero";
        }
        else
        {
            assert equals == 0 : String.format("Expected equals to be zero but was %d", equals);
        }
    }

    private static void testMinReduction(long[] nums, int expectedBelowOrEqualMin, boolean includesEquals)
    {
        long min = Long.MAX_VALUE;
        int below = 0;
        int equals = 0;

        for (int i = 0; i < nums.length; i++) {
            final long v = 11 * nums[i];
            if (v == min)
            {
                System.out.println("Element: " + v + ", Current Min: " + min + ", Equals");
                equals++;
            }
            else if (v < min)
            {
                System.out.println("Element: " + v + ", Current Min: " + min + ", Below");
                below++;
            }
            else
            {
                System.out.println("Element: " + v + ", Current Min: " + min + ", Above");
            }

            if (v <= min)
            {
                min = v;
            }
        }

        final int belowOrEquals = below + equals;
        int belowOrEqualMinPercentage = (belowOrEquals * 100) / nums.length;
        int aboveMinPercentage = 100 - belowOrEqualMinPercentage;

        System.out.printf("Percentage below or equal min value: %d%% from below or equals %d and and array size %d%n", belowOrEqualMinPercentage, belowOrEquals, nums.length);
        System.out.printf("Percentage above min value: %d%%%n", aboveMinPercentage);

        assert belowOrEqualMinPercentage == expectedBelowOrEqualMin : String.format("Expected %d%% above or equal max but got %d%%", expectedBelowOrEqualMin, belowOrEqualMinPercentage);
        if (includesEquals)
        {
            assert equals > 0 : "Expected equals to be bigger than zero but was zero";
        }
        else
        {
            assert equals == 0 : String.format("Expected equals to be zero but was %d", equals);
        }
    }

    static long[] negate(long[] nums)
    {
        return LongStream.of(nums).map(l -> -l).toArray();
    }
}
