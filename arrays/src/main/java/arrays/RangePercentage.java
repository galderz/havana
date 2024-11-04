package arrays;

public class RangePercentage
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();

        {
            int range = 90;
            int max = 48920;
            int min = 0;

            int highest = (max * range) / 100;
            int lowest = min + (max - highest);
            assert 44028 == highest;
            assert 4892 == lowest;
        }

        {
            int range = 90;
            int max = 100;
            int min = 0;

            int highest = (max * range) / 100;
            int lowest = min + (max - highest);
            assert 90 == highest;
            assert 10 == lowest;
        }

        {
            int range = 100;
            int max = 100;
            int min = 0;

            int highest = (max * range) / 100;
            int lowest = min + (max - highest);
            assert 100 == highest;
            assert 0 == lowest;
        }
    }
}
