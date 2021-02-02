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
        putchar(f * i == d / l ? '.' : 'F');
        putchar(f % i == d % l ? '.' : 'F');
        putchar(f + i == f - -i ? '.' : 'F');

        byte b = 0x1f;
        char c = 'G';
        putchar((c & b) == 7 ? '.' : 'F');
        putchar((c | b) == 95 ? '.' : 'F');
        putchar((c ^ b) == 88 ? '.' : 'F');

        float ff = (b==0) ? i : 4.0f;
        putchar(0.25 == 1.0 / ff ? '.' : 'F');

        putchar('\n');
    }

    private static void putchar(char c)
    {
        System.out.print(c);
    }
}
