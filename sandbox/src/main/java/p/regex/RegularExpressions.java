package p.regex;

public class RegularExpressions
{
    public static void main(String[] args)
    {
        String s1 = "127.0.0.1-11223";
        System.out.println(s1 + " -> " + replaced(s1));
    }

    static String replaced(String s) {
        return s.replaceAll("[^\\d]", "-");
    }

}
