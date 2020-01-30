package j.v8;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamsFlatMap
{
    public static void main(String[] args)
    {
        final var streams = Stream.of(
            "jdk.internal.vm.ci/jdk.vm.ci.runtime"
            , "jdk.internal.vm.ci/jdk.vm.ci.code"
        ).flatMap(module -> Stream.of("--add-exports", module));

        System.out.println(streams.collect(Collectors.toList()));
        // System.out.println(streams.collect(Collectors.joining(" ")));

    }
}
