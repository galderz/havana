public class Rounding
{
    public static void main(String[] args)
    {
        final int size = 1000;
        final int aboveCount = 506;
        final int abovePercent = ((aboveCount + 1) * 100) / size;
        System.out.println(abovePercent);

        final int below = 493;
        final int belowPercent = below * 100 / size;
        System.out.println(belowPercent);
    }
}
