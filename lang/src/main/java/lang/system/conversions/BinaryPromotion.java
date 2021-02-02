package lang.system.conversions;

public class BinaryPromotion
{
    public static void main()
    {
        binaryPromotion();
    }

    private static void binaryPromotion()
    {
        putchar('b');
        putchar('.');
        putchar('p');
        putchar('r');
        putchar('o');
        putchar('m');
        putchar('\t');
        putchar('\t');

        int i = 2;
        float f = 6.0f;
        double d = 24.0;
        long l = 2l;
        putchar(i * f == d / l ? '.' : 'F');

        byte b = 0x1f;
        char c = 'G';
        int control = c & b;
        putchar(control == 7 ? '.' : 'F');

        float ff = (b==0) ? i : 4.0f;
        putchar(0.25 == 1.0 / ff ? '.' : 'F');

        putchar('\n');
    }

    private static void putchar(char c)
    {
        System.out.print(c);
    }
}
