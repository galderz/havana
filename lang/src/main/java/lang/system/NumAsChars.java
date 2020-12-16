package lang.system;

public class NumAsChars
{
    public static void main(String[] args)
    {
        print(12345);
        System.out.println();
        print(-12345);
    }

    static void print(final int num)
    {
        int current;
        if (num < 0)
        {
            System.out.print('-');
            current = -num;
        }
        else
        {
            current = num;
        }

        if (current == 0)
            return;

        int rest = current / 10;
        print(rest);

        int digit = current % 10;
        System.out.print((char) ('0' + digit));
    }
    
}
