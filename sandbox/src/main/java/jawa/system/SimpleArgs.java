package jawa.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleArgs
{
    public static void main(String[] args)
    {
        checkAssertEnabled();

        Map<String, List<String>> params;

        params = readArgs("--test");
        assert params.containsKey("test");

        params = readArgs("--install", "--version", "1.2.3");
        assert params.containsKey("install");
        assert params.get("version").equals(Arrays.asList("1.2.3")) : params;

        params = readArgs("--deploy", "--version", "1.2.3", "--maven-proxy", "http://a.b.c.d");
        assert params.containsKey("deploy");
        assert params.get("version").equals(Arrays.asList("1.2.3")) : params;
        assert params.get("maven-proxy").equals(Arrays.asList("http://a.b.c.d")) : params;

        params = readArgs("--artifacts", "a");
        assert params.get("artifacts").equals(Arrays.asList("a")) : params;

        params = readArgs("--artifacts", "a,b");
        assert params.get("artifacts").equals(Arrays.asList("a,b")) : params;

        params = readArgs("--artifacts", "a", "b");
        assert params.get("artifacts").equals(Arrays.asList("a", "b")) : params;
    }

    private static void checkAssertEnabled()
    {
        boolean enabled = false;
        assert enabled = true;
        if(!enabled)
            throw new AssertionError("assert not enabled");
    }

    static Map<String, List<String>> readArgs(String... args)
    {
        final Map<String, List<String>> params = new HashMap<>();

        List<String> options = null;
        for (int i = 0; i < args.length; i++)
        {
            final String arg = args[i];

            if (arg.startsWith("--"))
            {
                if (arg.length() < 3)
                {
                    System.err.println("Error at argument " + arg);
                    return params;
                }

                options = new ArrayList<>();
                params.put(arg.substring(2), options);
            }
            else if (options != null)
            {
                options.add(arg);
            }
            else
            {
                System.err.println("Illegal parameter usage");
                return params;
            }
        }

        return params;
    }
}
