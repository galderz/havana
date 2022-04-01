package p.regex;

import java.time.Duration;

public class RegularExpressions
{
    public static void main(String[] args)
    {
        String s1 = "127.0.0.1-11223";
        System.out.println(s1 + " -> " + replaced(s1));

        String s2 = "                        15.1s (9.5% of total time) in 69 GCs | Peak RSS: 4.22GB | CPU load: 1.96";
        final String[] elements = s2.split("\\s+");
        System.out.println(elements[12]);

        System.out.println(Duration.parse("PT" + "2m 38s".replace("m ", "M")));
    }

    static String replaced(String s) {
        return s.replaceAll("[^\\d]", "-");
    }

}
