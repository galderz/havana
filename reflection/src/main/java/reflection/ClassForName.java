package reflection;

public class ClassForName
{
    public static void main(String[] args) throws ClassNotFoundException
    {
        Assert.check();

        assert String.class == Class.forName("java.lang.String");
        assert String[].class == Class.forName("[Ljava.lang.String;");
    }
}
