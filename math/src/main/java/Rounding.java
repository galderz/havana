public class Rounding
{
    public static void main(String[] args)
    {
        {
            final int size = 1000;
            final int aboveCount = 506;
            final int abovePercent = ((aboveCount + 1) * 100) / size;
            System.out.println(abovePercent);
        }

        {
            final int size = 1000;
            final int below = 493;
            final int belowPercent = below * 100 / size;
            System.out.println(belowPercent);
        }

        {
            final int size = 10;
            final int aboveCount = 7;
            final int abovePercent = (aboveCount + 1) * 100 / size;
            System.out.println(abovePercent);
        }

        {
            final long base = Long.MAX_VALUE;
            final int range = 90;
            final long rangeMax = (long) (base * (range / 100f));
            final long rangeMin = Long.MIN_VALUE + (base - rangeMax);
            System.out.println(rangeMax);
            System.out.println(rangeMin);
        }
    }
}
