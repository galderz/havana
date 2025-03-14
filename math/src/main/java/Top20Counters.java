import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Top20Counters
{
    public static void main(String[] args)
    {
        System.out.println("Hello World!");

        long[] counters = new long[] {
            0, 12323, 3992, 32966, 24239, 6466, 3140, 3170, 2001, 2494, 783, 125, 36, 8, 87, 60, 41438, 23789, 36599, 28117, 9620, 49670, 13666, 74, 411, 69275, 6574, 20744, 22251, 16134, 2128, 2208, 2177, 1284, 40, 57, 417, 58, 162, 140, 97, 41, 198525, 89213, 43800, 25897, 2294, 7065, 19, 57, 4870, 2515, 387, 52, 21549, 7411, 24, 212, 29009, 315, 2241, 4754, 4908, 81, 213, 460, 388, 0, 1, 2, 12, 14, 11, 5, 14, 1747, 8261, 10312, 8515, 16328, 3522, 28, 74, 11922, 5757, 1221, 47, 22684, 144, 75052, 1077, 132, 1258, 33, 0, 530, 9101, 7052, 7, 92, 6331, 1418, 0, 84, 1474, 4740, 61, 196, 553, 302, 20, 65, 317, 110, 0, 1, 425, 122, 7, 12, 1272, 1886, 1151, 1538, 984, 571, 3789, 1840, 2127, 418, 959, 257, 7401, 2836, 84, 88, 1280, 25, 37, 56, 3, 34, 32, 33, 7, 2084, 696, 166, 3435, 71, 89, 80, 28, 24949, 17041, 2001, 1968, 793, 2298, 3696, 6681, 2380, 6230, 1674, 2620, 1510, 3210, 36597, 0, 0, 566, 405, 23516, 1620, 112, 213, 41139, 40409, 42731, 11222, 77794, 46833, 122597, 61611, 62638, 32426, 2049, 35835, 2701, 5608, 7908, 15071, 18016, 3482, 1372, 3513, 0, 42, 12286, 8768, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };

        print(counters);
    }

    static void print(long[] counters)
    {
        System.out.println(Arrays.toString(counters));

        long totalSum = Arrays.stream(counters).sum();
        System.out.println(totalSum);

        final int[] sortedIndexes = IntStream.range(0, counters.length)
            .boxed()
            .sorted((i, j) -> Long.compare(counters[j], counters[i]))
            .mapToInt(i -> i)
            .toArray();


        Arrays.stream(sortedIndexes)
            .limit(5)
            .forEach(i -> System.out.println(i + "=" + counters[i] + " (" + ((double) counters[i] / totalSum) * 100 + "%)"));
    }
}
