package util.edges.v1;

public class Log
{
    public Log string(String s)
    {
        System.out.print(s);
        return this;
    }

    public void newline()
    {
        System.out.print(System.lineSeparator());
    }

    public void zhex(int num)
    {
        System.out.print(Integer.toHexString(num));
    }
}
