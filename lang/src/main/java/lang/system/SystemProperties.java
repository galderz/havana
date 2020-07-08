package lang.system;

import java.util.TreeMap;

public class SystemProperties
{
    public static void main(String[] args)
    {
        // Quick and dirty way to sort system properties in alphabetical key order
        final var properties = new TreeMap<>(System.getProperties());
        properties.forEach((key, value) ->
            System.out.printf("%s=%s%n", key, value)
        );
    }
}
