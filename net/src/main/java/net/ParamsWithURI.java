package net;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Based on :
 * https://stackoverflow.com/a/13592567/186429
 * https://stackoverflow.com/a/55290418/186429
 */
public class ParamsWithURI
{
    public static void main(String[] args)
    {
        Assert.check();

        final var uri = URI.create("https://github.com/openjdk/jdk11u-dev/tree/master?depth=0");
        System.out.println(uri.getRawQuery());
        final var paramsMapList = splitQueryList(uri);
        assert paramsMapList.size() == 1 : paramsMapList;
        assert paramsMapList.get("depth").equals(List.of("0")) : paramsMapList;
        final var paramsMap = splitQuery(uri);
        assert paramsMap.size() == 1 : paramsMap;
        assert paramsMap.get("depth").equals("0") : paramsMap;
    }

    public static Map<String, String> splitQuery(URI uri)
    {
        if (uri.getRawQuery() == null || uri.getRawQuery().isEmpty())
        {
            return Collections.emptyMap();
        }

        return Stream.of(uri.getRawQuery().split("&"))
            .map(e -> e.split("="))
            .collect(
                Collectors.toMap(
                    e -> decode(e[0])
                    , e -> decode(e[1])
                )
            );
    }

    public static Map<String, List<String>> splitQueryList(URI uri)
    {
        if (uri.getRawQuery() == null || uri.getRawQuery().isEmpty())
        {
            return Collections.emptyMap();
        }

        return Stream.of(uri.getRawQuery().split("&"))
            .map(e -> e.split("="))
            .collect(
                Collectors.groupingBy(
                    e -> decode(e[0])
                    , HashMap::new
                    , Collectors.mapping(e -> decode(e[1]), Collectors.toList())
                )
            );
    }

    static String decode(String s)
    {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }
}
