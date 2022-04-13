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

        String s3 = "Finished generating 'hibernate-orm-quickstart-1.0.0-SNAPSHOT-runner' in 2m 38s.";
        final String[] s3split = s3.split("\\s+");
        System.out.println(s3split[4]);
        System.out.println(s3split[5].replace(".", ""));
        System.out.println(Duration.parse("PT" + s3split[4] + s3split[5].replace(".", "")));
    }

    static String replaced(String s) {
        return s.replaceAll("[^\\d]", "-");
    }

}
