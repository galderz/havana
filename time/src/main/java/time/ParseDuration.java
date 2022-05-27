package time;

import java.time.Duration;

public class ParseDuration
{
    public static void main(String[] args)
    {
        System.out.println(
            parseDuration("0:42.90")
        );

        System.out.println(Duration.parse("PT45.2s"));
    }

    public static Duration parseDuration(String duration)
    {
        return Duration.parse(
            "PT"
            + duration.replace(':', 'M')
            + "S"
        );
    }
}
