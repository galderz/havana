package jmh.basics;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class EscapeAnalysisBenchmark
{
    @Param(value = {"-1", "1"})
    private int value;

    @Benchmark
    public void test(Blackhole blackhole)
    {
        check(value > 0, "expected positive value: %s %s", blackhole, "blah", value);
    }

    static void check(boolean cond, String msg, Blackhole blackhole, Object... args)
    {
        if (!cond)
        {
            blackhole.consume(String.format(msg, args));
        }
    }
}
