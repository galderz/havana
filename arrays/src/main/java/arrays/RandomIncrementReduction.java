package arrays;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.LongStream;

public class RandomIncrementReduction
{
    static long[] randomIncrement(int size, int probability)
    {
        final Random random = new Random();

        long[] result;
        int aboveCount;
        int abovePercent;
        int belowCount;
        int numberOfRounds = 0;

        // Iterate until you find a set that matches the requirement probability
        do {
            numberOfRounds++;

            long max = random.nextLong(10);
            result = new long[size];
            result[0] = max;

            aboveCount = 0;
            belowCount = 0;
            for (int i = 1; i < result.length; i++)
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
                    // System.out.printf("Decrement, decrement: %d, max: %d%n", decrement, max);
                    belowCount++;
                }

                result[i] = value;
                max = Math.max(max, value);
            }

            abovePercent = ((aboveCount + 1) * 100) / size;
            System.out.printf("Probability: %d, above obtained: %d%n", probability, abovePercent);

        } while (abovePercent != probability);

        System.out.printf(
            "Number of rounds: %d, size: %d, above max: %d, below max: %d, array: %s%n"
            , numberOfRounds
            , size
            , aboveCount
            , belowCount
            , Arrays.toString(result)
        );

        return result;
    }

    static long[] negate(long[] nums)
    {
        return LongStream.of(nums).map(l -> -l).toArray();
    }

    private static void testMaxReduction(long[] nums, int expectedAboveOrEqualMax)
    {
        long max = Long.MIN_VALUE;
        int above = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] >= max) {
                above++;
                max = nums[i];
                // System.out.println("Element: " + nums[i] + ", Current Max: " + max + ", Max");
            }
            if (nums[i] < max) {
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
            if (nums[i] <= min) {
                below++;
                min = nums[i];
                System.out.println("Element: " + nums[i] + ", Current Min: " + min + ", Below");
            }
            if (nums[i] > min) {
                System.out.println("Element: " + nums[i] + ", Current Min: " + min + ", Above");
            }

        }

        int belowOrEqualMinPercentage = (below * 100) / nums.length;
        int aboveMinPercentage = 100 - belowOrEqualMinPercentage;

        System.out.printf("Percentage above or equal max value: %d%% from above %d and and array size %d%n", belowOrEqualMinPercentage, below, nums.length);
        System.out.printf("Percentage below to max value: %d%%%n", aboveMinPercentage);

        assert belowOrEqualMinPercentage == expectedBelowOrEqualMin : String.format("Expected %d%% above or equal max but got %d%%", expectedBelowOrEqualMin, belowOrEqualMinPercentage);
    }

    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testMinReduction(negate(randomIncrement(10, 50)), 50);
        testMinReduction(negate(randomIncrement(10, 60)), 60);
        testMinReduction(negate(randomIncrement(10, 80)), 80);
        testMinReduction(negate(randomIncrement(10, 100)), 100);

        testMaxReduction(randomIncrement(10, 50), 50);
        testMaxReduction(randomIncrement(10, 60), 60);
        testMaxReduction(randomIncrement(10, 80), 80);
        testMaxReduction(randomIncrement(10, 100), 100);

        testMaxReduction(randomIncrement(100, 50), 50);
        testMaxReduction(randomIncrement(100, 60), 60);
        testMaxReduction(randomIncrement(100, 75), 75);
        testMaxReduction(randomIncrement(100, 80), 80);
        testMaxReduction(randomIncrement(100, 100), 100);

        testMaxReduction(randomIncrement(1000, 50), 50);
        testMaxReduction(randomIncrement(1000, 60), 60);
        testMaxReduction(randomIncrement(1000, 75), 75);
        testMaxReduction(randomIncrement(1000, 80), 80);
        testMaxReduction(randomIncrement(1000, 100), 100);

        testMaxReduction(randomIncrement(10000, 50), 50);
        testMaxReduction(randomIncrement(10000, 60), 60);
        testMaxReduction(randomIncrement(10000, 75), 75);
        testMaxReduction(randomIncrement(10000, 80), 80);
        testMaxReduction(randomIncrement(10000, 100), 100);
    }
}
