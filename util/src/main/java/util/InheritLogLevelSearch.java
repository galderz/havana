package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InheritLogLevelSearch
{
    public static void main(String[] args)
    {
        final var m = new HashMap<String, String>();
        m.put("io.quarkus.it.logging.minlevel.set.below", "TRACE");
        m.put("io.quarkus.it.logging.minlevel.set.below.child", "inherit");
        m.put("io.quarkus.it.logging.minlevel.set.below.child.grandchild", "inherit");
        m.put("io.quarkus.it.logging.minlevel.set.blah", "inherit");
        processLevels(m, "DEBUG");
    }

    private static void processLevels(HashMap<String, String> p, String defaultLevel)
    {
        for (Map.Entry<String, String> entry : p.entrySet())
        {
            final var level = getLogLevel(entry.getKey(), entry.getValue(), p, defaultLevel);
            System.out.printf("%s : %s%n", entry.getKey(), level.toLowerCase());
        }
    }

    private static String getLogLevel(String category, String level, Map<String, String> p, String defaultLevel)
    {
        if (Objects.isNull(level))
            return defaultLevel;

        if (!"inherit".equals(level))
            return level;

        int lastDotIndex = category.lastIndexOf('.');
        if (lastDotIndex == -1)
            return defaultLevel;

        String parent = category.substring(0, lastDotIndex);
        return getLogLevel(parent, p.get(parent), p, defaultLevel);
    }
}
