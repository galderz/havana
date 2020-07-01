package net;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Based on https://stackoverflow.com/a/13592567/186429
 */
public class ParamsWithURI
{
    public static void main(String[] args)
    {
        Assert.check();

        final var uri = URI.create("https://github.com/openjdk/jdk11u-dev/tree/master?depth=0");
        System.out.println(uri.getRawQuery());
        final var parameters = splitQuery(uri);
        assert parameters.size() == 1 : parameters;
        assert parameters.get("depth").equals(List.of("0")) : parameters;
    }

    public static Map<String, List<String>> splitQuery(URI uri) {
        if (uri.getRawQuery() == null || uri.getRawQuery().isEmpty()) {
            return Collections.emptyMap();
        }
        return Stream.of(uri.getRawQuery().split("&"))
            .map(ParamsWithURI::splitQueryParameter)
            .collect(Collectors.groupingBy(SimpleImmutableEntry::getKey, LinkedHashMap::new, Collectors.mapping(Map.Entry::getValue, toList())));
    }

    public static SimpleImmutableEntry<String, String> splitQueryParameter(String element)
    {
        final var split = element.split("=");
        return new SimpleImmutableEntry<>(
            URLDecoder.decode(split[0], StandardCharsets.UTF_8),
            URLDecoder.decode(split[1], StandardCharsets.UTF_8)
        );
    }
}
