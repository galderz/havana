package lang;

public class Widening
{
    public static void main(String[] args)
    {
        intToFloat();
    }

    private static void intToFloat()
    {
        int big = 1234567890;
        System.out.println(big);
        float aprox = big;
        System.out.println(big - (int) aprox);
    }
}
