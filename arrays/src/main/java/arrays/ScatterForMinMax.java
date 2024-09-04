package arrays;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ScatterForMinMax
{
    static int[] scatter(int size, double successRate)
    {
        final int boundary = (int) Math.ceil((1 - successRate / 100) * size) - 1;
        System.out.printf("Boundary: %d%n", boundary);

        final int[] nums = new Random()
            .ints(0, 20)
            .distinct()
            .limit(size)
            .sorted()
            .toArray();
        int max = nums[size - 1];

        // todo use an array directly
        final List<Integer> result = new ArrayList<>(size);
        for (int i = 0; i < boundary; i++)
        {
            result.add(nums[i]);
        }

        result.add(max);

        for (int i = boundary; i < size - 1; i++)
        {
            result.add(nums[i]);
        }

        // Collections.shuffle(result.subList(0, boundary));
        Collections.sort(result.subList(0, boundary));
        Collections.shuffle(result.subList(boundary + 1, size));

        System.out.printf("Result: %s%n", result);

        return result.stream()
            .mapToInt(Integer::intValue)
            .toArray();
    }

    private static void testMax(int[] nums)
    {
        int max = Integer.MIN_VALUE;
        int below = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] >= max) {
                max = nums[i];
                System.out.println("Element: " + nums[i] + ", Current Max: " + max + ", Max");
            }
            if (nums[i] < max) {
                below++;
                System.out.println("Element: " + nums[i] + ", Current Max: " + max + ", Below");
            }

        }

        double belowMaxPercentage = (double) below / nums.length * 100;
        double aboveMaxPercentage = 100 - belowMaxPercentage;

        System.out.println("Percentage below or equal to max value: " + belowMaxPercentage + "%");
        System.out.println("Percentage above max value: " + aboveMaxPercentage + "%");
    }

    public static void main(String[] args)
    {
        testMax(scatter(10, 40.0));
    }
}
