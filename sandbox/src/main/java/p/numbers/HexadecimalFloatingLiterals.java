package p.numbers;

public class HexadecimalFloatingLiterals
{
    public static void main(String[] args)
    {
        double a = 0x1.8p1;
        System.out.println(a);
        System.out.printf("%a%n", a);

        double b = 9.33263618503218878990e-302;
        System.out.printf("%a%n", b);
        System.out.println(b);
    }
}
