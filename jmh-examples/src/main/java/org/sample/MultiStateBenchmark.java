package org.sample;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 0)
@Measurement(iterations = 1, time = 1)
@Fork(1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.Throughput)
public class MultiStateBenchmark
{
    @State(Scope.Thread)
    public static class StateXY
    {
        @Param({"100", "1000", "10000"})
        int x;

        @Param({"50", "80", "100"})
        int y;

        long[][] longs;

        @Setup
        public void setup()
        {
            longs = new long[x][y];
        }
    }

    @State(Scope.Thread)
    public static class StateRange
    {
        @Param({"90", "100"})
        int range;
    }

    @Benchmark
    public long[][] withXY(StateXY stateXY)
    {
        return stateXY.longs;
    }

    @Benchmark
    public int withRange(StateRange stateRange)
    {
        return stateRange.range;
    }
}
