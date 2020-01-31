package nativ.external;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class NativeImageArguments
{
    static Stream<String> imageCp()
    {
        return Stream.of("-imagecp");
    }

    static Collector<CharSequence, ?, String> cLibraryPath()
    {
        return Collectors.joining(",");
    }

    static String h(String key, String value)
    {
        return String.format(
            "-H:%s=%s"
            , key
            , value
        );
    }

    private NativeImageArguments()
    {
    }
}
