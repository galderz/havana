package lang.system.conversions;

import static java.lang.System.out;

public class UnaryPromotion
{
    public static void main()
    {
        unaryPlus();
        unaryMinus();
        bitwiseComplement();
        shift();
    }

    private static void unaryPlus()
    {
        putchar('u');
        putchar('.');
        putchar('p');
        putchar('l');
        putchar('u');
        putchar('s');
        putchar('\t');
        putchar('\t');

        byte b = 10;
        int ib = +b;
        putchar(b == ib ? '.' : 'F');

        short s = 20;
        int is = +s;
        putchar(s == is ? '.' : 'F');

        char c = 'c';
        int ic = +c;
        putchar(c == ic ? '.' : 'F');

        putchar('\n');
    }

    private static void unaryMinus()
    {
        putchar('u');
        putchar('.');
        putchar('m');
        putchar('i');
        putchar('n');
        putchar('u');
        putchar('s');
        putchar('\t');
        putchar('\t');

        byte b = 10;
        int ib = -b;
        putchar(-b == ib ? '.' : 'F');

        short s = 20;
        int is = -s;
        putchar(-s == is ? '.' : 'F');

        char c = 'c';
        int ic = -c;
        putchar(-c == ic ? '.' : 'F');

        putchar('\n');
    }

    private static void bitwiseComplement()
    {
        putchar('u');
        putchar('.');
        putchar('c');
        putchar('o');
        putchar('m');
        putchar('p');
        putchar('l');
        putchar('\t');
        putchar('\t');

        byte b = 10;
        int ib = ~b;
        putchar(~b == ib ? '.' : 'F');

        short s = 20;
        int is = ~s;
        putchar(~s == is ? '.' : 'F');

        char c = 'c';
        int ic = ~c;
        putchar(~c == ic ? '.' : 'F');

        putchar('\n');
    }

    private static void shift()
    {
        putchar('u');
        putchar('.');
        putchar('s');
        putchar('h');
        putchar('i');
        putchar('f');
        putchar('t');
        putchar('\t');
        putchar('\t');

        byte b = 4;
        int ib = b << 4;
        int bi = 4 << b;
        int ibl = b << 4L;
        putchar(b == (ib >> 4) ? '.' : 'F');
        putchar(ib == bi ? '.' : 'F');
        putchar(ib == ibl ? '.' : 'F');

        short s = 8;
        int is = s << 8;
        int si = 8 << s;
        putchar(s == (is >> 8) ? '.' : 'F');
        putchar(is == si ? '.' : 'F');

        char c = 'c';
        int ic = c << 4;
        int ci = 4 << c;
        putchar(c == (ic >> 4) ? '.' : 'F');
        out.println(ci == 4 << 99 ? '.' : 'F');

    }

    private static void putchar(char c)
    {
        out.print(c);
    }
}
