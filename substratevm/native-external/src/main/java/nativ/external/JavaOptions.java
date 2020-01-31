package nativ.external;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class JavaOptions
{
    static String xxPlus(String parameter)
    {
        return String.format(
            "-XX:+%s"
            , parameter
        );
    }

    static String xxMinus(String parameter)
    {
        return String.format(
            "-XX:-%s"
            , parameter
        );
    }

    static String systemProperty(String key, String value)
    {
        return String.format(
            "-D%s=%s"
            , key
            , value
        );
    }

    static String addUnnamed(String modulePackage)
    {
        return String.format(
            "%s=ALL-UNNAMED"
            , modulePackage
        );
    }

    static Stream<String> addExports(String modulePackage)
    {
        return Stream.of(
            "--add-exports"
            , modulePackage
        );
    }

    static Stream<String> addOpens(String modulePackage)
    {
        return Stream.of(
            "--add-opens"
            , modulePackage
        );
    }

    static String xss(String xss)
    {
        return String.format(
            "-Xss%s"
            , xss
        );
    }

    static String xms(String xms)
    {
        return String.format(
            "-Xms%s"
            , xms
        );
    }

    static String xmx(String xmx)
    {
        return String.format(
            "-Xmx%s"
            , xmx
        );
    }

    static Stream<String> modulePath(String directory)
    {
        return Stream.of(
            "--module-path"
            , directory
        );
    }

    static String javaAgent(String javaAgent)
    {
        return String.format(
            "-javaagent:%s"
            , javaAgent
        );
    }

    static Stream<String> cp()
    {
        return Stream.of("-cp");
    }

    static Collector<CharSequence, ?, String> colon()
    {
        return Collectors.joining(":");
    }

    private JavaOptions()
    {
    }
}
