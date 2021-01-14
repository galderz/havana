package lang.classes;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Classes
{
    public static void main(String[] args)
    {
        System.out.println(Void.class.getName());
        System.out.println(Void.class.getSimpleName());
        System.out.println(Void.class.getSimpleName().toLowerCase());
        System.out.println();
        System.out.println(ConcurrentHashMap.class.getName());
        System.out.println(shortPackageName(ConcurrentHashMap.class));
        System.out.println(shortClassName(ConcurrentHashMap.class));
        System.out.println(shortPackageClass(ConcurrentHashMap.class));
    }

    private static String shortPackageClass(Class<?> clazz)
    {
        final var pattern = Pattern.compile(
            "\\b[a-zA-Z]|[A-Z]|\\."
        );

        return pattern.matcher(clazz.getName())
            .results()
            .map(MatchResult::group)
            .collect(Collectors.joining(""));
    }


    private static String shortClassName(Class<?> clazz)
    {
        return clazz.getName().chars()
            .filter(Character::isUpperCase)
            .mapToObj(c -> String.valueOf((char) c))
            .collect(Collectors.joining(""));
    }

    private static String shortPackageName(Class<?> clazz)
    {
        return Stream
            .of(clazz.getPackageName().split("\\."))
            .map(subPackage -> subPackage.charAt(0))
            .map(String::valueOf)
            .collect(Collectors.joining("."));
    }

}
