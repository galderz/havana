package util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PrettyMap
{
    public static void main(String[] args)
    {
        final Map<String, String> expected = new HashMap<>();
        expected.put("org/example/ea/samples.EASample_03_ParameterEscape.sampleViaAux()I", "org/example/ea/samples/EASample_03_ParameterEscape$A@0");
        expected.put("org/example/ea/samples.EASample_01_Basic.sample(I)I", "org/example/ea/samples/EASample_01_Basic$A@0");
        expected.put("org/example/ea/samples.EASample_03_ParameterEscape.sample()I", "org/example/ea/samples/EASample_03_ParameterEscape$A@0");

        final Map<String, String> actual = new HashMap<>();
        actual.put("org/example/ea/samples.EASample_07_Runnable.sample1()C", "org/example/ea/samples/EASample_07_Runnable$SampleRunnable@");
        actual.put("org/example/ea/samples.EASample_03_ParameterEscape.sample()I", "org/example/ea/samples/EASample_03_ParameterEscape$A@");
        actual.put("org/example/ea/samples.EASample_01_Basic.sample(I)I", "org/example/ea/samples/EASample_01_Basic$A@");
        actual.put("org/example/ea/samples.EASample_03_ParameterEscape.sampleViaAux()I", "org/example/ea/samples/EASample_03_ParameterEscape$A@");

        if (!expected.equals(actual))
        {
            System.out.printf(
                "%nTest allocations:%nFailure!%nExpected (%d):%n%s,%nbut got (%d):%n%s%n%n"
                // , new Object[]{expected, actual}
                , new Object[]{
                    expected.size()
                    , String.join("\n", expected.entrySet().toArray())
                    //, Arrays.toString(expected.entrySet().toArray())
                    // , expected.entrySet().stream().map(e -> e.getKey() + " = " + e.getValue()).sorted().collect(Collectors.joining("\n"))
                    , actual.size()
                    , actual
                    // , actual.entrySet().stream().map(e -> e.getKey() + " = " + e.getValue()).sorted().collect(Collectors.joining("\n"))
                }
            );
        }
    }
}
